<h1 align="center">🚗 Rush Hour Puzzle Solver 🚗</h1>

<p align="center">
<img src="https://img.shields.io/badge/Made%20with-Java-red.svg"/>
<img src="https://img.shields.io/badge/Made%20with-Swing-orange.svg"/>
<img src="https://img.shields.io/badge/License-MIT-green.svg"/>
<img src="https://img.shields.io/static/v1?label=%F0%9F%8C%9F&message=this%20Repository&style=style=flat&color=blue" alt="Star Badge"/>
</p>

<span align="center">

A Java application that solves Rush Hour puzzles using various search algorithms!<br/>
Uniform Cost Search (UCS) 🎯<br/>
Greedy Best First Search (GBFS) 🎯<br/>
A* Algorithm 🎯<br/>
Dijkstra's Algorithm 🎯<br/>

</span>

## 📋 Prerequisites

- Java Development Kit (JDK) 8 or higher
- Make sure Java is in your system's PATH

## 🚀 Running the Program

### Windows
1. Double-click the `run.bat` file, or
2. Open Command Prompt in the project directory and run:
   ```
   ./run.bat
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
   java -cp bin Main
   ```

## 📁 Program Structure
- `src/core/` - Core classes for the puzzle
- `src/io/` - Input/Output handling
- `src/algorithms/` - Puzzle solving algorithms
- `src/gui/` - Graphical user interface
- `test/input/` - Input configuration files
- `test/output/` - Output solution files

## ✨ Features
- Load puzzle configurations from files
- Visualize the solving process
- Step-by-step solution navigation
- Save solutions to files
- Exploration statistics display 

## 👥 Contributors
<p align="center">
  <table>
    <tr align="center">
      <td>
        <img src="https://github.com/RafaAbdussalam.png" width="100" height="100"><br>
        <b>Rafa Abdussalam Danadyaksa</b><br>
        13523133
      </td>
      <td>
        <img src="https://github.com/inRiza.png" width="100" height="100"><br>
        <b>Muhammad Rizain Firdaus</b><br>
        13523164
      </td>
    </tr>
  </table>
</p>
