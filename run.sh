#!/bin/bash

echo "Compiling Java files..."
mkdir -p bin
javac -d bin src/Main.java src/core/*.java src/io/*.java src/algorithms/*.java src/gui/*.java src/heuristics/*.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo
echo "Compilation successful!"
echo
echo "Running Rush Hour Puzzle Solver..."
echo
java -cp bin gui.MainWindow 