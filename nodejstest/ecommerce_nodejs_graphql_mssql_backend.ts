# Ecommerce Node.js + TypeScript + GraphQL + MSSQL (TypeORM)

This single file contains a scaffolded, production-oriented demo e-commerce backend using:

- Node.js + TypeScript
- Express + Apollo Server (GraphQL)
- TypeORM as ORM for Microsoft SQL Server (mssql)
- JWT authentication, bcrypt password hashing
- DataLoader pattern placeholder for batching
- Modular, service/repository architecture
- Docker Compose example to run SQL Server for local dev

---

///// PROJECT FILE TREE (logical) /////

// package.json
// tsconfig.json
// ormconfig.ts
// .env.example
// docker-compose.yml
// Dockerfile (optional)
// src/index.ts
// src/app.ts
// src/config.ts
// src/db.ts
// src/entities/User.ts
// src/entities/Category.ts
// src/entities/Product.ts
// src/entities/Order.ts
// src/entities/OrderItem.ts
// src/services/UserService.ts
// src/services/ProductService.ts
// src/services/OrderService.ts
// src/graphql/schema.ts
// src/graphql/resolvers/index.ts
// src/utils/auth.ts
// src/seed/seed.ts
// README.md

---

----- FILE: package.json -----
{
  "name": "ecommerce-graphql-mssql",
  "version": "1.0.0",
  "main": "dist/index.js",
  "license": "MIT",
  "scripts": {
    "start": "ts-node-dev --respawn --transpile-only src/index.ts",
    "build": "tsc",
    "typeorm:run": "ts-node ./node_modules/typeorm/cli.js",
    "seed": "ts-node src/seed/seed.ts"
  },
  "dependencies": {
    "apollo-server-express": "^3.11.1",
    "bcryptjs": "^2.4.3",
    "class-validator": "^0.14.0",
    "dataloader": "^2.0.0",
    "dotenv": "^16.0.3",
    "express": "^4.18.2",
    "graphql": "^16.6.0",
    "jsonwebtoken": "^9.0.0",
    "mssql": "^9.1.1",
    "reflect-metadata": "^0.1.13",
    "typeorm": "^0.3.16"
  },
  "devDependencies": {
    "@types/bcryptjs": "^2.4.2",
    "@types/express": "^4.17.17",
    "@types/jsonwebtoken": "^9.0.2",
    "@types/node": "^20.2.5",
    "ts-node": "^10.9.1",
    "ts-node-dev": "^2.0.0",
    "typescript": "^5.1.3"
  }
}

----- FILE: tsconfig.json -----
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "CommonJS",
    "outDir": "dist",
    "rootDir": "src",
    "strict": true,
    "esModuleInterop": true,
    "experimentalDecorators": true,
    "emitDecoratorMetadata": true,
    "skipLibCheck": true
  }
}

----- FILE: .env.example -----
# SQL Server connection
DB_HOST=localhost
DB_PORT=1433
DB_USERNAME=sa
DB_PASSWORD=YourStrong!Passw0rd
DB_NAME=ecommerce_db

# JWT
JWT_SECRET=replace_this_with_a_secure_secret
JWT_EXPIRES_IN=7d

# App
PORT=4000

----- FILE: ormconfig.ts -----
import { DataSource } from 'typeorm';
import * as dotenv from 'dotenv';
dotenv.config();

export const AppDataSource = new DataSource({
  type: 'mssql',
  host: process.env.DB_HOST || 'localhost',
  port: Number(process.env.DB_PORT || 1433),
  username: process.env.DB_USERNAME || 'sa',
  password: process.env.DB_PASSWORD || 'YourStrong!Passw0rd',
  database: process.env.DB_NAME || 'ecommerce_db',
  synchronize: false, // use migrations in prod; for demo you can set true
  logging: false,
  options: {
    encrypt: false
  },
  entities: [__dirname + '/entities/*.{ts,js}'],
  migrations: [__dirname + '/migration/*.{ts,js}']
});

----- FILE: docker-compose.yml -----
version: '3.8'
services:
  mssql:
    image: mcr.microsoft.com/mssql/server:2019-latest
    environment:
      SA_PASSWORD: "YourStrong!Passw0rd"
      ACCEPT_EULA: "Y"
    ports:
      - "1433:1433"
    healthcheck:
      test: ["CMD-SHELL", " /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P YourStrong!Passw0rd -Q \"SELECT 1\"" ]
      interval: 10s
      retries: 10

----- FILE: src/index.ts -----
import 'reflect-metadata';
import { AppDataSource } from '../ormconfig';
import { startServer } from './app';
import * as dotenv from 'dotenv';
dotenv.config();

const PORT = process.env.PORT || 4000;

