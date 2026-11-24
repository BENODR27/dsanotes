**Committed and pushed all six repositories to `main`.** Host-IP routing and configuration checks passed. No shared Docker network setup is required.

**1. On your Mac, open Docker Desktop and clone:**

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

**2. Configure once inside each repo:**

```sh
cp -n .env.container.example .env.container
cp -n .env.deploy.example .env.deploy
```

In `.env.container`, replace example IP `192.168.1.50` with your Mac’s IP:

```sh
ipconfig getifaddr en0
```

Fill your existing database credentials, secrets and subscription settings. Set storage’s `STORAGE_VOLUME` in `.env.deploy`.

Your databases and uploads must already be restored and reachable.

**3. One deploy command inside each repo:**

```sh
docker compose --env-file .env.deploy -f compose.independent.yaml up -d --build
```

Run that same command separately in all six folders. No Node installation is needed on the Mac.

**4. Open:**

- **ERP:** `http://YOUR_MAC_IP:5001`
- **Subscription Console:** `http://YOUR_MAC_IP:5006`

Use your existing credentials.
