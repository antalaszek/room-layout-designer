package com.roomlayout.placement;

import com.roomlayout.model.Point2D;
import com.roomlayout.model.Room;
import com.roomlayout.model.Furniture;

public final class CornerPlacementStrategy implements PlacementStrategy {
    private final Corner corner;
    private final Gap gap;
    private final double shiftX;
    private final double shiftY;
    
    public CornerPlacementStrategy(Corner corner, Gap gap, double shiftX, double shiftY) {
        this.corner = corner;
        this.gap = gap;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }
    
    public CornerPlacementStrategy(Corner corner, Gap gap) {
        this(corner, gap, 0.0, 0.0);
    }
    
    public CornerPlacementStrategy(Corner corner) {
        this(corner, Gap.NO_GAP, 0.0, 0.0);
    }
    
    @Override
    public Point2D calculatePosition(PlacementContext context) {
        Room room = context.getRoom();
        Furniture furniture = context.getFurniture();
        double gapValue = gap.getValue();
        
        double x, y;
        
        if (corner == Corner.NORTH_WEST) {
            x = gapValue;
            y = gapValue;
        } else if (corner == Corner.NORTH_EAST) {
            x = room.getWidth() - furniture.getWidth() - gapValue;
            y = gapValue;
        } else if (corner == Corner.SOUTH_WEST) {
            x = gapValue;
            y = room.getLength() - furniture.getLength() - gapValue;
        } else if (corner == Corner.SOUTH_EAST) {
            x = room.getWidth() - furniture.getWidth() - gapValue;
            y = room.getLength() - furniture.getLength() - gapValue;
        } else {
            throw new IllegalArgumentException("Unknown corner: " + corner);
        }
        
        // Apply directional shifts
        x += shiftX;
        y += shiftY;
        
        return new Point2D(x, y);
    }
}