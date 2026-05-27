package com.discoverethiopia.ui.auth;

import com.discoverethiopia.ui.components.CustomPasswordField;
import com.discoverethiopia.ui.components.RoundedButton;
import com.discoverethiopia.ui.components.CustomTextField;
import com.discoverethiopia.ui.utils.UIConstants;

import com.discoverethiopia.dao.UserDAO;
import com.discoverethiopia.model.User;

import com.discoverethiopia.ui.dashboard.MainDashboard;

import java.util.Optional;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    CustomTextField nameField;
    CustomTextField emailField;
    CustomPasswordField passwordField;


    public RegisterPanel(CardLayout cardLayout,
                         JPanel mainPanel) {

        setLayout(new GridLayout(1,2));

        // LEFT
        JPanel left = new JPanel();

        left.setBackground(UIConstants.PRIMARY_COLOR);

        left.setLayout(new GridBagLayout());

        JPanel content = new JPanel();

        content.setOpaque(false);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Discover Ethiopia");

        title.setForeground(Color.WHITE);

        title.setFont(UIConstants.TITLE_FONT);

        JLabel desc = new JLabel(
                "Create an account and start exploring"
        );

        desc.setForeground(Color.WHITE);

        desc.setFont(UIConstants.NORMAL_FONT);

        RoundedButton signInBtn =
                new RoundedButton("SIGN IN");

        content.add(title);

        content.add(Box.createRigidArea(new Dimension(0,10)));

        content.add(desc);

        content.add(Box.createRigidArea(new Dimension(0,25)));

        content.add(signInBtn);

        left.add(content);

        // RIGHT
        JPanel right = new JPanel(new GridBagLayout());

        right.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel registerTitle =
                new JLabel("Create Account");

        registerTitle.setFont(UIConstants.TITLE_FONT);

        registerTitle.setForeground(UIConstants.PRIMARY_COLOR);

        nameField = new CustomTextField("Username",20);

        emailField = new CustomTextField("email",20);

        passwordField =
                new CustomPasswordField("password",20);

        RoundedButton registerBtn =
                new RoundedButton("REGISTER");

        gbc.gridx = 0;

        gbc.gridy = 0;

        right.add(registerTitle, gbc);

        gbc.gridy++;

        right.add(nameField, gbc);

        gbc.gridy++;

        right.add(emailField, gbc);

        gbc.gridy++;

        right.add(passwordField, gbc);

        gbc.gridy++;

        right.add(registerBtn, gbc);

        add(left);

        add(right);

        signInBtn.addActionListener(e -> {

            cardLayout.show(mainPanel, "LOGIN");

        });

        registerBtn.addActionListener(e -> validateRegister());
    }

    public void validateRegister() {

        String username = nameField.getText();
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try {

            UserDAO userDAO = new UserDAO();

            // register user
            userDAO.createUser(username, email, password);

            JOptionPane.showMessageDialog(
                    null,
                    "Registration Successful!"
            );

            // automatic login after registration
            Optional<User> loggedUser =
                    userDAO.login(email, password);

            if (loggedUser.isPresent()) {

                User currentUser = loggedUser.get();

                // open dashboard
                new MainDashboard(currentUser);

                SwingUtilities.getWindowAncestor(this).dispose();

            }

            // clear fields
            nameField.setText("Username");
            emailField.setText("email");
            passwordField.setText("password");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage()
            );
        }
    }
}
