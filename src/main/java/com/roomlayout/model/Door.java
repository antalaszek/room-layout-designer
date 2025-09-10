package com.roomlayout.model;

public class Door extends WallItem {
    private final String type;
    
    public Door(Wall wall, double position, double width, double height, String type) {
        super(wall, position, width, height, 0);
        this.type = type;
    }
    
    public Door(Wall wall, double position, double width, double height) {
        this(wall, position, width, height, "Standard");
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return String.format("%s Door on %s wall: %.1fm wide x %.1fm high at position %.1fm", 
            type, wall, width, height, position);
    }
}