# Rush Hour Puzzle Solver

A Java application that solves Rush Hour puzzles using the Uniform Cost Search (UCS) algorithm, Greedy Best First Search (GBFS), and A*.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Make sure Java is in your system's PATH

## Running the Program

### Windows
1. Double-click the `run.bat` file, or
2. Open Command Prompt in the project directory and run:
   ```
   run.bat
   ```

### Linux/macOS
1. Open Terminal in the project directory
2. Make the script executable:
   ```
   chmod +x run.sh
   ```
3. Run the script:
   ```
   ./run.sh
   ```

### Manual Compilation and Run
If you prefer to compile and run manually:

1. Create the bin directory:
   ```
   mkdir bin
   ```

2. Compile the Java files:
   ```
   javac -d bin src/Main.java src/core/*.java src/io/*.java src/algorithms/*.java src/gui/*.java
   ```

3. Run the program:
   ```
   java -cp bin gui.MainWindow
   ```

## Program Structure
- `src/core/` - Core classes for the puzzle
- `src/io/` - Input/Output handling
- `src/algorithms/` - Puzzle solving algorithms
- `src/gui/` - Graphical user interface
- `test/input/` - Input configuration files
- `test/output/` - Output solution files

## Features
- Load puzzle configurations from files
- Visualize the solving process
- Step-by-step solution navigation
- Save solutions to files
- Exploration statistics display 