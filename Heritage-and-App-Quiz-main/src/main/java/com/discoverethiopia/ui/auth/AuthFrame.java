package com.discoverethiopia.ui.auth;

import javax.swing.*;
import java.awt.*;

public class AuthFrame extends JFrame {

    private CardLayout cardLayout;

    private JPanel mainPanel;

    public AuthFrame() {

        setTitle("Discover Ethiopia");

        setSize(950,550);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        cardLayout = new CardLayout();

        mainPanel = new JPanel(cardLayout);

        LoginPanel loginPanel =
                new LoginPanel(cardLayout, mainPanel);

        RegisterPanel registerPanel =
                new RegisterPanel(cardLayout, mainPanel);

        mainPanel.add(loginPanel, "LOGIN");

        mainPanel.add(registerPanel, "REGISTER");

        add(mainPanel);

        setVisible(true);
    }
}