async function main() {
  try {
    await AppDataSource.initialize();
    console.log('DataSource initialized');

    const server = await startServer();
    server.listen({ port: Number(PORT) }, () => {
      console.log(`Server is running on http://localhost:${PORT}/graphql`);
    });
  } catch (err) {
    console.error('Fatal error starting app', err);
    process.exit(1);
  }
}

main();

----- FILE: src/app.ts -----
import express from 'express';
import { ApolloServer } from 'apollo-server-express';
import { buildSchema } from './graphql/schema';
import { contextBuilder } from './utils/auth';

export async function startServer() {
  const app = express();

  const schema = await buildSchema();

  const server = new ApolloServer({
    schema,
    context: contextBuilder,
    introspection: true
  });

  await server.start();
  server.applyMiddleware({ app, path: '/graphql' });

  return app;
}

----- FILE: src/config.ts -----
export const JWT_SECRET = process.env.JWT_SECRET || 'replace_this_with_a_secure_secret';
export const JWT_EXPIRES_IN = process.env.JWT_EXPIRES_IN || '7d';

----- FILE: src/entities/User.ts -----
import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn } from 'typeorm';

@Entity()
export class User {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ unique: true })
  email!: string;

  @Column()
  password!: string; // hashed

  @Column({ default: 'customer' })
  role!: 'customer' | 'admin';

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}

----- FILE: src/entities/Category.ts -----
import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn } from 'typeorm';

@Entity()
export class Category {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ unique: true })
  name!: string;

  @Column({ nullable: true })
  description?: string;

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}

----- FILE: src/entities/Product.ts -----
import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn, CreateDateColumn, UpdateDateColumn } from 'typeorm';
import { Category } from './Category';

@Entity()
export class Product {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column()
  title!: string;

  @Column('text')
  description!: string;

  @Column('decimal', { precision: 12, scale: 2 })
  price!: number;

  @Column('int')
  stock!: number;

  @ManyToOne(() => Category, { nullable: true })
  @JoinColumn()
  category?: Category;

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}

----- FILE: src/entities/Order.ts -----
import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, OneToMany, ManyToOne, JoinColumn } from 'typeorm';
import { OrderItem } from './OrderItem';
import { User } from './User';

@Entity()
export class Order {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @ManyToOne(() => User)
  @JoinColumn()
  user!: User;

  @OneToMany(() => OrderItem, item => item.order, { cascade: true })
  items!: OrderItem[];

  @Column('decimal', { precision: 12, scale: 2 })
  total!: number;

  @Column({ default: 'created' })
  status!: 'created' | 'paid' | 'shipped' | 'cancelled';

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}

----- FILE: src/entities/OrderItem.ts -----
import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { Product } from './Product';
import { Order } from './Order';

@Entity()
export class OrderItem {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @ManyToOne(() => Product)
  @JoinColumn()
  product!: Product;

  @Column('int')
  quantity!: number;

  @Column('decimal', { precision: 12, scale: 2 })
  price!: number; // snapshot of product price

  @ManyToOne(() => Order, order => order.items)
  order!: Order;
}

----- FILE: src/services/UserService.ts -----
import { AppDataSource } from '../../ormconfig';
import { User } from '../entities/User';
import * as bcrypt from 'bcryptjs';
import * as jwt from 'jsonwebtoken';
import { JWT_SECRET, JWT_EXPIRES_IN } from '../config';

export class UserService {
  private repo = AppDataSource.getRepository(User);

  async register(email: string, password: string) {
    const existing = await this.repo.findOne({ where: { email } });
    if (existing) throw new Error('Email already in use');

    const hashed = await bcrypt.hash(password, 10);
    const user = this.repo.create({ email, password: hashed });
    await this.repo.save(user);

    const token = jwt.sign({ userId: user.id, role: user.role }, JWT_SECRET, { expiresIn: JWT_EXPIRES_IN });
    return { user, token };
  }

  async login(email: string, password: string) {
    const user = await this.repo.findOne({ where: { email } });
    if (!user) throw new Error('Invalid credentials');

    const match = await bcrypt.compare(password, user.password);
    if (!match) throw new Error('Invalid credentials');

    const token = jwt.sign({ userId: user.id, role: user.role }, JWT_SECRET, { expiresIn: JWT_EXPIRES_IN });
    return { user, token };
  }

  async findById(id: string) {
    return this.repo.findOne({ where: { id } });
  }
}

----- FILE: src/services/ProductService.ts -----
import { AppDataSource } from '../../ormconfig';
import { Product } from '../entities/Product';
import { Category } from '../entities/Category';

export class ProductService {
  private repo = AppDataSource.getRepository(Product);
  private catRepo = AppDataSource.getRepository(Category);

