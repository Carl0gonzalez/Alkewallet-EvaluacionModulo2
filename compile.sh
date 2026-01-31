#!/usr/bin/env bash
set -euo pipefail

rm -rf out
mkdir -p out

javac -encoding UTF-8 -d out $(find src -name "*.java")

echo "✅ Compilación OK (Java 17)."
