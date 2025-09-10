package com.roomlayout.placement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GapTest {
    
    @Test
    void testNoGapConstant() {
        assertEquals(0.0, Gap.NO_GAP.getValue());
    }
    
    @Test
    void testGapCreation() {
        Gap gap = Gap.of(1.5);
        assertEquals(1.5, gap.getValue());
    }
    
    @Test
    void testGapOfZeroReturnsNoGap() {
        assertSame(Gap.NO_GAP, Gap.of(0.0));
    }
    
    @Test
    void testNegativeGapThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Gap.of(-1.0));
        assertThrows(IllegalArgumentException.class, () -> Gap.of(-0.1));
    }
    
    @Test
    void testGapEquality() {
        Gap gap1 = Gap.of(1.0);
        Gap gap2 = Gap.of(1.0);
        Gap gap3 = Gap.of(2.0);
        
        assertEquals(gap1, gap2);
        assertNotEquals(gap1, gap3);
        assertEquals(Gap.NO_GAP, Gap.of(0.0));
    }
    
    @Test
    void testGapHashCode() {
        Gap gap1 = Gap.of(1.0);
        Gap gap2 = Gap.of(1.0);
        
        assertEquals(gap1.hashCode(), gap2.hashCode());
    }
    
    @Test
    void testGapToString() {
        Gap gap = Gap.of(1.5);
        assertTrue(gap.toString().contains("1.50"));
        assertTrue(gap.toString().contains("Gap"));
    }
}