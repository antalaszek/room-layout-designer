package com.roomlayout.placement;

import com.roomlayout.model.Point2D;
import com.roomlayout.model.Room;
import com.roomlayout.model.Furniture;
import com.roomlayout.model.Wall;

public final class WallPlacementStrategy implements PlacementStrategy {
    private final Wall wall;
    private final Gap gap;
    private final WallPosition position;
    private final double offset;
    
    public WallPlacementStrategy(Wall wall, WallPosition position, Gap gap, double offset) {
        this.wall = wall;
        this.position = position;
        this.gap = gap;
        this.offset = offset;
    }
    
    public WallPlacementStrategy(Wall wall, WallPosition position) {
        this(wall, position, Gap.NO_GAP, 0.0);
    }
    
    @Override
    public Point2D calculatePosition(PlacementContext context) {
        Room room = context.getRoom();
        Furniture furniture = context.getFurniture();
        double gapValue = gap.getValue();
        
        double x, y;
        
        switch (wall) {
            case NORTH:
                y = gapValue;
                x = calculateWallPosition(room.getWidth(), furniture.getWidth());
                break;
            case SOUTH:
                y = room.getLength() - furniture.getLength() - gapValue;
                x = calculateWallPosition(room.getWidth(), furniture.getWidth());
                break;
            case EAST:
                x = room.getWidth() - furniture.getWidth() - gapValue;
                y = calculateWallPosition(room.getLength(), furniture.getLength());
                break;
            case WEST:
                x = gapValue;
                y = calculateWallPosition(room.getLength(), furniture.getLength());
                break;
            default:
                throw new IllegalArgumentException("Invalid wall: " + wall);
        }
        
        return new Point2D(x, y);
    }
    
    private double calculateWallPosition(double wallLength, double furnitureSize) {
        switch (position) {
            case CENTERED:
                return (wallLength - furnitureSize) / 2.0 + offset;
            case FROM_START:
                return offset;
            case FROM_END:
                return wallLength - furnitureSize - offset;
            default:
                throw new IllegalArgumentException("Unknown wall position: " + position);
        }
    }
    
    public enum WallPosition {
        CENTERED, FROM_START, FROM_END
    }
}