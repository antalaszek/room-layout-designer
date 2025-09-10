package com.roomlayout.placement;

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
    public boolean equals(Object obj) {
        return this == obj;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}