package com.roomlayout.placement;

import com.roomlayout.model.Point2D;
import com.roomlayout.model.Furniture;

public final class RelativePlacementStrategy implements PlacementStrategy {
    private final Furniture referenceFurniture;
    private final Side side;
    private final Gap gap;
    
    public RelativePlacementStrategy(Furniture referenceFurniture, Side side, Gap gap) {
        this.referenceFurniture = referenceFurniture;
        this.side = side;
        this.gap = gap;
    }
    
    public RelativePlacementStrategy(Furniture referenceFurniture, Side side) {
        this(referenceFurniture, side, Gap.NO_GAP);
    }
    
    @Override
    public Point2D calculatePosition(PlacementContext context) {
        Furniture furniture = context.getFurniture();
        double gapValue = gap.getValue();
        
        double x, y;
        
        switch (side) {
            case NORTH:
                x = referenceFurniture.getX();
                y = referenceFurniture.getY() - furniture.getLength() - gapValue;
                break;
            case SOUTH:
                x = referenceFurniture.getX();
                y = referenceFurniture.getY() + referenceFurniture.getLength() + gapValue;
                break;
            case EAST:
                x = referenceFurniture.getX() + referenceFurniture.getWidth() + gapValue;
                y = referenceFurniture.getY();
                break;
            case WEST:
                x = referenceFurniture.getX() - furniture.getWidth() - gapValue;
                y = referenceFurniture.getY();
                break;
            default:
                throw new IllegalArgumentException("Unknown side: " + side);
        }
        
        return new Point2D(x, y);
    }
}