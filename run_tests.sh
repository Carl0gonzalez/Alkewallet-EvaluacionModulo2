#!/usr/bin/env bash
set -euo pipefail

# Requiere en ./lib:
#   junit-4.13.2.jar
#   hamcrest-core-1.3.jar

rm -rf out
mkdir -p out

# Compila app + tests
javac -encoding UTF-8 -d out \
  -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" \
  $(find src -name "*.java") \
  $(find test -name "*.java")

# Ejecuta runner con listener (muestra START/OK/FAIL por test)
java -cp "out:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" com.alkewallet.test.TestRunner
