#!/bin/bash

# The Cipher Forge - Compilation Script
echo "=== Compiling The Cipher Forge ==="

# Create build directory
mkdir -p build/classes

# Compile all Java files
javac -d build/classes -cp src/main/java src/main/java/com/cipherforge/*/*.java src/main/java/com/cipherforge/*.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo "📦 Classes compiled to: build/classes/"
else
    echo "❌ Compilation failed!"
    exit 1
fi
