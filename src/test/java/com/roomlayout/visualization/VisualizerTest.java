package com.roomlayout.visualization;

import com.roomlayout.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class VisualizerTest {
    private Room room;
    
    @BeforeEach
    void setUp() {
        room = new Room(4.0, 5.0, 3.0);
        room.addFurniture(new Furniture("Table", 1.0, 1.0, 0.8, 1.0, 1.0));
        room.addFurniture(new Furniture("Chair", 0.5, 0.5, 0.9, 2.0, 2.0));
        room.addDoor(new Door(Wall.NORTH, 1.0, 1.0, 2.0));
        room.addWindow(new Window(Wall.EAST, 1.0, 1.5, 1.0, 1.0));
    }
    
    @Test
    @DisplayName("ConsoleVisualizer should implement Visualizer interface")
    void testConsoleVisualizerImplementsInterface() {
        Visualizer visualizer = new ConsoleVisualizer(room);
        assertNotNull(visualizer);
        assertTrue(visualizer instanceof Visualizer);
    }
    
    @Test
    @DisplayName("ImageVisualizer should implement Visualizer interface")
    void testImageVisualizerImplementsInterface(@TempDir Path tempDir) {
        Visualizer visualizer = new ImageVisualizer(room, tempDir.toString());
        assertNotNull(visualizer);
        assertTrue(visualizer instanceof Visualizer);
    }
    
    @Test
    @DisplayName("ConsoleVisualizer should produce output")
    void testConsoleVisualizerOutput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        
        try {
            System.setOut(new PrintStream(outputStream));
            
            ConsoleVisualizer visualizer = new ConsoleVisualizer(room);
            visualizer.visualizeFloorPlan();
            
            String output = outputStream.toString();
            assertFalse(output.isEmpty());
            assertTrue(output.contains("FLOOR PLAN"));
            assertTrue(output.contains("Table"));
            assertTrue(output.contains("Chair"));
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    @DisplayName("ConsoleVisualizer should visualize all views")
    void testConsoleVisualizerAllViews() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        
        try {
            System.setOut(new PrintStream(outputStream));
            
            ConsoleVisualizer visualizer = new ConsoleVisualizer(room);
            visualizer.visualizeAll();
            
            String output = outputStream.toString();
            assertTrue(output.contains("FLOOR PLAN"));
            assertTrue(output.contains("NORTH WALL"));
            assertTrue(output.contains("SOUTH WALL"));
            assertTrue(output.contains("EAST WALL"));
            assertTrue(output.contains("WEST WALL"));
            assertTrue(output.contains("CEILING"));
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    @DisplayName("ImageVisualizer should create output directory")
    void testImageVisualizerCreatesDirectory(@TempDir Path tempDir) {
        String outputDir = tempDir.resolve("test-output").toString();
        ImageVisualizer visualizer = new ImageVisualizer(room, outputDir);
        
        File dir = new File(outputDir);
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
    }
    
    @Test
    @DisplayName("ImageVisualizer should create image files")
    void testImageVisualizerCreatesFiles(@TempDir Path tempDir) {
        String outputDir = tempDir.toString();
        ImageVisualizer visualizer = new ImageVisualizer(room, outputDir);
        
        visualizer.visualizeFloorPlan();
        File floorPlan = new File(outputDir, "floor_plan.png");
        assertTrue(floorPlan.exists());
        assertTrue(floorPlan.length() > 0);
        
        visualizer.visualizeWall(Wall.NORTH);
        File northWall = new File(outputDir, "north_wall.png");
        assertTrue(northWall.exists());
        assertTrue(northWall.length() > 0);
        
        visualizer.visualizeCeiling();
        File ceiling = new File(outputDir, "ceiling.png");
        assertTrue(ceiling.exists());
        assertTrue(ceiling.length() > 0);
    }
    
    @Test
    @DisplayName("ImageVisualizer should handle all walls")
    void testImageVisualizerAllWalls(@TempDir Path tempDir) {
        String outputDir = tempDir.toString();
        ImageVisualizer visualizer = new ImageVisualizer(room, outputDir);
        
        visualizer.visualizeWall(Wall.NORTH);
        visualizer.visualizeWall(Wall.SOUTH);
        visualizer.visualizeWall(Wall.EAST);
        visualizer.visualizeWall(Wall.WEST);
        
        assertTrue(new File(outputDir, "north_wall.png").exists());
        assertTrue(new File(outputDir, "south_wall.png").exists());
        assertTrue(new File(outputDir, "east_wall.png").exists());
        assertTrue(new File(outputDir, "west_wall.png").exists());
    }
    
    @Test
    @DisplayName("ImageVisualizer should skip floor and ceiling in wall views")
    void testImageVisualizerSkipsFloorCeilingWalls(@TempDir Path tempDir) {
        String outputDir = tempDir.toString();
        ImageVisualizer visualizer = new ImageVisualizer(room, outputDir);
        
        visualizer.visualizeWall(Wall.FLOOR);
        visualizer.visualizeWall(Wall.CEILING);
        
        assertFalse(new File(outputDir, "floor_wall.png").exists());
        assertFalse(new File(outputDir, "ceiling_wall.png").exists());
    }
    
    @Test
    @DisplayName("Visualizers should handle empty room")
    void testVisualizersHandleEmptyRoom(@TempDir Path tempDir) {
        Room emptyRoom = new Room(3.0, 3.0, 2.5);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        
        try {
            System.setOut(new PrintStream(outputStream));
            
            ConsoleVisualizer consoleVis = new ConsoleVisualizer(emptyRoom);
            consoleVis.visualizeAll();
            
            String output = outputStream.toString();
            assertFalse(output.isEmpty());
            
            ImageVisualizer imageVis = new ImageVisualizer(emptyRoom, tempDir.toString());
            imageVis.visualizeAll();
            
            assertTrue(new File(tempDir.toFile(), "floor_plan.png").exists());
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    @DisplayName("Visualizers should handle room with tall furniture")
    void testVisualizersHandleTallFurniture(@TempDir Path tempDir) {
        Room tallRoom = new Room(5.0, 5.0, 3.0);
        tallRoom.addFurniture(new Furniture("Bookshelf", 1.0, 0.3, 2.5, 1.0, 1.0));
        tallRoom.addFurniture(new Furniture("Table", 1.0, 1.0, 0.5, 3.0, 3.0));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        
        try {
            System.setOut(new PrintStream(outputStream));
            
            ConsoleVisualizer consoleVis = new ConsoleVisualizer(tallRoom);
            consoleVis.visualizeCeiling();
            
            String output = outputStream.toString();
            assertTrue(output.contains("*"));
            
            ImageVisualizer imageVis = new ImageVisualizer(tallRoom, tempDir.toString());
            imageVis.visualizeCeiling();
            
            File ceiling = new File(tempDir.toFile(), "ceiling.png");
            assertTrue(ceiling.exists());
        } finally {
            System.setOut(originalOut);
        }
    }
}