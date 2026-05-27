package com.discoverethiopia.ui.dashboard;

import com.discoverethiopia.dao.QuizDAO;
import com.discoverethiopia.model.QuizAttempt;
import com.discoverethiopia.model.User;
import com.discoverethiopia.ui.utils.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProfilePanel extends JPanel {

    private final User currentUser;
    private final QuizDAO quizDAO;

    public ProfilePanel(User user) {
        this.currentUser = user;
        this.quizDAO = new QuizDAO();

        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);

        // =========================
        // TOP PANEL (purple bar with title) – same style as other panels
        // =========================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBackground(UIConstants.PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        add(topPanel, BorderLayout.NORTH);

        // Center panel with profile card (wrapped for centering)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        centerPanel.add(createProfileCard());
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createProfileCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setMaximumSize(new Dimension(500, 450));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User details using GridBagLayout for clean alignment
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 0: Username
        addDetailRow(detailsPanel, gbc, "Username:", currentUser.getUsername(), 0);
        // Row 1: Email
        addDetailRow(detailsPanel, gbc, "Email:", currentUser.getEmail(), 1);
        // Row 2: Role
        String roleText = currentUser.isAdmin() ? "Administrator" : "Member";
        addDetailRow(detailsPanel, gbc, "Role:", roleText, 2);
        // Row 3: Member since
        String memberSince = currentUser.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        addDetailRow(detailsPanel, gbc, "Member since:", memberSince, 3);

        card.add(detailsPanel);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        // Quiz statistics (if any)
        JPanel statsPanel = createQuizStatsPanel();
        if (statsPanel != null) {
            card.add(statsPanel);
        }

        return card;
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc,
                              String labelText, String valueText, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.NORMAL_FONT);
        label.setForeground(UIConstants.SUBTEXT_COLOR);
        panel.add(label, gbc);

        gbc.gridx = 1;
        JLabel value = new JLabel(valueText);
        value.setFont(UIConstants.NORMAL_FONT);
        value.setForeground(UIConstants.TEXT_COLOR);
        panel.add(value, gbc);
    }

    private JPanel createQuizStatsPanel() {
        try {
            List<QuizAttempt> attempts = quizDAO.getAttemptsForUser(currentUser.getUserId());
            if (attempts.isEmpty()) {
                JLabel noStats = new JLabel("No quiz attempts yet.");
                noStats.setFont(UIConstants.NORMAL_FONT);
                noStats.setForeground(UIConstants.SUBTEXT_COLOR);
                noStats.setAlignmentX(Component.CENTER_ALIGNMENT);
                JPanel panel = new JPanel();
                panel.setBackground(Color.WHITE);
                panel.add(noStats);
                return panel;
            }

            int totalAttempts = attempts.size();
            double avgPercentage = attempts.stream()
                    .mapToInt(QuizAttempt::getPercentage)
                    .average()
                    .orElse(0.0);
            int bestScore = attempts.stream()
                    .mapToInt(QuizAttempt::getPercentage)
                    .max()
                    .orElse(0);

            JPanel statsPanel = new JPanel();
            statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
            statsPanel.setBackground(Color.WHITE);
            statsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

            JLabel statsTitle = new JLabel("Quiz Statistics");
            statsTitle.setFont(UIConstants.HERITAGE_SECTION_FONT);
            statsTitle.setForeground(UIConstants.PRIMARY_COLOR);
            statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            statsPanel.add(statsTitle);
            statsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            JLabel attemptsLabel = new JLabel("Total quizzes taken: " + totalAttempts);
            attemptsLabel.setFont(UIConstants.NORMAL_FONT);
            attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            statsPanel.add(attemptsLabel);

            JLabel avgLabel = new JLabel(String.format("Average score: %.1f%%", avgPercentage));
            avgLabel.setFont(UIConstants.NORMAL_FONT);
            avgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            statsPanel.add(avgLabel);

            JLabel bestLabel = new JLabel("Best score: " + bestScore + "%");
            bestLabel.setFont(UIConstants.NORMAL_FONT);
            bestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            statsPanel.add(bestLabel);

            return statsPanel;
        } catch (Exception e) {
            System.err.println("Could not load quiz stats: " + e.getMessage());
            return null;
        }
    }
}
