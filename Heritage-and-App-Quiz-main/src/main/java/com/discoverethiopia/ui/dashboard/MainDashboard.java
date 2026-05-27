package com.discoverethiopia.ui.dashboard;

import com.discoverethiopia.model.User;

import com.discoverethiopia.ui.auth.AuthFrame;
import com.discoverethiopia.ui.components.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    private CardLayout cardLayout;

    private JPanel contentPanel;

    public MainDashboard(User user) {

        setTitle("Discover Ethiopia Dashboard");

        setSize(1200, 750);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // SIDEBAR
        JPanel sidebar = createSidebar();

        add(sidebar, BorderLayout.WEST);

        // CONTENT AREA
        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);

        // PANELS
        contentPanel.add(
                new HomePanel(user),
                "HOME"
        );

        HeritagePanel heritagePanel = new HeritagePanel();
        heritagePanel.setDashboard(this);   // pass this MainDashboard
        contentPanel.add(heritagePanel, "HERITAGE");


        QuizPanel quizPanel = new QuizPanel(user);
        quizPanel.setDashboard(this);
        contentPanel.add(quizPanel, "QUIZ");

        contentPanel.add(new ProfilePanel(user),"PROFILE");

        contentPanel.add(new SuggestionPanel(user), "SUGGESTION");

        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createSidebar() {

        JPanel panel = new JPanel();

        panel.setPreferredSize(new Dimension(220, 0));

        panel.setBackground(new Color(45, 0, 70));

        panel.setLayout(new GridLayout(8,1,10,10));

        RoundedButton homeBtn =
                new RoundedButton("Home");

        RoundedButton heritageBtn =
                new RoundedButton("Heritage");

        RoundedButton quizBtn =
                new RoundedButton("Quiz");

        RoundedButton profileBtn =
                new RoundedButton("Profile");

        RoundedButton suggestBtn = new RoundedButton("Suggest");

        RoundedButton logoutBtn =
                new RoundedButton("Logout");


        // SWITCH PANELS
        homeBtn.addActionListener(e -> cardLayout.show(contentPanel, "HOME"));

        heritageBtn.addActionListener(e -> cardLayout.show(contentPanel, "HERITAGE"));

        quizBtn.addActionListener(e -> cardLayout.show(contentPanel, "QUIZ"));

        profileBtn.addActionListener(e -> cardLayout.show(contentPanel, "PROFILE"));

        suggestBtn.addActionListener(e -> cardLayout.show(contentPanel, "SUGGESTION"));

        logoutBtn.addActionListener(e -> {

            dispose();
            new AuthFrame();
            // BACK TO LOGIN
            // new AuthFrame();
        });

        panel.add(new JLabel(""));

        panel.add(homeBtn);

        panel.add(heritageBtn);

        panel.add(quizBtn);

        panel.add(profileBtn);

        panel.add(suggestBtn);

        panel.add(logoutBtn);

        return panel;
    }

    public void showSuggestionPanel() {
        cardLayout.show(contentPanel, "SUGGESTION");
    }
}
