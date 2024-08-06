package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

// Removed the unused mainFrame field
public class RegisterPanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterPanel(MainFrame mainFrame) {
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterAction());
        add(registerButton);

        JButton toLoginButton = new JButton("Back to Login");
        toLoginButton.addActionListener(e -> mainFrame.showLoginPanel());
        add(toLoginButton);
    }

    private class RegisterAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (isInvalidInput(username, password)) {
                JOptionPane.showMessageDialog(RegisterPanel.this, "Username and Password are required and must not be only spaces.");
                return;
            }

            UserDAO userDAO = new UserDAO();
            try {
                boolean success = userDAO.registerUser(username, password);
                if (success) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "User registered successfully.");
                } else {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Username already exists. Please choose another one.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(RegisterPanel.this, "An error occurred while registering the user. Please try again.");
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