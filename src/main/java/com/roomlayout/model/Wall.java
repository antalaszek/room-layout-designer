package com.roomlayout.model;

public enum Wall {
    NORTH("North"),
    SOUTH("South"),
    EAST("East"),
    WEST("West"),
    FLOOR("Floor"),
    CEILING("Ceiling");
    
    private final String name;
    
    Wall(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}