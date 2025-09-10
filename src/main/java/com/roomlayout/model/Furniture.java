package com.roomlayout.model;

public class Furniture {
    private final String name;
    private final double width;
    private final double length;
    private final double height;
    private final double x;
    private final double y;
    private final double rotation;
    
    public Furniture(String name, double width, double length, double height, double x, double y) {
        this(name, width, length, height, x, y, 0);
    }
    
    public Furniture(String name, double width, double length, double height, double x, double y, double rotation) {
        if (width <= 0 || length <= 0 || height <= 0) {
            throw new IllegalArgumentException("Furniture dimensions must be positive");
        }
        this.name = name;
        this.width = width;
        this.length = length;
        this.height = height;
        this.x = x;
        this.y = y;
        this.rotation = ((rotation % 360) + 360) % 360;
    }
    
    public String getName() {
        return name;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getLength() {
        return length;
    }
    
    public double getHeight() {
        return height;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getRotation() {
        return rotation;
    }
    
    public Point2D getCenter() {
        return new Point2D(x + width / 2, y + length / 2);
    }
    
    @Override
    public String toString() {
        return String.format("%s: %.1fx%.1fx%.1fm at (%.1f, %.1f)", 
            name, width, length, height, x, y);
    }
}