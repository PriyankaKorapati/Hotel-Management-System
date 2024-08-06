package com.example;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private MainWindow mainWindow;

    public MainFrame() {
        setTitle("Hotel Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");

        add(mainPanel);
        setVisible(true);
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "Login");
    }

    public void showRegisterPanel() {
        cardLayout.show(mainPanel, "Register");
    }

    public void showMainWindow() {
        if (mainWindow == null) {
            mainWindow = new MainWindow();
        }
        // Dispose the current frame to only show the main window.
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}