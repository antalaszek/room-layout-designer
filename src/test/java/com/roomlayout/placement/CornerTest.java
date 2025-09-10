package com.roomlayout.placement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CornerTest {
    
    @Test
    void testCornerConstants() {
        assertNotNull(Corner.NORTH_EAST);
        assertNotNull(Corner.NORTH_WEST);
        assertNotNull(Corner.SOUTH_EAST);
        assertNotNull(Corner.SOUTH_WEST);
    }
    
    @Test
    void testCornerEquality() {
        assertEquals(Corner.NORTH_EAST, Corner.NORTH_EAST);
        assertNotEquals(Corner.NORTH_EAST, Corner.NORTH_WEST);
        assertNotEquals(Corner.NORTH_EAST, null);
        assertNotEquals(Corner.NORTH_EAST, "not a corner");
    }
    
    @Test
    void testCornerHashCode() {
        assertEquals(Corner.NORTH_EAST.hashCode(), Corner.NORTH_EAST.hashCode());
        assertNotEquals(Corner.NORTH_EAST.hashCode(), Corner.SOUTH_WEST.hashCode());
    }
    
    @Test
    void testCornerToString() {
        assertEquals("NORTH_EAST", Corner.NORTH_EAST.toString());
        assertEquals("SOUTH_WEST", Corner.SOUTH_WEST.toString());
    }
}