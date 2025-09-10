package com.roomlayout.placement;

import com.roomlayout.model.Furniture;
import com.roomlayout.model.Room;
import com.roomlayout.model.Wall;

public final class FurniturePlacementBuilder {
    private final String name;
    private final double width;
    private final double length;
    private final double height;
    private final Room room;
    
    public FurniturePlacementBuilder(String name, double width, double length, double height, Room room) {
        this.name = name;
        this.width = width;
        this.length = length;
        this.height = height;
        this.room = room;
    }
    
    public CornerPlacementBuilder inCorner(Corner corner) {
        return new CornerPlacementBuilder(corner);
    }
    
    public WallPlacementBuilder onWall(Wall wall) {
        return new WallPlacementBuilder(wall);
    }
    
    public RelativePlacementBuilder nextTo(Furniture referenceFurniture) {
        return new RelativePlacementBuilder(referenceFurniture);
    }
    
    public CenterPlacementBuilder inCenter() {
        return new CenterPlacementBuilder();
    }
    
    public final class CornerPlacementBuilder {
        private final Corner corner;
        private Gap gap = Gap.NO_GAP;
        
        private CornerPlacementBuilder(Corner corner) {
            this.corner = corner;
        }
        
        public CornerPlacementBuilder withGap(double gapValue) {
            this.gap = Gap.of(gapValue);
            return this;
        }
        
        public Furniture build() {
            PlacementStrategy strategy = new CornerPlacementStrategy(corner, gap);
            Furniture furniture = PositionResolver.createFurnitureAt(name, width, length, height, strategy, room);
            room.addFurniture(furniture);
            return furniture;
        }
    }
    
    public final class WallPlacementBuilder {
        private final Wall wall;
        private WallPlacementStrategy.WallPosition position = WallPlacementStrategy.WallPosition.CENTERED;
        private Gap gap = Gap.NO_GAP;
        private double offset = 0.0;
        
        private WallPlacementBuilder(Wall wall) {
            this.wall = wall;
        }
        
        public WallPlacementBuilder centered() {
            this.position = WallPlacementStrategy.WallPosition.CENTERED;
            return this;
        }
        
        public WallPlacementBuilder fromNorth(double distance) {
            this.position = WallPlacementStrategy.WallPosition.FROM_START;
            this.offset = distance;
            return this;
        }
        
        public WallPlacementBuilder fromSouth(double distance) {
            this.position = WallPlacementStrategy.WallPosition.FROM_END;
            this.offset = distance;
            return this;
        }
        
        public WallPlacementBuilder fromEast(double distance) {
            this.position = WallPlacementStrategy.WallPosition.FROM_END;
            this.offset = distance;
            return this;
        }
        
        public WallPlacementBuilder fromWest(double distance) {
            this.position = WallPlacementStrategy.WallPosition.FROM_START;
            this.offset = distance;
            return this;
        }
        
        public WallPlacementBuilder shiftNorth(double distance) {
            this.offset -= distance;
            return this;
        }
        
        public WallPlacementBuilder shiftSouth(double distance) {
            this.offset += distance;
            return this;
        }
        
        public WallPlacementBuilder shiftEast(double distance) {
            this.offset += distance;
            return this;
        }
        
        public WallPlacementBuilder shiftWest(double distance) {
            this.offset -= distance;
            return this;
        }
        
        public WallPlacementBuilder withGap(double gapValue) {
            this.gap = Gap.of(gapValue);
            return this;
        }
        
        public Furniture build() {
            PlacementStrategy strategy = new WallPlacementStrategy(wall, position, gap, offset);
            Furniture furniture = PositionResolver.createFurnitureAt(name, width, length, height, strategy, room);
            room.addFurniture(furniture);
            return furniture;
        }
    }
    
    public final class RelativePlacementBuilder {
        private final Furniture referenceFurniture;
        private Side side;
        private Gap gap = Gap.NO_GAP;
        
        private RelativePlacementBuilder(Furniture referenceFurniture) {
            this.referenceFurniture = referenceFurniture;
        }
        
        public RelativePlacementBuilder onSide(Side side) {
            this.side = side;
            return this;
        }
        
        public RelativePlacementBuilder withGap(double gapValue) {
            this.gap = Gap.of(gapValue);
            return this;
        }
        
        public Furniture build() {
            if (side == null) {
                throw new IllegalStateException("Side must be specified for relative placement");
            }
            PlacementStrategy strategy = new RelativePlacementStrategy(referenceFurniture, side, gap);
            Furniture furniture = PositionResolver.createFurnitureAt(name, width, length, height, strategy, room);
            room.addFurniture(furniture);
            return furniture;
        }
    }
    
    public final class CenterPlacementBuilder {
        private double xOffset = 0.0;
        private double yOffset = 0.0;
        
        public CenterPlacementBuilder shiftNorth(double distance) {
            this.yOffset -= distance;
            return this;
        }
        
        public CenterPlacementBuilder shiftSouth(double distance) {
            this.yOffset += distance;
            return this;
        }
        
        public CenterPlacementBuilder shiftEast(double distance) {
            this.xOffset += distance;
            return this;
        }
        
        public CenterPlacementBuilder shiftWest(double distance) {
            this.xOffset -= distance;
            return this;
        }
        
        public Furniture build() {
            PlacementStrategy strategy = new CenterPlacementStrategy(xOffset, yOffset);
            Furniture furniture = PositionResolver.createFurnitureAt(name, width, length, height, strategy, room);
            room.addFurniture(furniture);
            return furniture;
        }
    }
}