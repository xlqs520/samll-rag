#!/usr/bin/env bash
set -euo pipefail

echo "生成的 MODEL_ENCRYPTION_KEY："
openssl rand -base64 32

echo
read -r -p "按回车键退出..."