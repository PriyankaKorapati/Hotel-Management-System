package com.example;

public class DeluxeRoom extends Room {
    private static final String ROOM_TYPE = "Deluxe";

    public DeluxeRoom(int roomNumber, String specialFeatures, double price, boolean occupied) {
        super(roomNumber, ROOM_TYPE, specialFeatures, price, occupied);
    }

    public DeluxeRoom(int roomNumber) {
        super(roomNumber);
        setRoomType(ROOM_TYPE);
    }

    // Additional methods and overrides if needed
}