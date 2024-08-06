package com.example;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private Hotel hotel;
    private ExecutorService executorService;
    private JComboBox<Customer> customerComboBox;
    private JTextField editNameField;
    private JTextField editRoomNumberField;
    private JTextField editCheckInDateField;
    private JTextField editCheckOutDateField;

    public MainWindow() {
        hotel = new Hotel();
        executorService = Executors.newFixedThreadPool(4);

        // Set up JTattoo Look-and-Feel
        try {
            UIManager.setLookAndFeel(new AluminiumLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("Hotel Management System");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add heading
        JLabel headingLabel = new JLabel("HOTEL MANAGEMENT SYSTEM", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));

        add(headingLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Set the preferred size of the window
        setPreferredSize(new Dimension(900, 600));

        JPanel mainControlPanel = createMainControlPanel();
        tabbedPane.addTab("Main Controls", mainControlPanel);

        JPanel editCustomerPanel = createEditCustomerPanel();
        tabbedPane.addTab("Edit Customer", editCustomerPanel);

        add(tabbedPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);  // Centers the frame
        setVisible(true);
    }

    private JPanel createMainControlPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 4, 5, 5)); // 3 rows, 4 cols

        // Control buttons
        JButton checkInButton = new JButton("Check-in");
        JButton checkOutButton = new JButton("Check-out");
        JButton viewAllCustomersButton = new JButton("View all customers");
        JButton viewAllRoomsButton = new JButton("View all rooms");
        JButton generateOccupancyReportButton = new JButton("Generate Occupancy Report");
        JButton generateRevenueReportButton = new JButton("Generate Revenue Report");
        JButton saveCustomersButton = new JButton("Save Customers to File");
        JButton loadCustomersButton = new JButton("Load Customers from File");
        JButton createRoomButton = new JButton("Create Room");
        JButton editPricesButton = new JButton("Edit Prices");

        // Add buttons to the panel
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(viewAllCustomersButton);
        buttonPanel.add(viewAllRoomsButton);
        buttonPanel.add(generateOccupancyReportButton);
        buttonPanel.add(generateRevenueReportButton);
        buttonPanel.add(saveCustomersButton);
        buttonPanel.add(loadCustomersButton);
        buttonPanel.add(createRoomButton);
        //buttonPanel.add(editPricesButton);

        // Button actions
        checkInButton.addActionListener(e -> showCheckInPanel());
        checkOutButton.addActionListener(e -> showCheckOutPanel());
        viewAllCustomersButton.addActionListener(e -> viewAllCustomers());
        viewAllRoomsButton.addActionListener(e -> viewAllRooms());
        generateOccupancyReportButton.addActionListener(e -> generateOccupancyReport());
        generateRevenueReportButton.addActionListener(e -> generateRevenueReport());
        saveCustomersButton.addActionListener(e -> saveCustomers());
        loadCustomersButton.addActionListener(e -> loadCustomers());
        createRoomButton.addActionListener(e -> showCreateRoomPanel());
        editPricesButton.addActionListener(e -> showEditPricesPanel());

        return buttonPanel;
    }

    private JPanel createEditCustomerPanel() {
        JPanel editCustomerPanel = new JPanel(new BorderLayout(15, 15));

        // ComboBox and Load Button Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerComboBox = new JComboBox<>();
        JButton loadCustomerButton = new JButton("Load Customer");
        loadCustomerButton.addActionListener(e -> loadCustomerDetails());

        topPanel.add(new JLabel("Select Customer:"));
        topPanel.add(customerComboBox);
        topPanel.add(loadCustomerButton);

        // Edit Fields Panel
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridy++;
        fieldsPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridy++;
        fieldsPanel.add(new JLabel("Check-in Date (yyyy-MM-dd):"), gbc);
        gbc.gridy++;
        fieldsPanel.add(new JLabel("Check-out Date (yyyy-MM-dd):"), gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0;
        editNameField = new JTextField(15);
        fieldsPanel.add(editNameField, gbc);
        gbc.gridy++;
        editRoomNumberField = new JTextField(15);
        fieldsPanel.add(editRoomNumberField, gbc);
        gbc.gridy++;
        editCheckInDateField = new JTextField(15);
        fieldsPanel.add(editCheckInDateField, gbc);
        gbc.gridy++;
        editCheckOutDateField = new JTextField(15);
        fieldsPanel.add(editCheckOutDateField, gbc);

        // Update Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton updateCustomerButton = new JButton("Update Customer");
        updateCustomerButton.addActionListener(e -> updateCustomerDetails());
        buttonPanel.add(updateCustomerButton);

        // Add panels to the main panel
        editCustomerPanel.add(topPanel, BorderLayout.NORTH);
        editCustomerPanel.add(fieldsPanel, BorderLayout.CENTER);
        editCustomerPanel.add(buttonPanel, BorderLayout.SOUTH);

        return editCustomerPanel;
    }

    private void refreshCustomerComboBox() {
        customerComboBox.removeAllItems();
        List<Customer> customers = hotel.getAllCustomers();
        for (Customer customer : customers) {
            customerComboBox.addItem(customer);
        }
    }

    private void loadCustomerDetails() {
        if (customerComboBox.getItemCount() == 0) { // Ensure comboBox is populated
            refreshCustomerComboBox();
        }

        Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
        if (selectedCustomer != null) {
            editNameField.setText(selectedCustomer.getName());
            editRoomNumberField.setText(String.valueOf(selectedCustomer.getRoomNumber()));
            editCheckInDateField.setText(hotel.getDateFormat().format(selectedCustomer.getCheckInDate()));
            editCheckOutDateField.setText(hotel.getDateFormat().format(selectedCustomer.getCheckOutDate()));
        }
    }

    private void updateCustomerDetails() {
        Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
        if (selectedCustomer != null) {
            String newName = editNameField.getText();
            try {
                int newRoomNumber = Integer.parseInt(editRoomNumberField.getText());
                if (!hotel.isRoomValid(newRoomNumber)) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Invalid room number! Please provide a valid room number."));
                    return;
                }
                java.util.Date newCheckInDate = hotel.getDateFormat().parse(editCheckInDateField.getText());
                java.util.Date newCheckOutDate = hotel.getDateFormat().parse(editCheckOutDateField.getText());

                if (!isDateValid(newCheckInDate, newCheckOutDate)) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Invalid dates! Check-out date should be equal to or after check-in date."));
                    return;
                }

                boolean updateSuccess = hotel.updateCustomerDetails(selectedCustomer.getRoomNumber(), newName, newRoomNumber,
                        new java.sql.Date(newCheckInDate.getTime()), new java.sql.Date(newCheckOutDate.getTime()));

                SwingUtilities.invokeLater(() -> {
                    if (updateSuccess) {
                        JOptionPane.showMessageDialog(this, "Customer details updated successfully!");
                        refreshCustomerComboBox();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update customer details.");
                    }
                });
            } catch (ParseException | NumberFormatException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Invalid input! Please provide correct information."));
            }
        }
    }

    private void showCreateRoomPanel() {
        JPanel createRoomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        createRoomPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridy++;
        createRoomPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridy++;
        createRoomPanel.add(new JLabel("Special Features (Deluxe only):"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField roomNumberField = new JTextField(15);
        createRoomPanel.add(roomNumberField, gbc);
        gbc.gridy++;
        JComboBox<String> roomTypeComboBox = new JComboBox<>(new String[]{"Standard", "Deluxe"});
        createRoomPanel.add(roomTypeComboBox, gbc);
        gbc.gridy++;
        JTextField specialFeaturesField = new JTextField(15);
        createRoomPanel.add(specialFeaturesField, gbc);

        int result = JOptionPane.showConfirmDialog(this, createRoomPanel, "Create New Room", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                String roomType = (String) roomTypeComboBox.getSelectedItem();
                String specialFeatures = specialFeaturesField.getText();

                boolean roomCreationSuccess = hotel.createRoom(roomNumber, roomType, specialFeatures);
                if (roomCreationSuccess) {
                    JOptionPane.showMessageDialog(this, "Room creation successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Room creation failed. Room number might already exist.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please provide correct information.");
            }
        }
    }

    private void showEditPricesPanel() {
        JPanel editPricesPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        editPricesPanel.add(new JLabel("Standard Room Price:"), gbc);
        gbc.gridy++;
        editPricesPanel.add(new JLabel("Deluxe Room Price:"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField standardPriceField = new JTextField(String.valueOf(hotel.getStandardRoomPrice()), 15);
        editPricesPanel.add(standardPriceField, gbc);
        gbc.gridy++;
        JTextField deluxePriceField = new JTextField(String.valueOf(hotel.getDeluxeRoomPrice()), 15);
        editPricesPanel.add(deluxePriceField, gbc);

        int result = JOptionPane.showConfirmDialog(this, editPricesPanel, "Edit Room Prices", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double standardPrice = Double.parseDouble(standardPriceField.getText());
                double deluxePrice = Double.parseDouble(deluxePriceField.getText());

                hotel.setStandardRoomPrice(standardPrice);
                hotel.setDeluxeRoomPrice(deluxePrice);

                JOptionPane.showMessageDialog(this, "Room prices updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please provide correct information.");
            }
        }
    }

    private void showCheckInPanel() {
        JPanel checkInPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        checkInPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridy++;
        checkInPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridy++;
        checkInPanel.add(new JLabel("Check-in Date (yyyy-MM-dd):"), gbc);
        gbc.gridy++;
        checkInPanel.add(new JLabel("Check-out Date (yyyy-MM-dd):"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField nameField = new JTextField(15);
        checkInPanel.add(nameField, gbc);
        gbc.gridy++;
        JTextField roomNumberField = new JTextField(15);
        checkInPanel.add(roomNumberField, gbc);
        gbc.gridy++;
        JTextField checkInDateField = new JTextField(15);
        checkInPanel.add(checkInDateField, gbc);
        gbc.gridy++;
        JTextField checkOutDateField = new JTextField(15);
        checkInPanel.add(checkOutDateField, gbc);

        int result = JOptionPane.showConfirmDialog(this, checkInPanel, "Check-In Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                java.util.Date checkInDate = hotel.getDateFormat().parse(checkInDateField.getText());
                java.util.Date checkOutDate = hotel.getDateFormat().parse(checkOutDateField.getText());

                if (!hotel.isRoomValid(roomNumber)) {
                    JOptionPane.showMessageDialog(this, "Invalid room number! Please provide a valid room number.");
                    return;
                }
                if (!isDateValid(checkInDate, checkOutDate)) {
                    JOptionPane.showMessageDialog(this, "Invalid dates! Check-out date should be equal to or after check-in date.");
                    return;
                }

                boolean checkInSuccess = hotel.checkInCustomer(name, roomNumber, new java.sql.Date(checkInDate.getTime()), new java.sql.Date(checkOutDate.getTime()));
                if (checkInSuccess) {
                    JOptionPane.showMessageDialog(this, "Check-in successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Check-in failed. Room might already be occupied.");
                }
            } catch (ParseException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please provide correct information.");
            }
        }
    }

    private void showCheckOutPanel() {
        JPanel checkOutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        checkOutPanel.add(new JLabel("Room Number:"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        JTextField roomNumberField = new JTextField(15);
        checkOutPanel.add(roomNumberField, gbc);

        int result = JOptionPane.showConfirmDialog(this, checkOutPanel, "Check-Out Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int roomNumber = Integer.parseInt(roomNumberField.getText());

                if (!hotel.isRoomValid(roomNumber)) {
                    JOptionPane.showMessageDialog(this, "Invalid room number! Please provide a valid room number.");
                    return;
                }

                boolean checkOutSuccess = hotel.checkOutCustomer(roomNumber);
                if (checkOutSuccess) {
                    JOptionPane.showMessageDialog(this, "Check-out successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Check-out failed. Room might not be occupied.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please provide correct information.");
            }
        }
    }

    private void viewAllCustomers() {
        executorService.execute(() -> {
            refreshCustomerComboBox();
            StringBuilder customersInfo = new StringBuilder("All Customers:\n");
            List<Customer> customers = hotel.getAllCustomers();
            customers.forEach(customer -> customersInfo.append(customer).append("\n"));
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, customersInfo.toString()));
        });
    }

    private void viewAllRooms() {
        executorService.execute(() -> {
            StringBuilder roomsInfo = new StringBuilder("All Rooms:\n");
            List<Room> rooms = hotel.getAllRooms();
            rooms.forEach(room -> roomsInfo.append(room).append("\n"));
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, roomsInfo.toString()));
        });
    }

    private void generateOccupancyReport() {
        executorService.execute(() -> {
            String report = hotel.generateOccupancyReport();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, report));
        });
    }

    private void generateRevenueReport() {
        executorService.execute(() -> {
            String report = hotel.generateRevenueReport();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, report));
        });
    }

    private void saveCustomers() {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Customers saved successfully!"));
    }

    private void loadCustomers() {
        SwingUtilities.invokeLater(() -> {
            refreshCustomerComboBox();
            JOptionPane.showMessageDialog(this, "Customers loaded successfully!");
        });
    }

    private static boolean isDateValid(java.util.Date checkInDate, java.util.Date checkOutDate) {
        return !checkOutDate.before(checkInDate);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
