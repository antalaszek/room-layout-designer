package com.roomlayout.model;

public abstract class WallItem {
    protected final Wall wall;
    protected final double position;
    protected final double width;
    protected final double height;
    protected final double bottomHeight;
    
    public WallItem(Wall wall, double position, double width, double height, double bottomHeight) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Wall item dimensions must be positive");
        }
        if (bottomHeight < 0) {
            throw new IllegalArgumentException("Bottom height cannot be negative");
        }
        if (wall == Wall.FLOOR || wall == Wall.CEILING) {
            throw new IllegalArgumentException("Wall items cannot be placed on floor or ceiling");
        }
        
        this.wall = wall;
        this.position = position;
        this.width = width;
        this.height = height;
        this.bottomHeight = bottomHeight;
    }
    
    public Wall getWall() {
        return wall;
    }
    
    public double getPosition() {
        return position;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public double getBottomHeight() {
        return bottomHeight;
    }
}