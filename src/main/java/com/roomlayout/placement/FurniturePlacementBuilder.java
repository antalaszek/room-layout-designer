package com.roomlayout.placement;

import com.roomlayout.model.Furniture;
import com.roomlayout.model.Room;
import com.roomlayout.model.Wall;

/**
 * Builder for natural furniture placement using fluent API.
 * 
 * <p>This class provides a human-readable way to place furniture in rooms using 
 * natural language concepts like corners, walls, relative positioning, and center placement.
 * 
 * <p>Example usage:
 * <pre>{@code
 * Room room = new Room(6.0, 4.0, 2.7);
 * 
 * // Place sofa in corner with gap
 * Furniture sofa = room.place("Sofa", 2.0, 0.8, 0.8)
 *     .inCorner(Corner.SOUTH_WEST)
 *     .withGap(0.2)
 *     .shiftEast(0.3)
 *     .build();
 * 
 * // Place TV on wall
 * Furniture tv = room.place("TV", 1.5, 0.3, 0.6)
 *     .onWall(Wall.NORTH)
 *     .centered()
 *     .build();
 * 
 * // Place coffee table relative to sofa
 * Furniture table = room.place("Coffee Table", 1.0, 0.6, 0.4)
 *     .nextTo(sofa)
 *     .onSide(Side.NORTH)
 *     .withGap(0.4)
 *     .build();
 * }</pre>
 * 
 * @see Room#place(String, double, double, double)
 * @see Corner
 * @see Wall
 * @see Side
 * @since 1.0.0
 */
public final class FurniturePlacementBuilder {
    private final String name;
    private final double width;
    private final double length;
    private final double height;
    private final Room room;
    
    /**
     * Creates a new furniture placement builder.
     *
     * @param name the display name of the furniture piece
     * @param width the width of the furniture in meters
     * @param length the length (depth) of the furniture in meters  
     * @param height the height of the furniture in meters
     * @param room the room where the furniture will be placed
     */
    public FurniturePlacementBuilder(String name, double width, double length, double height, Room room) {
        this.name = name;
        this.width = width;
        this.length = length;
        this.height = height;
        this.room = room;
    }
    
    /**
     * Places furniture in a room corner.
     * 
     * <p>Positions the furniture in one of the four room corners. By default, the furniture
     * is placed directly in the corner. Use {@code .withGap()} to add space from the walls,
     * or {@code .shiftEast()}, {@code .shiftWest()}, etc. for directional adjustments.
     * 
     * @param corner the target corner (NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST)
     * @return a corner placement builder for further configuration
     * @see Corner
     */
    public CornerPlacementBuilder inCorner(Corner corner) {
        return new CornerPlacementBuilder(corner);
    }
    
    /**
     * Places furniture against a room wall.
     * 
     * <p>Positions the furniture against one of the room walls. Use {@code .centered()} to
     * center on the wall, {@code .fromNorth()}, {@code .fromSouth()}, etc. to position at
     * specific distances from wall edges, or {@code .shift*()} methods for fine adjustments.
     * 
     * @param wall the target wall (NORTH, SOUTH, EAST, WEST)
     * @return a wall placement builder for further configuration
     * @see Wall
     */
    public WallPlacementBuilder onWall(Wall wall) {
        return new WallPlacementBuilder(wall);
    }
    
    /**
     * Places furniture relative to another furniture piece.
     * 
     * <p>Positions the furniture next to an existing piece. Use {@code .onSide()} to specify
     * which side (NORTH, SOUTH, EAST, WEST) and {@code .withGap()} to control spacing.
     * 
     * @param referenceFurniture the existing furniture to position relative to
     * @return a relative placement builder for further configuration
     * @see Side
     */
    public RelativePlacementBuilder nextTo(Furniture referenceFurniture) {
        return new RelativePlacementBuilder(referenceFurniture);
    }
    
    /**
     * Places furniture in the center of the room.
     * 
     * <p>Positions the furniture at the geometric center of the room. Use 
     * {@code .shift*()} methods to offset from the exact center.
     * 
     * @return a center placement builder for further configuration
     */
    public CenterPlacementBuilder inCenter() {
        return new CenterPlacementBuilder();
    }
    
    /**
     * Builder for corner placement with gap and directional shift options.
     * 
     * <p>Provides methods to fine-tune furniture placement in room corners:
     * <ul>
     * <li>{@code .withGap()} - adds uniform distance from corner walls</li>
     * <li>{@code .shiftEast()}, {@code .shiftWest()}, {@code .shiftNorth()}, {@code .shiftSouth()} - directional movement from corner position</li>
     * </ul>
     * 
     * <p>Example: "Place sofa in south-west corner, 20cm from walls, then shift 30cm east"
     * <pre>{@code
     * furniture.inCorner(Corner.SOUTH_WEST)
     *     .withGap(0.2)
     *     .shiftEast(0.3)
     *     .build();
     * }</pre>
     */
    public final class CornerPlacementBuilder {
        private final Corner corner;
        private Gap gap = Gap.NO_GAP;
        private double shiftX = 0.0;
        private double shiftY = 0.0;
        
        private CornerPlacementBuilder(Corner corner) {
            this.corner = corner;
        }
        
        /**
         * Adds a uniform gap from corner walls.
         * 
         * @param gapValue the distance in meters to maintain from both walls
         * @return this builder for method chaining
         */
        public CornerPlacementBuilder withGap(double gapValue) {
            this.gap = Gap.of(gapValue);
            return this;
        }
        
        /**
         * Shifts the furniture north (towards negative Y).
         * 
         * @param distance the distance in meters to shift northward
         * @return this builder for method chaining
         */
        public CornerPlacementBuilder shiftNorth(double distance) {
            this.shiftY -= distance;
            return this;
        }
        
        /**
         * Shifts the furniture south (towards positive Y).
         * 
         * @param distance the distance in meters to shift southward
         * @return this builder for method chaining
         */
        public CornerPlacementBuilder shiftSouth(double distance) {
            this.shiftY += distance;
            return this;
        }
        
        /**
         * Shifts the furniture east (towards positive X).
         * 
         * @param distance the distance in meters to shift eastward
         * @return this builder for method chaining
         */
        public CornerPlacementBuilder shiftEast(double distance) {
            this.shiftX += distance;
            return this;
        }
        
        /**
         * Shifts the furniture west (towards negative X).
         * 
         * @param distance the distance in meters to shift westward
         * @return this builder for method chaining
         */
        public CornerPlacementBuilder shiftWest(double distance) {
            this.shiftX -= distance;
            return this;
        }
        
        /**
         * Builds and places the furniture in the room.
         * 
         * @return the created and positioned furniture
         * @throws IllegalArgumentException if the furniture doesn't fit in the specified position
         */
        public Furniture build() {
            PlacementStrategy strategy = new CornerPlacementStrategy(corner, gap, shiftX, shiftY);
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