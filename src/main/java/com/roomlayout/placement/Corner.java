package com.roomlayout.placement;

/**
 * Represents the four corners of a rectangular room.
 * 
 * <p>Corner constants provide type-safe references for furniture placement
 * in room corners. The naming follows compass directions:
 * <ul>
 * <li>{@link #NORTH_WEST} - Top-left corner (0,0)</li>
 * <li>{@link #NORTH_EAST} - Top-right corner</li>
 * <li>{@link #SOUTH_WEST} - Bottom-left corner</li>  
 * <li>{@link #SOUTH_EAST} - Bottom-right corner</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * // Place sofa in southwest corner with 20cm gap and 30cm eastward shift
 * Furniture sofa = room.place("Sofa", 2.0, 0.8, 0.8)
 *     .inCorner(Corner.SOUTH_WEST)
 *     .withGap(0.2)
 *     .shiftEast(0.3)
 *     .build();
 * }</pre>
 * 
 * @see FurniturePlacementBuilder#inCorner(Corner)
 * @since 1.0.0
 */
public final class Corner {
    public static final Corner NORTH_EAST = new Corner("NORTH_EAST");
    public static final Corner NORTH_WEST = new Corner("NORTH_WEST");
    public static final Corner SOUTH_EAST = new Corner("SOUTH_EAST");
    public static final Corner SOUTH_WEST = new Corner("SOUTH_WEST");
    
    private final String name;
    
    private Corner(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}