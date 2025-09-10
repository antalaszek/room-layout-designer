package com.roomlayout.placement;

import com.roomlayout.model.Point2D;
import com.roomlayout.model.Furniture;
import com.roomlayout.model.Room;

public final class PositionResolver {
    
    public static Point2D resolve(PlacementStrategy strategy, Room room, Furniture furniture) {
        PlacementContext context = new PlacementContext(room, furniture);
        Point2D position = strategy.calculatePosition(context);
        
        validatePosition(position, furniture, room);
        return position;
    }
    
    private static void validatePosition(Point2D position, Furniture furniture, Room room) {
        if (position.getX() < 0 || position.getY() < 0) {
            throw new IllegalArgumentException("Furniture position cannot be negative");
        }
        
        if (position.getX() + furniture.getWidth() > room.getWidth() ||
            position.getY() + furniture.getLength() > room.getLength()) {
            throw new IllegalArgumentException("Furniture doesn't fit in the room at calculated position");
        }
    }
    
    public static Furniture createFurnitureAt(String name, double width, double length, double height,
                                            PlacementStrategy strategy, Room room) {
        Furniture tempFurniture = new Furniture(name, width, length, height, 0, 0);
        Point2D position = resolve(strategy, room, tempFurniture);
        return new Furniture(name, width, length, height, position.getX(), position.getY());
    }
}