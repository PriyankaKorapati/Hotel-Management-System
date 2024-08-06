package com.example;

public class Room {
    private int roomNumber;
    private String roomType;
    private String specialFeatures;
    private double price;
    private boolean occupied;

    // Full constructor
    public Room(int roomNumber, String roomType, String specialFeatures, double price, boolean occupied) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.specialFeatures = specialFeatures;
        this.price = price;
        this.occupied = occupied;
    }

    // Constructor with only roomNumber
    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    // Getter and Setter methods
    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public double getPrice() {
        return price;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", roomType='" + roomType + '\'' +
                ", specialFeatures='" + specialFeatures + '\'' +
                ", price=" + price +
                ", occupied=" + occupied +
                '}';
    }
}