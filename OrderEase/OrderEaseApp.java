// Filename OrderEaseApp.java
// Written by Anthony Colley
// Updated on 10/9/2024
// Final Project - Comprehensive Restaurant Management System

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * The OrderEaseApp class is the main GUI application for the OrderEase system.
 * It provides user interfaces for guests, servers, and admins to interact with
 * the restaurant management system.
 */
public class OrderEaseApp extends JFrame {
    private Restaurant restaurant;  // Manages restaurant data
    private Table currentTable;     // Tracks the current table being accessed by the guest

    /**
     * Constructor to initialize the application.
     */
    public OrderEaseApp() {
        restaurant = new Restaurant();
        try {
            restaurant.loadData("restaurant_data.dat"); // Load saved data
        } catch (IOException | ClassNotFoundException e) {
            populateSampleData(); // Load sample data if file is not found
        }

        // Default interface as Guest
        setupGuestUI();
    }

    /**
     * Sets up the user interface for guest users, allowing them to browse the menu and place orders.
     */
    private void setupGuestUI() {
        setTitle("OrderEase - Guest Interface");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton viewMenuButton = new JButton("View Menu");
        JButton placeOrderButton = new JButton("Place Order");
        JButton callServerButton = new JButton("Call Server");
        JButton requestCheckButton = new JButton("Request Check");
        JButton loginButton = new JButton("Server/Admin"); // Updated button text
    
        mainPanel.add(viewMenuButton);
        mainPanel.add(placeOrderButton);
        mainPanel.add(callServerButton);
        mainPanel.add(requestCheckButton);
        mainPanel.add(loginButton); // Add to the panel
    
        add(mainPanel);
    
        viewMenuButton.addActionListener(e -> displayMenu());
        placeOrderButton.addActionListener(e -> displayOrderButtons()); // Use buttons for order placement
        callServerButton.addActionListener(e -> callServer());
        requestCheckButton.addActionListener(e -> requestCheck());

        // Direct role selection without any login input
        loginButton.addActionListener(e -> chooseRole());

        // Initialize guest table and automatically assign server (using sample data)
        currentTable = restaurant.getTables().get(0); // Default to first table
        currentTable.setSeatingTime(LocalDateTime.now()); // Record seating time
        currentTable.setAssignedServer(restaurant.getServers().get(0)); // Auto-assign first server
    }

