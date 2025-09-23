Nice — I’ll give you a complete, copy-pasteable guide to create an **Angular 19 app with SSR (server-side rendering)** and deploy it to an Ubuntu server (Node.js + systemd + Nginx reverse proxy + optional HTTPS via Certbot). I’ll include all commands, important files (server entry, systemd service, Nginx site), and notes about Node versions and `ng` commands. I used the official Angular docs and recent guides to ensure the commands match Angular 19’s recommended flow. ([Angular][1])

---

# Summary (what you’ll get)

1. Local dev setup (Node, Angular CLI).
2. Create Angular 19 app with SSR (`ng new --ssr` or `ng add @angular/ssr`). ([Angular][1])
3. Build for production and run the SSR server locally.
4. Deploy to an Ubuntu server: copy files, install Node, build, set up `systemd` service (or use `pm2`), configure Nginx as reverse proxy, enable HTTPS with Certbot (optional).
5. All necessary config files and commands included.

---

# Important prerequisites / compatibility

- Angular docs recommend Node **v20.19.0 or newer** for recent Angular CLI versions; Angular 19 is compatible with current Node 20+ (check your versions if you get errors). ([Angular][2])
- Ubuntu server: this guide assumes **Ubuntu 22.04/24.04** or similar.
- You need a domain (optional, but required for HTTPS) and sudo access on the server.

---

# 1) Local Development — create Angular 19 + SSR project

Open a terminal on your dev machine.

Install Node (if not already). I recommend `nvm` so you can pick exact Node versions:

```bash
# install nvm (if you don't have it)
curl -fsSL https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.6/install.sh | bash
# then open a new shell or source nvm
export NVM_DIR="$HOME/.nvm"; [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"

# install a Node version compatible with Angular 19 (example)
nvm install 20
nvm use 20

# verify
node -v
npm -v
```

Install Angular CLI (global):

```bash
npm install -g @angular/cli
```

Create a new Angular app with SSR scaffolding (recommended):
You can create the project with SSR already enabled:

```bash
ng new my-ssr-app --ssr
cd my-ssr-app
```

Or if you already have a project, add SSR with the Angular-maintained package:

```bash
# in project root
ng add @angular/ssr
```

(Older tutorials used `@nguniversal/express-engine`; Angular docs currently document `--ssr` or `ng add @angular/ssr` as the supported ways to enable SSR.) ([Angular][1])

After `ng new --ssr` or `ng add @angular/ssr` you should see:

- `server.ts` (entry for the server) or files appropriate to the new SSR engine,
- `main.server.ts`,
- `tsconfig.server.json`,
- new `architect` targets in `angular.json` (server / prerender / etc.),
- new `package.json` scripts like `build:ssr`, `serve:ssr`.

Run the dev SSR server (development):

```bash
# build & serve for SSR in dev
npm run build:ssr
npm run serve:ssr
# OR
ng run my-ssr-app:serve-ssr
```

Open `http://localhost:4200` (or the port shown) — pages should be server-rendered for initial HTML.

---

# 2) Example: check/update package.json scripts

After scaffolding, `package.json` typically contains something like:

```json
{
  "name": "my-ssr-app",
  "scripts": {
    "ng": "ng",
    "start": "ng serve",
    "build": "ng build",
    "preview": "ng run my-ssr-app:preview:production",
    "build:ssr": "ng run my-ssr-app:server:production && ng run my-ssr-app:build:production",
    "serve:ssr": "node dist/my-ssr-app/server/main.js",
    "dev:ssr": "ng run my-ssr-app:serve-ssr"
  }
}
```

_Note:_ exact script names can vary if your project name differs; adjust the `dist/<project>/server/main.js` path accordingly.

---

# 3) Production build locally (test before deployment)

```bash
# production build for browser and server
npm run build:ssr

# start the SSR server for testing locally:
npm run serve:ssr
# or
node dist/my-ssr-app/server/main.js
```

