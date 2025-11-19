#!/bin/bash

# The Cipher Forge - Run Script
echo "=== Starting The Cipher Forge ==="

# Check if compiled
if [ ! -d "build/classes" ]; then
    echo "⚠️  Classes not found. Compiling first..."
    ./compile.sh
fi

# Run the application
java -cp build/classes com.cipherforge.CryptographySimulator
