package com.discoverethiopia.ui.auth;

import com.discoverethiopia.model.Role;
import com.discoverethiopia.ui.components.CustomPasswordField;
import com.discoverethiopia.ui.components.RoundedButton;
import com.discoverethiopia.ui.components.CustomTextField;
import com.discoverethiopia.ui.utils.UIConstants;

import com.discoverethiopia.model.User;
import com.discoverethiopia.dao.UserDAO;

import java.util.Optional;

import com.discoverethiopia.ui.dashboard.AdminDashboard;
import com.discoverethiopia.ui.dashboard.MainDashboard;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class LoginPanel extends JPanel {

    //components
    CustomTextField emailField;
    CustomPasswordField passwordField;
    RoundedButton loginBtn;

    public LoginPanel(CardLayout cardLayout, JPanel mainPanel) {

        setLayout(new GridLayout(1,2));

        // LEFT
        JPanel left = new JPanel();

        left.setBackground(UIConstants.PRIMARY_COLOR);

        left.setLayout(new GridBagLayout());

        JPanel content = new JPanel();

        content.setOpaque(false);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome Back!");

        title.setForeground(Color.WHITE);

        title.setFont(UIConstants.TITLE_FONT);

        JLabel desc = new JLabel(
                "Login to continue exploring Ethiopia!"
        );

        desc.setForeground(Color.WHITE);

        desc.setFont(UIConstants.NORMAL_FONT);

        RoundedButton createBtn =
                new RoundedButton("CREATE ACCOUNT");
        //RoundedButton extends JButton and it is found in RoundButton class

        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);

        content.add(Box.createRigidArea(new Dimension(0,10)));

        content.add(desc);

        content.add(Box.createRigidArea(new Dimension(0,25)));

        content.add(createBtn);

        left.add(content);

        // RIGHT
        JPanel right = new JPanel(new GridBagLayout());

        right.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Login");

        loginTitle.setFont(UIConstants.TITLE_FONT);

        loginTitle.setForeground(UIConstants.PRIMARY_COLOR);

        emailField = new CustomTextField("email", 20);

        passwordField =
                new CustomPasswordField("password",20);

        loginBtn =
                new RoundedButton("LOGIN");

        JLabel registerLabel =
                new JLabel("<-Don't have an account? Create one");

        registerLabel.setForeground(Color.GRAY);

        gbc.gridx = 0;

        gbc.gridy = 0;

        right.add(loginTitle, gbc);

        gbc.gridy++;

        right.add(emailField, gbc);

        gbc.gridy++;

        right.add(passwordField, gbc);

        gbc.gridy++;

        right.add(loginBtn, gbc);

        gbc.gridy++;

        right.add(registerLabel, gbc);

        add(left);

        add(right);

        // SWITCH
        createBtn.addActionListener(e -> {

            cardLayout.show(mainPanel, "REGISTER");

        });

        loginBtn.addActionListener(e->validateLogin());
    }
    public void validateLogin() {

        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try {

            UserDAO userDAO = new UserDAO();

            Optional<User> loggedUser =
                    userDAO.login(email, password);

            if (loggedUser.isPresent()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Login Successful!"
                );

                User currentUser = loggedUser.get();

                emailField.setText("email");
                passwordField.setText("password");

                if (currentUser.isAdmin()) {
                    new AdminDashboard(currentUser);
                } else {
                    new MainDashboard(currentUser);
                }
                SwingUtilities.getWindowAncestor(this).dispose();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Invalid email or password"
                );

                passwordField.setText("");
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage()
            );
        }
    }
}
