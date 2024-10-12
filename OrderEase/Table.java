// Filename Table.java
// Written by Anthony Colley
// Updated on 10/8/2024
// Final Project - Represents a Table in the Restaurant

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The Table class represents a table in the restaurant.
 * It holds data about the table number, assigned server, and current order.
 */
public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    private int tableNumber;          // The number identifying the table
    private Server assignedServer;    // The server assigned to the table
    private Order currentOrder;       // The current order associated with the table
    private LocalDateTime seatingTime; // The time the guests were seated

    /**
     * Constructor to initialize a table with a specific number.
     *
     * @param tableNumber The table number.
     */
    public Table(int tableNumber) {
        this.tableNumber = tableNumber;
        this.currentOrder = new Order(); // Initialize with an empty order
    }

    // Getters and setters for table data
    public int getTableNumber() {
        return tableNumber;
    }

    public Server getAssignedServer() {
        return assignedServer;
    }

    public void setAssignedServer(Server assignedServer) {
        this.assignedServer = assignedServer;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public LocalDateTime getSeatingTime() {
        return seatingTime;
    }

    public void setSeatingTime(LocalDateTime seatingTime) {
        this.seatingTime = seatingTime;
    }

    /**
     * Calculates the time since the guests were seated at the table.
     *
     * @return The duration in minutes since the guests were seated.
     */
    public long getSeatingDuration() {
        return java.time.Duration.between(seatingTime, LocalDateTime.now()).toMinutes();
    }
}
