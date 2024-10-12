// Filename Server.java
// Written by Anthony Colley
// Updated on 10/8/2024
// Final Project - Represents a Server in the Restaurant

import java.io.Serializable;

/**
 * The Server class represents a server working in the restaurant.
 * It tracks the server's name, availability, and interactions with tables.
 */
public class Server implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;           // The name of the server
    private boolean available;     // Indicates if the server is available for assignment

    /**
     * Constructor to initialize a server with a specific name.
     *
     * @param name The name of the server.
     */
    public Server(String name) {
        this.name = name;
        this.available = true; // Server is initially available
    }

    // Getters and setters for server data
    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Notifies the server of a new order from a specific table.
     *
     * @param table The table that placed the new order.
     */
    public void notifyNewOrder(Table table) {
        System.out.println("Server " + name + " notified of new order for Table " + table.getTableNumber());
    }

    /**
     * Notifies the server when a guest calls them to the table.
     *
     * @param table The table that called the server.
     */
    public void notifyCall(Table table) {
        System.out.println("Server " + name + " notified of call from Table " + table.getTableNumber());
    }

    /**
     * Notifies the server when a guest requests the check.
     *
     * @param table The table that requested the check.
     */
    public void notifyCheckRequest(Table table) {
        System.out.println("Server " + name + " notified of check request from Table " + table.getTableNumber());
    }
}