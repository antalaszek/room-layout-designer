package com.roomlayout.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final double width;
    private final double length;
    private final double height;
    private final List<Furniture> furniture;
    private final List<Door> doors;
    private final List<Window> windows;
    
    public Room(double width, double length, double height) {
        if (width <= 0 || length <= 0 || height <= 0) {
            throw new IllegalArgumentException("Room dimensions must be positive");
        }
        this.width = width;
        this.length = length;
        this.height = height;
        this.furniture = new ArrayList<>();
        this.doors = new ArrayList<>();
        this.windows = new ArrayList<>();
    }
    
    public void addFurniture(Furniture item) {
        if (item.getX() < 0 || item.getY() < 0 || 
            item.getX() + item.getWidth() > width || 
            item.getY() + item.getLength() > length ||
            item.getHeight() > height) {
            throw new IllegalArgumentException("Furniture doesn't fit in the room");
        }
        furniture.add(item);
    }
    
    public void addDoor(Door door) {
        validateWallItem(door);
        doors.add(door);
    }
    
    public void addWindow(Window window) {
        validateWallItem(window);
        windows.add(window);
    }
    
    private void validateWallItem(WallItem item) {
        switch (item.getWall()) {
            case NORTH:
            case SOUTH:
                if (item.getPosition() < 0 || item.getPosition() + item.getWidth() > width) {
                    throw new IllegalArgumentException("Item doesn't fit on " + item.getWall() + " wall");
                }
                break;
            case EAST:
            case WEST:
                if (item.getPosition() < 0 || item.getPosition() + item.getWidth() > length) {
                    throw new IllegalArgumentException("Item doesn't fit on " + item.getWall() + " wall");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid wall for door/window");
        }
        
        if (item.getBottomHeight() < 0 || item.getBottomHeight() + item.getHeight() > height) {
            throw new IllegalArgumentException("Item doesn't fit vertically on wall");
        }
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
    
    public List<Furniture> getFurniture() {
        return new ArrayList<>(furniture);
    }
    
    public List<Door> getDoors() {
        return new ArrayList<>(doors);
    }
    
    public List<Window> getWindows() {
        return new ArrayList<>(windows);
    }
    
    @Override
    public String toString() {
        return String.format("Room: %.1fm x %.1fm x %.1fm (W x L x H)", width, length, height);
    }
}