package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
//import java.util.regex.Pattern;

public class SignupFrame extends JFrame {
    private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;

    public SignupFrame() {
        setTitle("Signup");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        panel.add(signupButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Validate input
        if (isInvalidInput(username, password)) {
            JOptionPane.showMessageDialog(this, "Username and Password are required and must not be only spaces.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        try {
            boolean success = userDAO.registerUser(username, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "User registered successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose another one.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while registering the user. Please try again.");
        }
    }

    private boolean isInvalidInput(String username, String password) {
        return isStringEmptyOrOnlySpaces(username) || isStringEmptyOrOnlySpaces(password);
    }

    private boolean isStringEmptyOrOnlySpaces(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignupFrame::new);
    }
}