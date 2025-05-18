@echo off
echo Compiling Java files...
if not exist bin mkdir bin
javac -d bin src/Main.java src/core/*.java src/io/*.java src/algorithms/*.java src/gui/*.java src/heuristics/*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo.
echo Compilation successful!
echo.
echo Running Rush Hour Puzzle Solver...
echo.
java -cp bin gui.MainWindow

pause 