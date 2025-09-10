package com.roomlayout.model;

import com.roomlayout.placement.FurniturePlacementBuilder;
import com.roomlayout.placement.WallItemPlacementBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a room with natural furniture and wall item placement capabilities.
 * 
 * <p>The Room class is the main entry point for the Room Layout Designer library.
 * It provides intuitive methods for placing furniture, doors, and windows using
 * natural language concepts like corners, walls, and relative positioning.
 * 
 * <p>Coordinate system:
 * <ul>
 * <li>Origin (0,0) is at the North-West corner</li>
 * <li>X-axis increases eastward (positive X = east)</li>
 * <li>Y-axis increases southward (positive Y = south)</li>
 * <li>All dimensions are in meters</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * // Create a 6m x 4m room with 2.7m height
 * Room livingRoom = new Room(6.0, 4.0, 2.7);
 * 
 * // Place furniture naturally
 * Furniture sofa = livingRoom.place("Sofa", 2.0, 0.8, 0.8)
 *     .inCorner(Corner.SOUTH_WEST)
 *     .withGap(0.2)
 *     .shiftEast(0.3)
 *     .build();
 * 
 * // Add doors and windows
 * Door mainDoor = livingRoom.placeDoor("Main Door", 0.9, 2.1)
 *     .onWall(Wall.NORTH)
 *     .centered()
 *     .build();
 * 
 * Window bayWindow = livingRoom.placeWindow("Bay Window", 2.4, 1.4, 0.8)
 *     .onWall(Wall.EAST)
 *     .centered()
 *     .build();
 * }</pre>
 * 
 * @see FurniturePlacementBuilder
 * @see WallItemPlacementBuilder
 * @since 1.0.0
 */
public class Room {
    private final double width;
    private final double length;
    private final double height;
    private final List<Furniture> furniture;
    private final List<Door> doors;
    private final List<Window> windows;
    
    /**
     * Creates a new room with specified dimensions.
     * 
     * @param width the width of the room in meters (east-west dimension)
     * @param length the length of the room in meters (north-south dimension)
     * @param height the height of the room in meters (floor to ceiling)
     * @throws IllegalArgumentException if any dimension is not positive
     */
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
    
    /**
     * Starts natural furniture placement using fluent API.
     * 
     * <p>This is the main entry point for placing furniture in the room. Returns a builder
     * that allows chaining placement methods like {@code .inCorner()}, {@code .onWall()}, 
     * {@code .nextTo()}, or {@code .inCenter()}.
     * 
     * <p>Example:
     * <pre>{@code
     * Furniture sofa = room.place("Sofa", 2.0, 0.8, 0.8)
     *     .inCorner(Corner.SOUTH_WEST)
     *     .withGap(0.2)
     *     .shiftEast(0.3)
     *     .build();
     * }</pre>
     * 
     * @param name the display name of the furniture
     * @param width the width of the furniture in meters
     * @param length the length (depth) of the furniture in meters
     * @param height the height of the furniture in meters
     * @return a furniture placement builder for method chaining
     * @see FurniturePlacementBuilder
     */
    public FurniturePlacementBuilder place(String name, double width, double length, double height) {
        return new FurniturePlacementBuilder(name, width, length, height, this);
    }
    
    /**
     * Places furniture at the room center (convenience method).
     * 
     * <p>Equivalent to {@code place(name, width, length, height).inCenter().build()}.
     * 
     * @param name the display name of the furniture
     * @param width the width of the furniture in meters
     * @param length the length (depth) of the furniture in meters
     * @param height the height of the furniture in meters
     * @return the created and positioned furniture
     */
    public Furniture placeAndAdd(String name, double width, double length, double height) {
        FurniturePlacementBuilder builder = place(name, width, length, height);
        return builder.inCenter().build();
    }
    
    /**
     * Starts natural door placement using fluent API.
     * 
     * <p>Example:
     * <pre>{@code
     * Door mainDoor = room.placeDoor("Main Entry", 0.9, 2.1)
     *     .onWall(Wall.NORTH)
     *     .centered()
     *     .build();
     * }</pre>
     * 
     * @param name the display name of the door
     * @param width the width of the door opening in meters
     * @param height the height of the door opening in meters
     * @return a wall item placement builder for method chaining
     * @see WallItemPlacementBuilder
     */
    public WallItemPlacementBuilder placeDoor(String name, double width, double height) {
        return new WallItemPlacementBuilder(name, width, height, 0.0, this, WallItemPlacementBuilder.WallItemType.DOOR);
    }
    
    /**
     * Starts natural window placement using fluent API.
     * 
     * <p>Example:
     * <pre>{@code
     * Window bayWindow = room.placeWindow("Bay Window", 2.4, 1.4, 0.8)
     *     .onWall(Wall.EAST)
     *     .centered()
     *     .build();
     * }</pre>
     * 
     * @param name the display name of the window
     * @param width the width of the window in meters
     * @param height the height of the window in meters
     * @param bottomHeight the distance from floor to window bottom in meters
     * @return a wall item placement builder for method chaining
     * @see WallItemPlacementBuilder
     */
    public WallItemPlacementBuilder placeWindow(String name, double width, double height, double bottomHeight) {
        return new WallItemPlacementBuilder(name, width, height, bottomHeight, this, WallItemPlacementBuilder.WallItemType.WINDOW);
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