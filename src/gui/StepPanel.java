package gui;

import javax.swing.*;
import java.awt.*;
import io.Step;
import core.Piece;

public class StepPanel extends JPanel {
    private Step currentStep;
    private JLabel stepInfoLabel;
    private JLabel moveInfoLabel;
    private JLabel costInfoLabel;

    public StepPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(43, 43, 43));
        setForeground(new Color(200, 200, 200));

        stepInfoLabel = new JLabel("Step: 0/0");
        moveInfoLabel = new JLabel("Move: None");
        costInfoLabel = new JLabel("Cost: 0");

        styleLabel(stepInfoLabel);
        styleLabel(moveInfoLabel);
        styleLabel(costInfoLabel);

        add(stepInfoLabel);
        add(Box.createVerticalStrut(10));
        add(moveInfoLabel);
        add(Box.createVerticalStrut(10));
        add(costInfoLabel);
    }

    private void styleLabel(JLabel label) {
        label.setForeground(new Color(200, 200, 200));
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void updateStep(Step step, int currentStep, int totalSteps) {
        this.currentStep = step;
        stepInfoLabel.setText(String.format("Step: %d/%d", currentStep, totalSteps));

        if (step != null && step.getPiece() != null) {
            Piece piece = step.getPiece();
            moveInfoLabel.setText(String.format("Move: %s %s %d steps",
                    piece.getId(), step.getDirection(), step.getSteps()));
            costInfoLabel.setText(String.format("Cost: %d", step.getSteps()));
        } else {
            moveInfoLabel.setText("Move: None");
            costInfoLabel.setText("Cost: 0");
        }
    }
}