const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

exports.createProduct = async (req, res) => {
    try {
        const product = await prisma.product.create({ data: req.body });
        res.json(product);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.getAllProducts = async (req, res) => {
    const products = await prisma.product.findMany();
    res.json(products);
};

exports.getProductById = async (req, res) => {
    const { id } = req.params;
    const product = await prisma.product.findUnique({ where: { id: Number(id) } });
    if (product) res.json(product);
    else res.status(404).json({ message: "Product not found" });
};

exports.updateProduct = async (req, res) => {
    const { id } = req.params;
    try {
        const product = await prisma.product.update({
            where: { id: Number(id) },
            data: req.body,
        });
        res.json(product);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.deleteProduct = async (req, res) => {
    const { id } = req.params;
    await prisma.product.delete({ where: { id: Number(id) } });
    res.json({ message: "Product deleted" });
};

exports.upsertProduct = async (req, res) => {
    const { id, name, description, price, stock } = req.body;
    const product = await prisma.product.upsert({
        where: { id: id || 0 },
        update: { name, description, price, stock },
        create: { name, description, price, stock },
    });
    res.json(product);
};
