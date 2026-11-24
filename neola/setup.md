On your **Mac**, install and open [Docker Desktop for Apple silicon](https://docs.docker.com/desktop/setup/install/mac-install/).

**1. Clone your repositories**

Open Terminal:

```sh
mkdir -p ~/NeoLA
cd ~/NeoLA

git clone https://github.com/BENODR27/neola-erp-frontend.git frontend
git clone https://github.com/BENODR27/neola-iam-backend.git authguardv2-node
git clone https://github.com/BENODR27/neola-mmms-backend.git mmms-backend
git clone https://github.com/BENODR27/neola-storage-service.git storage-service
git clone https://github.com/BENODR27/neola-subscription-backend.git subscription-backend
git clone https://github.com/BENODR27/neola-subscription-frontend.git subscription-frontend
```

**2. Prepare configuration once**

```sh
docker network create neola-platform

for app in frontend authguardv2-node mmms-backend storage-service subscription-backend subscription-frontend; do
  (
    cd "$app"
    [ -f .env.container ] || cp .env.container.example .env.container
    [ -f .env.deploy ] || cp .env.deploy.example .env.deploy
  )
done
```

Edit each package’s `.env.container` and `.env.deploy` with your actual settings.

**Before starting:** restore your Windows databases and uploads on the Mac, and preserve the existing passwords, JWT secret, Console encryption key and subscription activation settings. GitHub contains only your code.

**3. Start each repository**

Run these separately:

```sh
cd ~/NeoLA/subscription-backend && npm run docker:up
```

```sh
cd ~/NeoLA/authguardv2-node && npm run docker:up
```

```sh
cd ~/NeoLA/mmms-backend && npm run docker:up
```

```sh
cd ~/NeoLA/storage-service && npm run docker:up
```

```sh
cd ~/NeoLA/frontend && npm run docker:up
```

```sh
cd ~/NeoLA/subscription-frontend && npm run docker:up
```

The npm commands require Node.js/npm installed on your Mac. Alternatively, inside any repository use:

```sh
docker compose --env-file .env.deploy -f compose.independent.yaml up -d --build
```

**4. Open your apps**

- ERP: [http://localhost:5001](http://localhost:5001)
- Subscription Console: [http://localhost:5006](http://localhost:5006)

Use your existing credentials after the data restore.
