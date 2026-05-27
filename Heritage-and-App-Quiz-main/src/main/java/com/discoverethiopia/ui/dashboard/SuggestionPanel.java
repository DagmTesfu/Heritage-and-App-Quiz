package com.discoverethiopia.ui.dashboard;

import com.discoverethiopia.dao.SuggestionDAO;
import com.discoverethiopia.model.User;
import com.discoverethiopia.ui.components.RoundedButton;
import com.discoverethiopia.ui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class SuggestionPanel extends JPanel {
    private final User currentUser;
    private final SuggestionDAO suggestionDAO;
    private JTextField nameField;
    private JTextField regionField;
    private JTextArea descriptionArea;
    private JTextArea reasonArea;
    private RoundedButton submitButton;

    public SuggestionPanel(User user) {
        this.currentUser = user;
        this.suggestionDAO = new SuggestionDAO();
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);

        // =========================
        // TOP PANEL (purple bar with title) – same style as HeritagePanel & QuizPanel
        // =========================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBackground(UIConstants.PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Submit a Suggestion");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        add(topPanel, BorderLayout.NORTH);

        // =========================
        // CENTER FORM (unchanged)
        // =========================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Heritage Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(30);
        nameField.setFont(UIConstants.NORMAL_FONT);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Region:"), gbc);
        gbc.gridx = 1;
        regionField = new JTextField(30);
        regionField.setFont(UIConstants.NORMAL_FONT);
        formPanel.add(regionField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = createTextArea(5);
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Why add it?"), gbc);
        gbc.gridx = 1;
        reasonArea = createTextArea(4);
        formPanel.add(new JScrollPane(reasonArea), gbc);

        add(formPanel, BorderLayout.CENTER);

        // =========================
        // SUBMIT BUTTON (bottom)
        // =========================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        submitButton = new RoundedButton("Submit Suggestion");
        submitButton.addActionListener(e -> submitSuggestion());
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextArea createTextArea(int rows) {
        JTextArea area = new JTextArea(rows, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(UIConstants.NORMAL_FONT);
        return area;
    }

    private void submitSuggestion() {
        String name = nameField.getText().trim();
        String region = regionField.getText().trim();
        String description = descriptionArea.getText().trim();
        String reason = reasonArea.getText().trim();
        if (name.isEmpty() || region.isEmpty() || description.isEmpty() || reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill every suggestion field.");
            return;
        }
        try {
            int suggestionId = suggestionDAO.createSuggestion(
                    currentUser.getUserId(),
                    name,
                    region,
                    description,
                    reason);
            JOptionPane.showMessageDialog(this,
                    "Thank you! Your suggestion has been submitted.\nReference ID: " + suggestionId);
            nameField.setText("");
            regionField.setText("");
            descriptionArea.setText("");
            reasonArea.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to submit suggestion: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