Visit `http://localhost:4000` (or the port the server logs) to test production SSR.

---

# 4) Prepare Ubuntu server (remote) — server-side steps

On your **Ubuntu server** (example IP `203.0.113.10`), SSH in and:

1. Install Node (use NodeSource or nvm). Example using NodeSource for Node 20:

```bash
# as sudo
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs build-essential
node -v
npm -v
```

(If you prefer `nvm`, install it and then `nvm install 20`.)

2. Install `nginx`, `certbot`, and utility packages:

```bash
sudo apt update
sudo apt install -y nginx git curl
# optional: certbot for LetsEncrypt
sudo apt install -y certbot python3-certbot-nginx
```

3. Create a system user for the app (recommended):

```bash
sudo useradd -m -s /bin/bash ssruser
sudo passwd ssruser
# or set up ssh keys and add them to ssruser's ~/.ssh/authorized_keys
```

---

# 5) Deployment strategy — copy source vs build artifact

Two common approaches:

A) Build on the server (clone repo, `npm ci`, `npm run build:ssr`) — recommended for simplicity.
B) Build locally and transfer `dist/` and `package.json`/`node_modules` to server — less common.

Here I'll show **A: build on server**.

On the server, as `ssruser`:

```bash
# clone your repo
cd ~
git clone https://github.com/you/your-repo.git my-ssr-app
cd my-ssr-app

# install dependencies
npm ci

# build
npm run build:ssr

# test run
node dist/my-ssr-app/server/main.js
# keep it running? we'll set up systemd or pm2 next
```

---

# 6) Keep the SSR server running: systemd (recommended) or pm2

### Option A — systemd service

Create `/etc/systemd/system/my-ssr-app.service` (run as root or via sudo):

```ini
[Unit]
Description=My Angular SSR App
After=network.target

[Service]
Type=simple
User=ssruser
WorkingDirectory=/home/ssruser/my-ssr-app
ExecStart=/usr/bin/node /home/ssruser/my-ssr-app/dist/my-ssr-app/server/main.js
Restart=always
RestartSec=5
Environment=PORT=4000 NODE_ENV=production

[Install]
WantedBy=multi-user.target
```

Then enable & start:

```bash
sudo systemctl daemon-reload
sudo systemctl enable my-ssr-app
sudo systemctl start my-ssr-app
sudo journalctl -u my-ssr-app -f
```

### Option B — pm2 (optional)

If you prefer pm2:

```bash
sudo npm install -g pm2
# as ssruser
pm2 start dist/my-ssr-app/server/main.js --name my-ssr-app --env production
pm2 save
pm2 startup systemd   # follow printed instructions
```

Either method will keep the Node SSR server running on `PORT` 4000 (or whatever you set).

---

# 7) Configure Nginx as reverse proxy

Create an Nginx site file `/etc/nginx/sites-available/my-ssr-app`:

```nginx
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;  # replace with your domain or server IP

    location / {
        proxy_pass http://127.0.0.1:4000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }

    # optional: static assets caching can be added here
}
```

Enable and test:

```bash
sudo ln -s /etc/nginx/sites-available/my-ssr-app /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

Visit `http://yourdomain.com` — it should proxy to your Node SSR server and serve SSR HTML.

---

# 8) Add HTTPS (Let's Encrypt with Certbot)

If you have a domain name and DNS pointed to server, run:

```bash
sudo certbot --nginx -d yourdomain.com -d www.yourdomain.com
```

Certbot will update Nginx config to redirect HTTP→HTTPS and install certificates. Certbot also sets up auto-renewal.

---

# 9) Useful production notes & SEO / caching

- Use `Cache-Control` headers for static assets (served by Nginx) to improve performance.
- Ensure `server.ts`/Express middleware is secure: avoid exposing debug details in production.
- Monitor memory/CPU: SSR can be heavier than SPA hosting. Consider horizontal scaling with multiple Node workers or a cluster manager if traffic is high.
- Angular docs describe SSR/hybrid rendering and the modern recommended commands: `ng new --ssr` or `ng add @angular/ssr`. ([Angular][1])

