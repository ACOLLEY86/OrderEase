// Filename MenuItem.java
// Written by Anthony Colley
// Updated on 10/8/2024
// Final Project - Represents a Menu Item in the Restaurant

import java.io.Serializable;

/**
 * The MenuItem class represents a menu item offered by the restaurant.
 * It holds data about the item's name, description, price, and availability.
 */
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;         // The name of the menu item
    private String description;  // The description of the menu item
    private double price;        // The price of the menu item
    private boolean available;   // Indicates if the item is currently available

    /**
     * Constructor to initialize a menu item with specific details.
     *
     * @param name        The name of the item.
     * @param description The description of the item.
     * @param price       The price of the item.
     * @param available   The availability status of the item.
     */
    public MenuItem(String name, String description, double price, boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    // Getters and setters for menu item data
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}