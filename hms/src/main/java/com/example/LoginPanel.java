package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        add(loginButton);

        JButton toRegisterButton = new JButton("Register");
        toRegisterButton.addActionListener(e -> mainFrame.showRegisterPanel());
        add(toRegisterButton);
    }

    private class LoginAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (isInvalidInput(username, password)) {
                JOptionPane.showMessageDialog(LoginPanel.this, "Username and Password are required and must not be only spaces.");
                return;
            }

            UserDAO userDAO = new UserDAO();
            try {
                boolean success = userDAO.loginUser(username, password);
                if (success) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Login successful.");
                    mainFrame.showMainWindow(); // Use mainFrame to show the main application window
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid username or password.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(LoginPanel.this, "An error occurred while logging in. Please try again.");
            }
        }
    }

    private boolean isInvalidInput(String username, String password) {
        return isStringEmptyOrOnlySpaces(username) || isStringEmptyOrOnlySpaces(password);
    }

    private boolean isStringEmptyOrOnlySpaces(String input) {
        return input == null || input.trim().isEmpty();
    }
}