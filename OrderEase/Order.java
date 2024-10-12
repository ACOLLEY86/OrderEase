// Filename Order.java
// Written by Anthony Colley
// Updated on 10/8/2024
// Final Project - Manages Orders Placed at a Restaurant

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Order class represents an order placed by a customer.
 * It manages a list of menu items and calculates the total cost of the order.
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<MenuItem> items;  // List of items in the order
    private double totalCost;      // Total cost of the order

    /**
     * Constructor to initialize an empty order.
     */
    public Order() {
        items = new ArrayList<>();
        totalCost = 0.0;
    }

    /**
     * Adds a menu item to the order and updates the total cost.
     *
     * @param item The menu item to add.
     */
    public void addItem(MenuItem item) {
        items.add(item);
        totalCost += item.getPrice();
    }

    /**
     * Removes a menu item from the order and updates the total cost.
     *
     * @param item The menu item to remove.
     */
    public void removeItem(MenuItem item) {
        if (items.remove(item)) {
            totalCost -= item.getPrice();
        }
    }

    /**
     * Clears all items from the order and resets the total cost.
     */
    public void clearOrder() {
        items.clear();
        totalCost = 0.0;
    }

    // Getters for order data
    public List<MenuItem> getItems() {
        return items;
    }

    public double getTotalCost() {
        return totalCost;
    }
}