  async list({ skip = 0, take = 20 } = {}) {
    return this.repo.find({ relations: ['category'], skip, take });
  }

  async getById(id: string) {
    return this.repo.findOne({ where: { id }, relations: ['category'] });
  }

  async create(input: Partial<Product>) {
    if (input.category && typeof input.category === 'object' && (input.category as Category).id) {
      input.category = await this.catRepo.findOneBy({ id: (input.category as any).id });
    }
    const p = this.repo.create(input);
    return this.repo.save(p);
  }

  async update(id: string, input: Partial<Product>) {
    await this.repo.update(id, input);
    return this.getById(id);
  }

  async decreaseStock(productId: string, amount: number) {
    const p = await this.repo.findOneBy({ id: productId });
    if (!p) throw new Error('Product not found');
    if (p.stock < amount) throw new Error('Insufficient stock');
    p.stock -= amount;
    return this.repo.save(p);
  }
}

----- FILE: src/services/OrderService.ts -----
import { AppDataSource } from '../../ormconfig';
import { Order } from '../entities/Order';
import { OrderItem } from '../entities/OrderItem';
import { Product } from '../entities/Product';
import { User } from '../entities/User';

export class OrderService {
  private repo = AppDataSource.getRepository(Order);
  private productRepo = AppDataSource.getRepository(Product);
  private userRepo = AppDataSource.getRepository(User);

  async createOrder(userId: string, items: { productId: string; quantity: number }[]) {
    const user = await this.userRepo.findOneBy({ id: userId });
    if (!user) throw new Error('User not found');

    let total = 0;
    const orderItems: OrderItem[] = [];

    for (const it of items) {
      const p = await this.productRepo.findOneBy({ id: it.productId });
      if (!p) throw new Error(`Product ${it.productId} not found`);
      if (p.stock < it.quantity) throw new Error(`Not enough stock for product ${p.title}`);

      const oi = new OrderItem();
      oi.product = p;
      oi.quantity = it.quantity;
      oi.price = p.price;
      total += Number((p.price as any) * it.quantity);

      // decrease stock (simple); in real app wrap in transaction
      p.stock -= it.quantity;
      await this.productRepo.save(p);

      orderItems.push(oi);
    }

    const order = this.repo.create({ user, items: orderItems, total, status: 'created' });
    return this.repo.save(order);
  }

  async getById(id: string) {
    return this.repo.findOne({ where: { id }, relations: ['items', 'items.product', 'user'] });
  }
}

----- FILE: src/graphql/schema.ts -----
import { makeExecutableSchema } from '@graphql-tools/schema';
import { gql } from 'apollo-server-core';
import { resolvers } from './resolvers';

export async function buildSchema() {
  const typeDefs = gql`
    scalar Date

    type Query {
      me: User
      products(skip: Int, take: Int): [Product!]!
      product(id: ID!): Product
      order(id: ID!): Order
    }

    type Mutation {
      register(email: String!, password: String!): AuthPayload!
      login(email: String!, password: String!): AuthPayload!
      createProduct(input: ProductInput!): Product!
      createOrder(items: [OrderItemInput!]!): Order!
    }

    type User {
      id: ID!
      email: String!
      role: String!
      createdAt: Date!
    }

    type AuthPayload {
      token: String!
      user: User!
    }

    type Category {
      id: ID!
      name: String!
      description: String
    }

    type Product {
      id: ID!
      title: String!
      description: String!
      price: Float!
      stock: Int!
      category: Category
    }

    input ProductInput {
      title: String!
      description: String!
      price: Float!
      stock: Int!
      categoryId: ID
    }

    input OrderItemInput {
      productId: ID!
      quantity: Int!
    }

    type OrderItem {
      id: ID!
      product: Product!
      quantity: Int!
      price: Float!
    }

    type Order {
      id: ID!
      user: User!
      items: [OrderItem!]!
      total: Float!
      status: String!
      createdAt: Date!
    }
  `;

  return makeExecutableSchema({ typeDefs, resolvers });
}

----- FILE: src/graphql/resolvers/index.ts -----
import { GraphQLScalarType, Kind } from 'graphql';
import { UserService } from '../../services/UserService';
import { ProductService } from '../../services/ProductService';
import { OrderService } from '../../services/OrderService';

const userService = new UserService();
const productService = new ProductService();
const orderService = new OrderService();

