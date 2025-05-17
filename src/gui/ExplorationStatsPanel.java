package gui;

import javax.swing.*;
import java.awt.*;

public class ExplorationStatsPanel extends JPanel {
    private JLabel timeLabel;
    private JLabel nodesLabel;
    private JLabel queueLabel;

    public ExplorationStatsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(43, 43, 43));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                "Exploration Statistics",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                null,
                new Color(200, 200, 200)));

        // Initialize labels
        timeLabel = new JLabel("Time: -");
        nodesLabel = new JLabel("Nodes: -");
        queueLabel = new JLabel("Max Queue: -");

        // Style labels
        Color textColor = new Color(200, 200, 200);
        Font labelFont = new Font("Arial", Font.PLAIN, 12);
        styleLabel(timeLabel, textColor, labelFont);
        styleLabel(nodesLabel, textColor, labelFont);
        styleLabel(queueLabel, textColor, labelFont);

        // Add labels to panel
        add(Box.createVerticalStrut(5));
        add(timeLabel);
        add(Box.createVerticalStrut(5));
        add(nodesLabel);
        add(Box.createVerticalStrut(5));
        add(queueLabel);
        add(Box.createVerticalStrut(5));
    }

    private void styleLabel(JLabel label, Color textColor, Font font) {
        label.setForeground(textColor);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void updateStats(long explorationTime, int nodesExplored, int maxQueueSize) {
        timeLabel.setText(String.format("Time: %.3f seconds", explorationTime / 1000.0));
        nodesLabel.setText(String.format("Nodes: %d", nodesExplored));
        queueLabel.setText(String.format("Max Queue: %d", maxQueueSize));
    }

    public void reset() {
        timeLabel.setText("Time: -");
        nodesLabel.setText("Nodes: -");
        queueLabel.setText("Max Queue: -");
    }
}