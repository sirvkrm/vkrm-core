#!/usr/bin/env bash
set -euo pipefail
if [ $# -ne 1 ]; then
  echo "usage: $0 /path/to/upstream/src" >&2
  exit 1
fi
TARGET=$1
REPO_ROOT=$(cd "$(dirname "$0")/.." && pwd)
rsync -a --exclude '.git' --exclude 'README.md' --exclude 'UPSTREAM.md' --exclude 'manifests' --exclude 'scripts' "$REPO_ROOT/" "$TARGET/"
