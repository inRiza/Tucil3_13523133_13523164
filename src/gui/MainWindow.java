package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import core.Board;
import io.InputHandler;
import io.OutputHandler;
import algorithms.UCS;
import java.util.List;
import io.Step;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private JPanel boardPanel;
    private JPanel controlPanel;
    private StepPanel stepPanel;
    private ExplorationStatsPanel statsPanel;
    private JButton loadFileButton;
    private JButton startButton;
    private JButton nextButton;
    private JButton prevButton;
    private JButton autoButton;
    private JButton saveButton;
    private JComboBox<String> algorithmComboBox;
    private JLabel statusLabel;
    private JSpinner delaySpinner;
    private Board currentBoard;
    private Board initialBoard;
    private List<Step> solution;
    private int currentStep = 0;
    private Timer animationTimer;
    private static final int DEFAULT_DELAY = 1000; // 1 second per step

    // Exploration statistics
    private long explorationTime;
    private int nodesExplored;
    private int maxQueueSize;

    public MainWindow() {
        setTitle("Rush Hour Puzzle Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Set dark mode colors
        Color backgroundColor = new Color(43, 43, 43);
        Color textColor = new Color(200, 200, 200);
        Color buttonColor = new Color(60, 60, 60);
        Color buttonTextColor = new Color(200, 200, 200);

        // Main panel with dark background
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        add(mainPanel);

        // Control panel
        controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(backgroundColor);

        // Load file button
        loadFileButton = new JButton("Load Config File");
        styleButton(loadFileButton, buttonColor, buttonTextColor);
        loadFileButton.addActionListener(e -> loadConfigFile());

        // Algorithm selection
        String[] algorithms = { "UCS" };
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setBackground(buttonColor);
        algorithmComboBox.setForeground(buttonTextColor);

        // Start button
        startButton = new JButton("Start Solving");
        styleButton(startButton, buttonColor, buttonTextColor);
        startButton.addActionListener(e -> startSolving());
        startButton.setEnabled(false);

        // Navigation buttons
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        autoButton = new JButton("Auto Play");
        saveButton = new JButton("Save Solution");
        styleButton(prevButton, buttonColor, buttonTextColor);
        styleButton(nextButton, buttonColor, buttonTextColor);
        styleButton(autoButton, buttonColor, buttonTextColor);
        styleButton(saveButton, buttonColor, buttonTextColor);
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        autoButton.setEnabled(false);
        saveButton.setEnabled(false);

        prevButton.addActionListener(e -> showPreviousStep());
        nextButton.addActionListener(e -> showNextStep());
        autoButton.addActionListener(e -> toggleAutoPlay());
        saveButton.addActionListener(e -> saveSolution());

        // Delay spinner
        SpinnerNumberModel delayModel = new SpinnerNumberModel(DEFAULT_DELAY, 100, 5000, 100);
        delaySpinner = new JSpinner(delayModel);
        delaySpinner.setPreferredSize(new Dimension(80, 25));
        delaySpinner.setBackground(buttonColor);
        delaySpinner.setForeground(buttonTextColor);

        // Status label
        statusLabel = new JLabel("Load a config file to begin");
        statusLabel.setForeground(textColor);

        controlPanel.add(loadFileButton);
        controlPanel.add(new JLabel("Algorithm: "));
        controlPanel.add(algorithmComboBox);
        controlPanel.add(startButton);
        controlPanel.add(new JLabel("Delay (ms): "));
        controlPanel.add(delaySpinner);
        controlPanel.add(prevButton);
        controlPanel.add(nextButton);
        controlPanel.add(autoButton);
        controlPanel.add(saveButton);
        controlPanel.add(statusLabel);

        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Center panel for board and step info
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);

        // Left panel for board and stats
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(backgroundColor);

        // Board panel
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentBoard != null) {
                    drawBoard(g);
                }
            }
        };
        boardPanel.setBackground(backgroundColor);
        leftPanel.add(boardPanel, BorderLayout.CENTER);

        // Stats panel
        statsPanel = new ExplorationStatsPanel();
        leftPanel.add(statsPanel, BorderLayout.SOUTH);

        centerPanel.add(leftPanel, BorderLayout.CENTER);

        // Step panel
        stepPanel = new StepPanel();
        centerPanel.add(stepPanel, BorderLayout.EAST);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Animation timer
        animationTimer = new Timer(DEFAULT_DELAY, e -> {
            if (solution != null && currentStep < solution.size()) {
                showNextStep();
                if (currentStep == solution.size()) {
                    animationTimer.stop();
                    autoButton.setText("Auto Play");
                    statusLabel.setText("Solution complete!");
                }
            }
        });
    }

    private void saveSolution() {
        if (solution == null)
            return;

        String filename = JOptionPane.showInputDialog(this,
                "Enter filename to save solution (without extension):",
                "Save Solution",
                JOptionPane.QUESTION_MESSAGE);

        if (filename != null && !filename.trim().isEmpty()) {
            try {
                // Add .txt extension if not present
                if (!filename.toLowerCase().endsWith(".txt")) {
                    filename += ".txt";
                }
                OutputHandler.saveSolution(filename, solution, initialBoard, explorationTime, nodesExplored,
                        maxQueueSize);
                statusLabel.setText("Solution saved to: " + filename);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving solution: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showNextStep() {
        if (solution != null && currentStep < solution.size()) {
            currentStep++;
            updateBoardAndStep();
            updateNavigationButtons();
        }
    }

    private void showPreviousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateBoardAndStep();
            updateNavigationButtons();
        }
    }

    private void updateBoardAndStep() {
        if (currentStep > 0 && currentStep <= solution.size()) {
            currentBoard = solution.get(currentStep - 1).getBoard();
            stepPanel.updateStep(solution.get(currentStep - 1), currentStep, solution.size());
            boardPanel.repaint();
        }
    }

    private void updateNavigationButtons() {
        prevButton.setEnabled(currentStep > 0);
        nextButton.setEnabled(currentStep < solution.size());
        if (currentStep == solution.size()) {
            autoButton.setEnabled(false);
        }
    }

    private void toggleAutoPlay() {
        if (animationTimer.isRunning()) {
            animationTimer.stop();
            autoButton.setText("Auto Play");
        } else {
            int delay = (Integer) delaySpinner.getValue();
            animationTimer.setDelay(delay);
            animationTimer.start();
            autoButton.setText("Stop");
        }
    }

    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
    }

    private void loadConfigFile() {
        JFileChooser fileChooser = new JFileChooser("test/input");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                InputHandler inputHandler = new InputHandler();
                currentBoard = inputHandler.readInput(file.getPath());
                initialBoard = currentBoard; // Save initial board state
                startButton.setEnabled(true);
                statusLabel.setText("File loaded: " + file.getName());
                boardPanel.repaint();
                statsPanel.reset();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void startSolving() {
        if (currentBoard == null)
            return;

        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        if ("UCS".equals(selectedAlgorithm)) {
            long startTime = System.currentTimeMillis();
            solution = UCS.solve(currentBoard);
            explorationTime = System.currentTimeMillis() - startTime;
            nodesExplored = UCS.getNodesExplored();
            maxQueueSize = UCS.getMaxQueueSize();

            // Update stats panel
            statsPanel.updateStats(explorationTime, nodesExplored, maxQueueSize);

            if (solution != null) {
                currentStep = 0;
                stepPanel.updateStep(null, 0, solution.size());
                prevButton.setEnabled(false);
                nextButton.setEnabled(true);
                autoButton.setEnabled(true);
                saveButton.setEnabled(true);
            } else {
                stepPanel.updateStep(null, 0, 0);
                prevButton.setEnabled(false);
                nextButton.setEnabled(false);
                autoButton.setEnabled(false);
                saveButton.setEnabled(false);
            }
        }
    }

    private void drawBoard(Graphics g) {
        if (currentBoard == null)
            return;

        int cellSize = Math.min(
                boardPanel.getWidth() / currentBoard.getCols(),
                boardPanel.getHeight() / currentBoard.getRows());

        int startX = (boardPanel.getWidth() - cellSize * currentBoard.getCols()) / 2;
        int startY = (boardPanel.getHeight() - cellSize * currentBoard.getRows()) / 2;

        // Draw grid
        g.setColor(new Color(100, 100, 100));
        for (int i = 0; i <= currentBoard.getRows(); i++) {
            g.drawLine(startX, startY + i * cellSize,
                    startX + currentBoard.getCols() * cellSize, startY + i * cellSize);
        }
        for (int i = 0; i <= currentBoard.getCols(); i++) {
            g.drawLine(startX + i * cellSize, startY,
                    startX + i * cellSize, startY + currentBoard.getRows() * cellSize);
        }

        // Draw pieces
        char[][] grid = currentBoard.getGrid();
        for (int i = 0; i < currentBoard.getRows(); i++) {
            for (int j = 0; j < currentBoard.getCols(); j++) {
                if (grid[i][j] != '.') {
                    Color pieceColor = getPieceColor(grid[i][j]);
                    g.setColor(pieceColor);
                    g.fillRect(startX + j * cellSize + 1, startY + i * cellSize + 1,
                            cellSize - 2, cellSize - 2);
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(grid[i][j]),
                            startX + j * cellSize + cellSize / 2 - 4,
                            startY + i * cellSize + cellSize / 2 + 4);
                }
            }
        }

        // Draw exit
        g.setColor(new Color(255, 100, 100));
        g.drawString("K", startX + currentBoard.getExitCol() * cellSize + cellSize / 2 - 4,
                startY + currentBoard.getExitRow() * cellSize + cellSize / 2 + 4);
    }

    private Color getPieceColor(char pieceId) {
        // Generate different colors for different pieces
        int hash = pieceId * 31;
        return new Color(
                (hash & 0xFF0000) >> 16,
                (hash & 0x00FF00) >> 8,
                hash & 0x0000FF);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}