---

# 10) Example minimal `server.ts` (if your scaffold didn’t generate one)

If `ng add` didn't generate a server entry, here is a minimal Express server for SSR (adjust to your project name):

```ts
// server.ts (TypeScript) - compile to JS or use already generated main.js
import "zone.js/node";
import express from "express";
import { join } from "path";
import { existsSync } from "fs";

// Express server
const app = express();

const distFolder = join(process.cwd(), "dist/my-ssr-app/browser"); // browser build
const indexHtml = existsSync(join(distFolder, "index.original.html"))
  ? "index.original.html"
  : "index";

app.use(
  express.static(distFolder, {
    maxAge: "1y",
  })
);

app.get("*", (req, res) => {
  // This requires your server build's handler (renderModuleFactory or similar).
  // Usually the Angular tooling provides a compiled server bundle you can require and call.
  // For most setups the generated server main.js already sets up the handler,
  // so you won't need to write this manually.
  res.sendFile(join(distFolder, "index.html"));
});

const port = process.env.PORT || 4000;
app.listen(port, () => {
  console.log(`Node Express server listening on http://localhost:${port}`);
});
```

> Note: modern `ng add @angular/ssr` usually generates the necessary `main.server.ts` and server entry — prefer the generated files instead of hand rolling unless you need custom routes. ([Coder Legion][3])

---

# 11) Troubleshooting tips

- `ERR_REQUIRE_ESM` or module errors: check Node version and whether the scaffold produces ESM or CommonJS outputs. Use the Node version recommended by Angular (Node 20.19+ recommended). ([Angular][2])
- If `ng add @angular/ssr` fails: make sure packages (Angular core, CLI) are synced to compatible versions; sometimes clearing `node_modules` and `package-lock.json` and re-running `npm ci` helps.
- If assets 404 — ensure Nginx points to correct `dist/<project>/browser` folder and Node serves server bundle from `dist/<project>/server`.

---

# 12) Recap of core commands (copy-paste checklist)

Local:

```bash
# dev
nvm install 20
npm install -g @angular/cli
ng new my-ssr-app --ssr
cd my-ssr-app
npm run build:ssr
npm run serve:ssr
```

Server (Ubuntu):

```bash
# setup node & nginx
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs nginx git

# clone and build on server
sudo -u ssruser -i
cd ~
git clone https://github.com/you/your-repo.git my-ssr-app
cd my-ssr-app
npm ci
npm run build:ssr

# start via systemd (see service file above)
sudo systemctl enable --now my-ssr-app
# configure nginx and optionally certbot for HTTPS
```

---

# References / Sources

- Angular official SSR / Hybrid-rendering guide. ([Angular][1])
- Angular installation / Node version recommendation. ([Angular][2])
- Recent community guides & deployment examples for Angular Universal/SSR. ([ANGULARarchitects][4])

---

If you want, I can:

- generate a ready-to-deploy `systemd` file and `nginx` config with your **actual domain** and project name filled in, or
- produce a `Dockerfile` and `docker-compose` setup for containerized deployment, or
- create a small CI/CD script (GitHub Actions) that builds and deploys to your Ubuntu box over SSH.

Tell me which of those you'd like and give me your project name and domain (if you want configs filled in) — I’ll generate the exact files.

[1]: https://angular.dev/guide/ssr?utm_source=chatgpt.com "Server-side and hybrid-rendering"
[2]: https://angular.dev/installation?utm_source=chatgpt.com "Installation"
[3]: https://coderlegion.com/4175/how-to-add-angular-universal-ssr-to-your-angular-app?utm_source=chatgpt.com "How to Add Angular Universal (SSR) to Your Angular App"
[4]: https://www.angulararchitects.io/blog/guide-for-ssr/?utm_source=chatgpt.com "Updated: Guide for Server-Side Rendering (SSR) in Angular"
