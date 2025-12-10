#!/bin/bash

# Configuration
APP_NAME="FastCar Location"
MAIN_CLASS="com.fastcar.ui.MainFrame"
LIB_DIR="lib"
BIN_DIR="bin"
SRC_DIR="src"
JAR_NAME="mysql-connector-j-9.5.0.jar"
CLASSPATH="$LIB_DIR/$JAR_NAME:$BIN_DIR"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "================================"
echo -e "${GREEN}$APP_NAME - Linux Launcher${NC}"
echo "================================"
echo ""

# Check if library exists
if [ ! -f "$LIB_DIR/$JAR_NAME" ]; then
    echo -e "${RED}[ERROR] Library not found: $LIB_DIR/$JAR_NAME${NC}"
    exit 1
fi

# Clean and create bin directory
echo "Preparing build directory..."
if [ -d "$BIN_DIR" ]; then
    rm -rf "$BIN_DIR"
fi
mkdir -p "$BIN_DIR"

# Find all Java files
echo "Finding source files..."
SOURCES=$(find "$SRC_DIR" -name "*.java")
if [ -z "$SOURCES" ]; then
    echo -e "${RED}[ERROR] No Java source files found in $SRC_DIR${NC}"
    exit 1
fi

# Compile
echo "Compiling..."
javac -d "$BIN_DIR" -cp "$LIB_DIR/$JAR_NAME" $SOURCES

if [ $? -eq 0 ]; then
    echo -e "${GREEN}[OK] Compilation successful${NC}"
    echo ""
    echo "Launching application..."
    echo ""
    
    # Run
    java -cp "$CLASSPATH" "$MAIN_CLASS"
else
    echo -e "${RED}[ERROR] Compilation failed!${NC}"
    exit 1
fi
