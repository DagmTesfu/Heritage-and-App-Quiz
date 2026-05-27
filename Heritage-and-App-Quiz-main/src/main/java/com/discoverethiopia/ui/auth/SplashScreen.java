package com.discoverethiopia.ui.auth;

import com.discoverethiopia.ui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    public SplashScreen() {

        setTitle("Discover Ethiopia");

        setSize(700,500);

        setLocationRelativeTo(null);

        setUndecorated(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        panel.setBackground(Color.WHITE);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // IMAGE
        ImageIcon logo = new ImageIcon("images/discover_ethiopia.png");

        JLabel imageLabel = new JLabel(logo);

        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // TITLE
        JLabel title = new JLabel("Discover Ethiopia");

        title.setFont(UIConstants.TITLE_FONT);

        title.setForeground(UIConstants.PRIMARY_COLOR);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // DESCRIPTION
        JLabel desc = new JLabel(
                "Explore Ethiopia's rich heritage and test your knowledge!"
        );

        desc.setFont(UIConstants.NORMAL_FONT);

        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        // DEVELOPERS
        JLabel devs = new JLabel(
                "Developed by Biniam and Dagim"
        );

        devs.setFont(UIConstants.SMALL_FONT);

        devs.setForeground(Color.GRAY);

        devs.setAlignmentX(Component.CENTER_ALIGNMENT);

        // LOADING
        JProgressBar progressBar = new JProgressBar();

        progressBar.setIndeterminate(true);

        progressBar.setMaximumSize(new Dimension(300,20));

        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());

        panel.add(imageLabel);

        panel.add(Box.createRigidArea(new Dimension(0,20)));

        panel.add(title);

        panel.add(Box.createRigidArea(new Dimension(0,10)));

        panel.add(desc);

        panel.add(Box.createRigidArea(new Dimension(0,25)));

        panel.add(progressBar);

        panel.add(Box.createRigidArea(new Dimension(0,25)));

        panel.add(devs);

        panel.add(Box.createVerticalGlue());

        add(panel);

        setVisible(true);

        // AUTO OPEN LOGIN
        Timer timer = new Timer(3500, e -> {

            dispose();

            new AuthFrame();

        });

        timer.setRepeats(false);

        timer.start();
    }
}
