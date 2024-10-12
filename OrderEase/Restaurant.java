// Filename Restaurant.java
// Written by Anthony Colley
// Updated on 10/8/2024
// Final Project - Manages Restaurant Data and Operations

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Restaurant class manages the overall restaurant data, including tables, servers, and menu items.
 * It provides methods for data persistence, server assignment, menu updates, and analytics.
 */
public class Restaurant implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Table> tables;     // List of tables in the restaurant
    private List<Server> servers;   // List of servers in the restaurant
    private List<MenuItem> menu;    // List of menu items offered by the restaurant

    /**
     * Constructor to initialize the restaurant data.
     */
    public Restaurant() {
        tables = new ArrayList<>();
        servers = new ArrayList<>();
        menu = new ArrayList<>();
    }

    // Getters for restaurant data
    public List<Table> getTables() {
        return tables;
    }

    public List<Server> getServers() {
        return servers;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    // Add a new table to the restaurant
    public void addTable(Table table) {
        tables.add(table);
    }

    // Add a new server to the restaurant
    public void addServer(Server server) {
        servers.add(server);
    }

    // Add a new menu item to the restaurant's menu
    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }

    /**
     * Reassigns a server to a specified table, updating the availability of both the current and new server.
     *
     * @param table     The table to which the server will be reassigned.
     * @param newServer The new server to be assigned to the table.
     */
    public void reassignServer(Table table, Server newServer) {
        // Set current server availability back to true if it exists
        Server currentServer = table.getAssignedServer();
        if (currentServer != null) {
            currentServer.setAvailable(true);
        }
        // Set the new server to the table and update its availability
        table.setAssignedServer(newServer);
        newServer.setAvailable(false);
    }

    /**
     * Returns a list of popular menu items based on the frequency of orders.
     *
     * @return List of popular menu items with their order count.
     */
    public List<String> getPopularItems() {
        return tables.stream()
                .flatMap(table -> table.getCurrentOrder().getItems().stream())
                .collect(Collectors.groupingBy(MenuItem::getName, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(entry -> entry.getKey() + " (" + entry.getValue() + " orders)")
                .collect(Collectors.toList());
    }

    /**
     * Saves the restaurant data to a file for persistence.
     *
     * @param filename The name of the file where the data will be saved.
     * @throws IOException If an I/O error occurs.
     */
    public void saveData(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(tables);
            out.writeObject(servers);
            out.writeObject(menu);
        }
    }

    /**
     * Loads the restaurant data from a file.
     *
     * @param filename The name of the file to load the data from.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public void loadData(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            tables = (List<Table>) in.readObject();
            servers = (List<Server>) in.readObject();
            menu = (List<MenuItem>) in.readObject();
        }
    }
}