    /**
     * Displays the restaurant menu in a dialog for the guest.
     */
    private void displayMenu() {
        JTextArea menuArea = new JTextArea(10, 30);
        StringBuilder menuText = new StringBuilder("Menu:\n");
        for (MenuItem item : restaurant.getMenu()) {
            menuText.append(item.getName()).append(" - $")
                    .append(item.getPrice()).append(" : ")
                    .append(item.isAvailable() ? "Available" : "Not Available").append("\n");
        }
        menuArea.setText(menuText.toString());
        menuArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(menuArea), "Menu", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays available menu items as buttons for order placement.
     */
    private void displayOrderButtons() {
        JPanel panel = new JPanel(new GridLayout(restaurant.getMenu().size(), 1));
        for (MenuItem item : restaurant.getMenu()) {
            if (item.isAvailable()) {
                JButton button = new JButton(item.getName() + " - $" + item.getPrice());
                button.addActionListener(e -> placeOrder(item));
                panel.add(button);
            }
        }
        JOptionPane.showMessageDialog(this, panel, "Place Order", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Places the selected item into the guest's order.
     */
    private void placeOrder(MenuItem item) {
        currentTable.getCurrentOrder().addItem(item);
        JOptionPane.showMessageDialog(this, "Item added to order. Server will be notified.");
        Server assignedServer = currentTable.getAssignedServer();
        if (assignedServer != null) {
            assignedServer.notifyNewOrder(currentTable);
        }
    }

    /**
     * Simulates a guest calling a server to the table.
     */
    private void callServer() {
        Server assignedServer = currentTable.getAssignedServer();
        if (assignedServer != null) {
            JOptionPane.showMessageDialog(this, "Server " + assignedServer.getName() + " has been notified.");
            assignedServer.notifyCall(currentTable);
        } else {
            JOptionPane.showMessageDialog(this, "No server assigned to this table.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Simulates a guest requesting the check.
     */
    private void requestCheck() {
        JOptionPane.showMessageDialog(this, "Your check has been requested. A server will be with you shortly.");
        Server assignedServer = currentTable.getAssignedServer();
        if (assignedServer != null) {
            assignedServer.notifyCheckRequest(currentTable);
        }
    }

    /**
     * Direct role selection without login.
     */
    private void chooseRole() {
        // Create options for role selection
        String[] options = {"Server", "Admin"};
        int choice = JOptionPane.showOptionDialog(this,
                "Select Role:",
                "Role Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
    
        if (choice == 0) { // Server option selected
            Server server = restaurant.getServers().stream()
                    .filter(s -> s.getName().equalsIgnoreCase("Alice")) // Auto-select the first server "Alice"
                    .findFirst()
                    .orElse(null);
            if (server != null) {
                setupServerUI(server); // Go to server UI
            } else {
                JOptionPane.showMessageDialog(this, "Server not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (choice == 1) { // Admin option selected
            setupAdminUI(); // Go to admin UI
        }
    }

    /**
     * Sets up the user interface for the admin, allowing them to manage menu items and server assignments.
     */
    private void setupAdminUI() {
        setTitle("OrderEase - Admin Interface");
        getContentPane().removeAll(); // Clear existing UI
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton addMenuItemButton = new JButton("Add Menu Item");
        JButton removeMenuItemButton = new JButton("Remove Menu Item");
        JButton assignServerButton = new JButton("Assign Server to Table");
        JButton backToGuestButton = new JButton("Back to Guest");

        mainPanel.add(addMenuItemButton);
        mainPanel.add(removeMenuItemButton);
        mainPanel.add(assignServerButton);
        mainPanel.add(backToGuestButton);

        add(mainPanel);
        revalidate();
        repaint();

        addMenuItemButton.addActionListener(e -> addMenuItem());
        removeMenuItemButton.addActionListener(e -> removeMenuItem());
        assignServerButton.addActionListener(e -> assignServer());
        backToGuestButton.addActionListener(e -> {
            getContentPane().removeAll();  // Clear current server UI components
            setupGuestUI();                 // Set up the guest UI
            revalidate();                   // Revalidate the UI to apply changes
            repaint();                      // Repaint the JFrame to ensure the guest UI is displayed
        });
    }

    /**
     * Sets up the user interface for the server, allowing them to view and manage their assigned tables.
     *
     * @param server The server to display the interface for.
     */
    private void setupServerUI(Server server) {
        setTitle("OrderEase - Server Interface");
        getContentPane().removeAll(); // Clear existing UI
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton viewTablesButton = new JButton("View Tables");
        JButton checkInButton = new JButton("Check In");
        JButton markServedButton = new JButton("Mark Order Served");
        JButton backToGuestButton = new JButton("Back to Guest");
    
        mainPanel.add(viewTablesButton);
        mainPanel.add(checkInButton);
        mainPanel.add(markServedButton);
        mainPanel.add(backToGuestButton);
    
        add(mainPanel);
        revalidate();
        repaint();
    
        viewTablesButton.addActionListener(e -> viewTables(server));
        checkInButton.addActionListener(e -> checkInWithGuest(server));
        markServedButton.addActionListener(e -> markOrderServed(server));
    
        // Properly clear server UI and reset back to guest UI
        backToGuestButton.addActionListener(e -> {
            getContentPane().removeAll();  // Clear current server UI components
            setupGuestUI();                 // Set up the guest UI
            revalidate();                   // Revalidate the UI to apply changes
            repaint();                      // Repaint the JFrame to ensure the guest UI is displayed
        });
    }
    

    /**
     * Displays all tables assigned to the server and their status.
     */
    private void viewTables(Server server) {
        StringBuilder tableText = new StringBuilder("Assigned Tables:\n");
        for (Table table : restaurant.getTables()) {
            if (table.getAssignedServer() != null && table.getAssignedServer().getName().equalsIgnoreCase(server.getName())) {
                tableText.append("Table ").append(table.getTableNumber()).append(" - ")
                        .append("Seated for: ").append(table.getSeatingDuration()).append(" minutes")
                        .append(" - Order Total: $").append(table.getCurrentOrder().getTotalCost()).append("\n");
            }
        }
        JTextArea tableArea = new JTextArea(10, 30);
        tableArea.setText(tableText.toString());
        tableArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(tableArea), "Assigned Tables", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Allows the server to check in with a guest at a specified table.
     */
    private void checkInWithGuest(Server server) {
        String tableNumberStr = JOptionPane.showInputDialog(this, "Enter table number to check in:");
        if (tableNumberStr == null || tableNumberStr.trim().isEmpty()) {
            return; // Return early if no input
        }
        int tableNumber = Integer.parseInt(tableNumberStr);
        Table table = restaurant.getTables().stream()
                .filter(t -> t.getTableNumber() == tableNumber && t.getAssignedServer() == server)
                .findFirst()
                .orElse(null);

        if (table == null) {
            JOptionPane.showMessageDialog(this, "Table not found or not assigned to you.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Checked in with Table " + tableNumber + ".");
    }

    /**
     * Marks the order for a table as served.
     */
    private void markOrderServed(Server server) {
        String tableNumberStr = JOptionPane.showInputDialog(this, "Enter table number to mark as served:");
        if (tableNumberStr == null || tableNumberStr.trim().isEmpty()) {
            return; // Return early if no input
        }
        int tableNumber = Integer.parseInt(tableNumberStr);
        Table table = restaurant.getTables().stream()
                .filter(t -> t.getTableNumber() == tableNumber && t.getAssignedServer() == server)
                .findFirst()
                .orElse(null);

        if (table == null) {
            JOptionPane.showMessageDialog(this, "Table not found or not assigned to you.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        table.getCurrentOrder().clearOrder(); // Clear order after serving
        JOptionPane.showMessageDialog(this, "Order for Table " + tableNumber + " has been marked as served.");
    }

    /**
     * Adds a new menu item to the restaurant's menu.
     */
    private void addMenuItem() {
        String name = JOptionPane.showInputDialog(this, "Enter menu item name:");
        if (name == null || name.trim().isEmpty()) {
            return; // Return early if no input
        }
        String description = JOptionPane.showInputDialog(this, "Enter menu item description:");
        if (description == null || description.trim().isEmpty()) {
            return; // Return early if no input
        }
        String priceStr = JOptionPane.showInputDialog(this, "Enter menu item price:");
        if (priceStr == null || priceStr.trim().isEmpty()) {
            return; // Return early if no input
        }
        double price = Double.parseDouble(priceStr);
        String availableStr = JOptionPane.showInputDialog(this, "Is the item available? (yes/no)");
        boolean available = "yes".equalsIgnoreCase(availableStr);

        MenuItem newItem = new MenuItem(name, description, price, available);
        restaurant.addMenuItem(newItem);
        JOptionPane.showMessageDialog(this, "Menu item added.");
    }

    /**
     * Removes an existing menu item from the restaurant's menu.
     */
    private void removeMenuItem() {
        JPanel panel = new JPanel(new GridLayout(restaurant.getMenu().size(), 1));
        for (MenuItem item : restaurant.getMenu()) {
            JButton button = new JButton(item.getName());
            button.addActionListener(e -> {
                removeMenuItem(item);
            });
            panel.add(button);
        }
        JOptionPane.showMessageDialog(this, panel, "Remove Menu Item", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Removes the selected menu item.
     */
    private void removeMenuItem(MenuItem item) {
        restaurant.getMenu().remove(item);
        JOptionPane.showMessageDialog(this, "Menu item '" + item.getName() + "' has been removed.");
    }

    /**
     * Assigns a server to a specified table.
     */
    private void assignServer() {
        String tableNumberStr = JOptionPane.showInputDialog(this, "Enter table number:");
        if (tableNumberStr == null || tableNumberStr.trim().isEmpty()) {
            return; // Return early if no input
        }
        int tableNumber = Integer.parseInt(tableNumberStr);
        Table table = restaurant.getTables().stream()
                .filter(t -> t.getTableNumber() == tableNumber)
                .findFirst()
                .orElse(null);

        if (table == null) {
            JOptionPane.showMessageDialog(this, "Table not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String serverName = JOptionPane.showInputDialog(this, "Enter server name to assign:");
        if (serverName == null || serverName.trim().isEmpty()) {
            return; // Return early if no input
        }
        Server server = restaurant.getServers().stream()
                .filter(s -> s.getName().equalsIgnoreCase(serverName))
                .findFirst()
                .orElse(null);

        if (server == null || !server.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Server not available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        restaurant.reassignServer(table, server);
        JOptionPane.showMessageDialog(this, "Server assigned to table.");
    }

    /**
     * Populates the restaurant with sample data.
     */
    private void populateSampleData() {
        restaurant.addMenuItem(new MenuItem("Burger", "Beef patty with cheese", 8.99, true));
        restaurant.addMenuItem(new MenuItem("Pizza", "Pepperoni pizza", 12.99, true));
        restaurant.addMenuItem(new MenuItem("Salad", "Caesar salad", 6.99, false));
        restaurant.addServer(new Server("Alice"));
        restaurant.addServer(new Server("Bob"));
        restaurant.addTable(new Table(1));
        restaurant.addTable(new Table(2));
    }

    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrderEaseApp app = new OrderEaseApp();
            app.setVisible(true);
        });
    }
}
