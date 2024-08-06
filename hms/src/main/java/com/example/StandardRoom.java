package com.example;

public class StandardRoom extends Room {
    private static final String ROOM_TYPE = "Standard";

    public StandardRoom(int roomNumber, String specialFeatures, double price, boolean occupied) {
        super(roomNumber, ROOM_TYPE, specialFeatures, price, occupied);
    }

    public StandardRoom(int roomNumber) {
        super(roomNumber);
        super.setRoomType(ROOM_TYPE);
    }

    // Additional methods and overrides if needed
}