export const resolvers = {
  Date: new GraphQLScalarType({
    name: 'Date',
    description: 'Date scalar type',
    parseValue(value) {
      return new Date(value);
    },
    serialize(value) {
      return (value as Date).toISOString();
    },
    parseLiteral(ast) {
      if (ast.kind === Kind.STRING) {
        return new Date(ast.value);
      }
      return null;
    }
  }),

  Query: {
    me: async (_: any, __: any, ctx: any) => {
      if (!ctx.user) return null;
      return userService.findById(ctx.user.userId);
    },
    products: async (_: any, args: any) => productService.list(args),
    product: async (_: any, { id }: any) => productService.getById(id),
    order: async (_: any, { id }: any, ctx: any) => {
      if (!ctx.user) throw new Error('Unauthorized');
      return orderService.getById(id);
    }
  },

  Mutation: {
    register: async (_: any, { email, password }: any) => userService.register(email, password),
    login: async (_: any, { email, password }: any) => userService.login(email, password),
    createProduct: async (_: any, { input }: any, ctx: any) => {
      // naive admin check
      if (!ctx.user || ctx.user.role !== 'admin') throw new Error('Unauthorized');
      const data: any = { ...input };
      if (input.categoryId) data.category = { id: input.categoryId };
      return productService.create(data);
    },
    createOrder: async (_: any, { items }: any, ctx: any) => {
      if (!ctx.user) throw new Error('Unauthorized');
      return orderService.createOrder(ctx.user.userId, items);
    }
  }
};

----- FILE: src/utils/auth.ts -----
import { Request } from 'express';
import * as jwt from 'jsonwebtoken';
import { JWT_SECRET } from '../config';
import { UserService } from '../services/UserService';

const userService = new UserService();

export function contextBuilder({ req }: { req: Request }) {
  // Called per-request by Apollo
  const token = req.headers.authorization?.replace('Bearer ', '') || '';
  if (!token) {
    return { req };
  }

  try {
    const payload: any = jwt.verify(token, JWT_SECRET);
    return { req, user: payload };
  } catch (err) {
    return { req };
  }
}

----- FILE: src/seed/seed.ts -----
import 'reflect-metadata';
import { AppDataSource } from '../../ormconfig';
import { Category } from '../entities/Category';
import { Product } from '../entities/Product';
import { User } from '../entities/User';
import * as bcrypt from 'bcryptjs';

async function seed() {
  await AppDataSource.initialize();
  const catRepo = AppDataSource.getRepository(Category);
  const prodRepo = AppDataSource.getRepository(Product);
  const userRepo = AppDataSource.getRepository(User);

  // categories
  const electronics = catRepo.create({ name: 'Electronics', description: 'Devices & gadgets' });
  const clothing = catRepo.create({ name: 'Clothing', description: 'Apparel' });
  await catRepo.save([electronics, clothing]);

  // products
  const p1 = prodRepo.create({ title: 'Wireless Headphones', description: 'Noise-cancelling', price: 99.99, stock: 50, category: electronics });
  const p2 = prodRepo.create({ title: 'T-Shirt', description: '100% cotton', price: 19.99, stock: 200, category: clothing });
  await prodRepo.save([p1, p2]);

  // admin user
  const admin = userRepo.create({ email: 'admin@example.com', password: await bcrypt.hash('password123', 10), role: 'admin' });
  const user = userRepo.create({ email: 'user@example.com', password: await bcrypt.hash('password123', 10), role: 'customer' });
  await userRepo.save([admin, user]);

  console.log('Seed complete');
  process.exit(0);
}

seed().catch(err => {
  console.error('Seed failed', err);
  process.exit(1);
});

----- FILE: README.md -----
# Demo E-commerce Backend (GraphQL + MSSQL)

## Quickstart

1. Copy `.env.example` to `.env` and update values.
2. Start SQL Server locally (or use Docker Compose):

   ```bash
   docker-compose up -d mssql
   ```

3. Install deps & seed DB:

   ```bash
   npm install
   npm run seed
   ```

4. Start the server:

   ```bash
   npm start
   ```

5. Open GraphQL playground: `http://localhost:4000/graphql`

## Example Queries

### Register / Login

```graphql
mutation {
  register(email:"new@demo.com", password:"pwd123") {
    token
    user { id email }
  }
}

mutation {
  login(email:"admin@example.com", password:"password123") {
    token
    user { id email role }
  }
}
```

Add `Authorization: Bearer <token>` header for authenticated requests.

### List products

```graphql
query {
  products { id title price stock category { name } }
}
```

### Create order (authenticated)

```graphql
mutation {
  createOrder(items: [{ productId: "<id-of-product>", quantity: 1 }]) {
    id
    total
    items { product { title } quantity price }
  }
}
```

## Notes & Next steps

- Turn `synchronize` to `false` and use TypeORM migrations for production.
- Wrap createOrder in a DB transaction for atomicity.
- Add validation, rate-limiting, logging, metrics, pagination cursors.
- Consider using DataLoader for batching product loads in resolvers.
- Add payment provider mock/integration and order lifecycle events.

---

// End of scaffold
