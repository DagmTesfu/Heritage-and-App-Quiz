package com.discoverethiopia.ui.dashboard;

import com.discoverethiopia.model.User;
import com.discoverethiopia.ui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(User user) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);

        // =========================
        // TOP PANEL (purple bar with title)
        // =========================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBackground(UIConstants.PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleBarLabel = new JLabel("Home");
        titleBarLabel.setFont(UIConstants.TITLE_FONT);
        titleBarLabel.setForeground(Color.WHITE);
        topPanel.add(titleBarLabel);

        add(topPanel, BorderLayout.NORTH);

        // =========================
        // CENTER CONTENT (existing welcome layout)
        // =========================
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Image
        ImageIcon originalIcon = new ImageIcon("images/discover_ethiopia.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(850, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Welcome to Discover Ethiopia");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User label
        JLabel userLabel = new JLabel("Hello, " + user.getUsername());
        userLabel.setFont(UIConstants.NORMAL_FONT);
        userLabel.setForeground(Color.DARK_GRAY);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Description label
        JLabel descriptionLabel = new JLabel(
                "Explore Ethiopia's heritage sites, take quizzes, and review your progress."
        );
        descriptionLabel.setFont(UIConstants.NORMAL_FONT);
        descriptionLabel.setForeground(new Color(90, 90, 90));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with spacing
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(imageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(userLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(descriptionLabel);

        add(contentPanel, BorderLayout.CENTER);
    }
}
