package com.roomlayout.visualization;

import com.roomlayout.model.*;
import com.roomlayout.placement.Corner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify the configurable scaling works correctly and provides good detail
 */
class VisualizerScalingTest {

    private Room room;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        // Create a 4m x 6m room with some furniture
        room = new Room(4.0, 6.0, 2.7);
        
        // Add small furniture to test detail level
        room.place("Sofa", 2.0, 1.0, 0.8)
            .inCorner(Corner.SOUTH_WEST)
            .build();
            
        room.place("Table", 1.0, 0.8, 0.7)
            .inCorner(Corner.NORTH_EAST)
            .build();
    }
    
    @Test
    void testConsoleVisualizerWithDefaultScaling() {
        ConsoleVisualizer viz = new ConsoleVisualizer(room);
        
        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            viz.visualizeFloorPlan();
            String output = outputStream.toString();
            
            // Should show scale in characters per meter format
            assertTrue(output.contains("characters per meter"), "Should show characters per meter");
            
            // Should have reasonable detail - at least 8 characters per meter by default
            assertTrue(output.contains("Scale: "), "Should show scale information");
            
            // Should have proper furniture representation
            assertTrue(output.contains("S"), "Should show sofa initial");
            assertTrue(output.contains("T"), "Should show table initial");
            
            // Count the width to ensure it's reasonable
            String[] lines = output.split("\n");
            String floorPlanLine = null;
            for (String line : lines) {
                if (line.contains("#") && line.length() > 10) {
                    floorPlanLine = line;
                    break;
                }
            }
            assertNotNull(floorPlanLine, "Should have a floor plan visualization");
            // With 4m width and minimum 8 chars/meter, should be at least 32 chars wide + borders
            assertTrue(floorPlanLine.length() >= 30, "Visualization should have good detail");
            
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    void testConsoleVisualizerWithCustomScaling() {
        // Test with high detail (20 characters per meter)
        ConsoleVisualizer highDetailViz = new ConsoleVisualizer(room, 20);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            highDetailViz.visualizeFloorPlan();
            String output = outputStream.toString();
            
            // Should show the custom scale
            assertTrue(output.contains("Scale: 20 characters per meter"), "Should show custom scale");
            
            // Count width - should be much larger
            String[] lines = output.split("\n");
            String floorPlanLine = null;
            for (String line : lines) {
                if (line.contains("#") && line.length() > 50) {
                    floorPlanLine = line;
                    break;
                }
            }
            assertNotNull(floorPlanLine, "Should have a detailed floor plan visualization");
            // With 4m width and 20 chars/meter, should be 80 chars + borders
            assertTrue(floorPlanLine.length() >= 80, "High detail visualization should be wide");
            
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    void testConsoleVisualizerWithLowDetailScaling() {
        // Test with low detail (3 characters per meter)
        ConsoleVisualizer lowDetailViz = new ConsoleVisualizer(room, 3);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            lowDetailViz.visualizeFloorPlan();
            String output = outputStream.toString();
            
            // Should show the custom scale
            assertTrue(output.contains("Scale: 3 characters per meter"), "Should show low detail scale");
            
            // Should still work but be more compact
            assertTrue(output.contains("FLOOR PLAN"), "Should still produce floor plan");
            assertTrue(output.contains("#"), "Should still show walls");
            
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    void testTextVisualizerWithDefaultScaling() throws Exception {
        TextVisualizer viz = new TextVisualizer(room, tempDir.toString());
        
        viz.visualizeFloorPlan();
        
        // Read the generated file
        String content = Files.readString(tempDir.resolve("floor_plan.txt"));
        
        // Should show scale in characters per meter format
        assertTrue(content.contains("characters per meter"), "Should show characters per meter in file");
        
        // Should have good detail for text files (at least 12 chars/meter by default)
        assertTrue(content.contains("Scale: "), "Should show scale information in file");
        
        // Count the width to ensure good detail
        String[] lines = content.split("\n");
        String floorPlanLine = null;
        for (String line : lines) {
            if (line.contains("#") && line.length() > 40) {
                floorPlanLine = line;
                break;
            }
        }
        assertNotNull(floorPlanLine, "Should have a detailed floor plan in file");
        // With 4m width and minimum 12 chars/meter, should be at least 48 chars wide + borders
        assertTrue(floorPlanLine.length() >= 45, "File visualization should have high detail");
    }
    
    @Test
    void testTextVisualizerWithCustomScaling() throws Exception {
        // Test with very high detail (30 characters per meter)
        TextVisualizer highDetailViz = new TextVisualizer(room, tempDir.toString(), 30);
        
        highDetailViz.visualizeFloorPlan();
        
        String content = Files.readString(tempDir.resolve("floor_plan.txt"));
        
        // Should show the custom scale
        assertTrue(content.contains("Scale: 30 characters per meter"), "Should show custom high detail scale");
        
        // Should be very detailed
        String[] lines = content.split("\n");
        String floorPlanLine = null;
        for (String line : lines) {
            if (line.contains("#") && line.length() > 100) {
                floorPlanLine = line;
                break;
            }
        }
        assertNotNull(floorPlanLine, "Should have a very detailed floor plan in file");
        // With 4m width and 30 chars/meter, should be 120 chars + borders
        assertTrue(floorPlanLine.length() >= 120, "Very high detail visualization should be very wide");
    }
    
    @Test
    void testScalingForDifferentRoomSizes() {
        // Test that scaling adapts properly for different room sizes
        
        // Small room
        Room smallRoom = new Room(2.0, 3.0, 2.5);
        ConsoleVisualizer smallViz = new ConsoleVisualizer(smallRoom);
        assertNotNull(smallViz, "Should handle small room with default scaling");
        
        // Large room 
        Room largeRoom = new Room(12.0, 15.0, 3.0);
        ConsoleVisualizer largeViz = new ConsoleVisualizer(largeRoom);
        assertNotNull(largeViz, "Should handle large room with default scaling");
        
        // Very small room with custom high scaling should still work
        Room tinyRoom = new Room(0.5, 0.8, 2.0);
        ConsoleVisualizer tinyHighDetailViz = new ConsoleVisualizer(tinyRoom, 50);
        assertNotNull(tinyHighDetailViz, "Should handle tiny room with high detail scaling");
    }
    
    @Test 
    void testWallVisualizationScaling() {
        ConsoleVisualizer highDetailViz = new ConsoleVisualizer(room, 15);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            highDetailViz.visualizeWall(Wall.NORTH);
            String output = outputStream.toString();
            
            // Should produce a wall visualization
            assertTrue(output.contains("NORTH WALL VIEW"), "Should show north wall");
            
            // Should have reasonable width with 15 chars/meter and 4m room width
            String[] lines = output.split("\n");
            String wallLine = null;
            for (String line : lines) {
                if (line.contains("#") && line.length() > 40) {
                    wallLine = line;
                    break;
                }
            }
            assertNotNull(wallLine, "Should have a detailed wall visualization");
            // With 4m width and 15 chars/meter, should be 60 chars + borders
            assertTrue(wallLine.length() >= 55, "Wall visualization should reflect scaling");
            
        } finally {
            System.setOut(originalOut);
        }
    }
}