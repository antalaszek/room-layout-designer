package com.roomlayout.placement;

import com.roomlayout.model.Point2D;
import com.roomlayout.model.Room;
import com.roomlayout.model.Furniture;

public final class CenterPlacementStrategy implements PlacementStrategy {
    private final double xOffset;
    private final double yOffset;
    
    public CenterPlacementStrategy(double xOffset, double yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
    public CenterPlacementStrategy() {
        this(0.0, 0.0);
    }
    
    @Override
    public Point2D calculatePosition(PlacementContext context) {
        Room room = context.getRoom();
        Furniture furniture = context.getFurniture();
        
        double x = (room.getWidth() - furniture.getWidth()) / 2.0 + xOffset;
        double y = (room.getLength() - furniture.getLength()) / 2.0 + yOffset;
        
        return new Point2D(x, y);
    }
}