package com.roomlayout.model;

public class Window extends WallItem {
    private final String type;
    
    public Window(Wall wall, double position, double width, double height, double bottomHeight, String type) {
        super(wall, position, width, height, bottomHeight);
        this.type = type;
    }
    
    public Window(Wall wall, double position, double width, double height, double bottomHeight) {
        this(wall, position, width, height, bottomHeight, "Standard");
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return String.format("%s Window on %s wall: %.1fm wide x %.1fm high at position %.1fm, %.1fm from floor", 
            type, wall, width, height, position, bottomHeight);
    }
}