package com.roomlayout.placement;

import com.roomlayout.model.Furniture;
import com.roomlayout.model.Room;

public final class PlacementContext {
    private final Room room;
    private final Furniture furniture;
    
    public PlacementContext(Room room, Furniture furniture) {
        this.room = room;
        this.furniture = furniture;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public Furniture getFurniture() {
        return furniture;
    }
}