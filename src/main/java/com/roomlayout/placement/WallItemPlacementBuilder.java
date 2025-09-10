package com.roomlayout.placement;

import com.roomlayout.model.*;

public final class WallItemPlacementBuilder {
    private final String name;
    private final double width;
    private final double height;
    private final double bottomHeight;
    private final Room room;
    private final WallItemType itemType;
    
    public enum WallItemType {
        DOOR, WINDOW
    }
    
    public WallItemPlacementBuilder(String name, double width, double height, double bottomHeight, 
                                  Room room, WallItemType itemType) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.bottomHeight = bottomHeight;
        this.room = room;
        this.itemType = itemType;
    }
    
    public WallItemWallPlacementBuilder onWall(Wall wall) {
        return new WallItemWallPlacementBuilder(wall);
    }
    
    public final class WallItemWallPlacementBuilder {
        private final Wall wall;
        private WallPlacementStrategy.WallPosition position = WallPlacementStrategy.WallPosition.CENTERED;
        private double offset = 0.0;
        
        private WallItemWallPlacementBuilder(Wall wall) {
            this.wall = wall;
        }
        
        public WallItemWallPlacementBuilder centered() {
            this.position = WallPlacementStrategy.WallPosition.CENTERED;
            return this;
        }
        
        public WallItemWallPlacementBuilder fromNorth(double distance) {
            this.position = WallPlacementStrategy.WallPosition.FROM_START;
            this.offset = distance;
            return this;
        }
        
        public WallItemWallPlacementBuilder fromSouth(double distance) {
            this.position = WallPlacementStrategy.WallPosition.FROM_END;
            this.offset = distance;
            return this;
        }
        
        public WallItemWallPlacementBuilder fromEast(double distance) {
            this.position = WallPlacementStrategy.WallPosition.FROM_END;
            this.offset = distance;
            return this;
        }
        
        public WallItemWallPlacementBuilder fromWest(double distance) {
            this.position = WallPlacementStrategy.WallPosition.FROM_START;
            this.offset = distance;
            return this;
        }
        
        public WallItemWallPlacementBuilder shiftNorth(double distance) {
            this.offset -= distance;
            return this;
        }
        
        public WallItemWallPlacementBuilder shiftSouth(double distance) {
            this.offset += distance;
            return this;
        }
        
        public WallItemWallPlacementBuilder shiftEast(double distance) {
            this.offset += distance;
            return this;
        }
        
        public WallItemWallPlacementBuilder shiftWest(double distance) {
            this.offset -= distance;
            return this;
        }
        
        public WallItem build() {
            double wallLength = (wall == Wall.NORTH || wall == Wall.SOUTH) ? room.getWidth() : room.getLength();
            double calculatedPosition = calculateWallPosition(wallLength);
            
            // Validate position
            if (calculatedPosition < 0 || calculatedPosition + width > wallLength) {
                throw new IllegalArgumentException("Wall item doesn't fit on " + wall + " wall at calculated position");
            }
            
            if (bottomHeight < 0 || bottomHeight + height > room.getHeight()) {
                throw new IllegalArgumentException("Wall item doesn't fit vertically on wall");
            }
            
            WallItem item;
            if (itemType == WallItemType.DOOR) {
                item = new Door(wall, calculatedPosition, width, height, name);
            } else {
                item = new Window(wall, calculatedPosition, width, height, bottomHeight);
            }
            
            // Add to room
            if (itemType == WallItemType.DOOR) {
                room.addDoor((Door) item);
            } else {
                room.addWindow((Window) item);
            }
            
            return item;
        }
        
        private double calculateWallPosition(double wallLength) {
            switch (position) {
                case CENTERED:
                    return (wallLength - width) / 2.0 + offset;
                case FROM_START:
                    return offset;
                case FROM_END:
                    return wallLength - width - offset;
                default:
                    throw new IllegalArgumentException("Unknown wall position: " + position);
            }
        }
    }
}