#!/bin/sh
# Install private Mac test settings; never writes databases or resets accounts.
set -eu
umask 077
set -C
bundle=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
target=${1:-"$HOME/NeoLA"}
ip=${2:-${MAC_HOST_IP:-}}
if [ -z "$ip" ]; then
  interface=$(route -n get default 2>/dev/null | awk '$1 == "interface:" { print $2; exit }')
  ip=$(ipconfig getifaddr "${interface:-en0}" 2>/dev/null || true)
fi
case "$ip" in ''|*[!0-9.]*) echo 'Supply your Mac IPv4 address as the second argument.' >&2; exit 1 ;; esac
printf '%s\n' "$ip" | awk -F. 'NF != 4 { exit 1 } { for (i=1; i<=4; i++) if ($i == "" || $i < 0 || $i > 255) exit 1 }' || { echo 'Invalid IPv4 address.' >&2; exit 1; }
apps='common-db frontend authguardv2-node mmms-backend storage-service subscription-backend subscription-frontend'
for app in $apps; do
  files='.env.container .env.deploy'
  compose=compose.independent.yaml
  if [ "$app" = common-db ]; then files=.env; compose=compose.yaml; fi
  [ -f "$target/$app/$compose" ] || { echo "Missing cloned repo: $target/$app" >&2; exit 1; }
  for file in $files; do
    if [ -e "$target/$app/$file" ] && ! sed "s/__MAC_HOST_IP__/$ip/g" "$bundle/$app/$file" | cmp -s - "$target/$app/$file"; then
      echo "Existing $app/$file differs and was preserved. Back it up and review before replacing it." >&2
      exit 1
    fi
  done
done
for app in $apps; do
  files='.env.container .env.deploy'
  if [ "$app" = common-db ]; then files=.env; fi
  for file in $files; do
    if [ ! -e "$target/$app/$file" ]; then
      sed "s/__MAC_HOST_IP__/$ip/g" "$bundle/$app/$file" > "$target/$app/$file"
    fi
    chmod 600 "$target/$app/$file"
  done
done
printf 'Installed/matched 13 private environment files.\nDatabase: %s:5433\nERP: http://%s:5001\nConsole: http://%s:5006\n' "$ip" "$ip" "$ip"
echo 'Restore existing backups into common-db before testing original logins. No accounts or data were reset.'
