package com.roomlayout.visualization;

import com.roomlayout.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Text-based visualizer that can save room layouts to text files.
 * Provides the same visualization as ConsoleVisualizer but with file output capability.
 */
public class TextVisualizer implements Visualizer {
    private static final char WALL_CHAR = '#';
    private static final char EMPTY_CHAR = ' ';
    private static final char DOOR_CHAR = 'D';
    private static final char WINDOW_CHAR = 'W';
    private static final char FURNITURE_CHAR = 'F';
    
    private final Room room;
    private final int charactersPerMeter;
    private final String outputDirectory;
    private StringBuilder currentOutput;
    
    public TextVisualizer(Room room, String outputDirectory) {
        this(room, outputDirectory, calculateOptimalScale(room));
    }
    
    public TextVisualizer(Room room, String outputDirectory, int charactersPerMeter) {
        this.room = room;
        this.charactersPerMeter = Math.max(1, charactersPerMeter);
        this.outputDirectory = outputDirectory;
        this.currentOutput = new StringBuilder();
        
        // Create output directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(outputDirectory));
        } catch (IOException e) {
            System.err.println("Warning: Could not create output directory: " + e.getMessage());
        }
    }
    
    /**
     * Calculate optimal charactersPerMeter for text files - can be larger than console since files can handle more
     */
    private static int calculateOptimalScale(Room room) {
        double maxDimension = Math.max(room.getWidth(), room.getLength());
        int maxFileSize = 150; // Reasonable width for text files
        
        // Aim for good detail - at least 12 characters per meter for text files
        int minScale = 12;
        
        // Calculate charactersPerMeter that keeps the visualization under maxFileSize characters
        int maxScale = (int) Math.floor(maxFileSize / maxDimension);
        
        // Use at least minScale, but don't exceed maxScale
        return Math.max(minScale, Math.min(maxScale, 40));
    }
    
    @Override
    public void visualizeAll() {
        currentOutput = new StringBuilder();
        
        appendLine("=" .repeat(80));
        appendLine("ROOM LAYOUT VISUALIZATION");
        appendLine("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        appendLine("=" .repeat(80));
        appendLine(room.toString());
        appendLine("");
        
        visualizeFloorPlan();
        visualizeWall(Wall.NORTH);
        visualizeWall(Wall.SOUTH);
        visualizeWall(Wall.EAST);
        visualizeWall(Wall.WEST);
        visualizeCeiling();
        
        saveToFile("complete_layout.txt", currentOutput.toString());
    }
    
    @Override
    public void visualizeFloorPlan() {
        StringBuilder output = new StringBuilder();
        
        output.append("\n--- FLOOR PLAN (Top-down view) ---\n");
        output.append(String.format("Scale: %d characters per meter (1 character = %.2f meters)%n", 
            charactersPerMeter, (1.0 / charactersPerMeter)));
        
        int width = (int)(room.getWidth() * charactersPerMeter);
        int length = (int)(room.getLength() * charactersPerMeter);
        char[][] grid = new char[length + 2][width + 2];
        
        // Initialize grid with walls and empty space
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == 0 || i == grid.length - 1 || j == 0 || j == grid[0].length - 1) {
                    grid[i][j] = WALL_CHAR;
                } else {
                    grid[i][j] = EMPTY_CHAR;
                }
            }
        }
        
        // Place furniture
        for (Furniture f : room.getFurniture()) {
            placeFurnitureOnGrid(grid, f);
        }
        
        // Place doors
        for (Door d : room.getDoors()) {
            placeWallItemOnFloorGrid(grid, d, DOOR_CHAR);
        }
        
        // Place windows
        for (Window w : room.getWindows()) {
            placeWallItemOnFloorGrid(grid, w, WINDOW_CHAR);
        }
        
        // Add compass
        output.append("   N\n");
        output.append("W     E\n");
        output.append("   S\n\n");
        
        // Add grid
        for (char[] row : grid) {
            output.append(new String(row)).append("\n");
        }
        
        appendLegend(output);
        
        // Add to main output and save individual file
        currentOutput.append(output);
        saveToFile("floor_plan.txt", output.toString());
    }
    
    @Override
    public void visualizeWall(Wall wall) {
        if (wall == Wall.FLOOR || wall == Wall.CEILING) return;
        
        StringBuilder output = new StringBuilder();
        
        output.append("\n--- ").append(wall.toString().toUpperCase()).append(" WALL VIEW ---\n");
        
        double wallWidth = (wall == Wall.NORTH || wall == Wall.SOUTH) ? room.getWidth() : room.getLength();
        int width = (int)(wallWidth * charactersPerMeter);
        int height = (int)(room.getHeight() * charactersPerMeter);
        char[][] grid = new char[height + 2][width + 2];
        
        // Initialize grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == 0 || i == grid.length - 1 || j == 0 || j == grid[0].length - 1) {
                    grid[i][j] = WALL_CHAR;
                } else {
                    grid[i][j] = EMPTY_CHAR;
                }
            }
        }
        
        // Place doors on wall
        for (Door d : room.getDoors()) {
            if (d.getWall() == wall) {
                placeWallItemOnWallGrid(grid, d, DOOR_CHAR, wall);
            }
        }
        
        // Place windows on wall
        for (Window w : room.getWindows()) {
            if (w.getWall() == wall) {
                placeWallItemOnWallGrid(grid, w, WINDOW_CHAR, wall);
            }
        }
        
        // Project furniture onto wall
        projectFurnitureOnWall(grid, wall);
        
        // Add grid to output
        for (char[] row : grid) {
            output.append(new String(row)).append("\n");
        }
        
        // Add to main output and save individual file
        currentOutput.append(output);
        saveToFile(wall.toString().toLowerCase() + "_wall.txt", output.toString());
    }
    
    @Override
    public void visualizeCeiling() {
        StringBuilder output = new StringBuilder();
        
        output.append("\n--- CEILING VIEW (Looking up) ---\n");
        
        int width = (int)(room.getWidth() * charactersPerMeter);
        int length = (int)(room.getLength() * charactersPerMeter);
        char[][] grid = new char[length + 2][width + 2];
        
        // Initialize grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == 0 || i == grid.length - 1 || j == 0 || j == grid[0].length - 1) {
                    grid[i][j] = WALL_CHAR;
                } else {
                    grid[i][j] = '.';
                }
            }
        }
        
        // Show tall furniture
        for (Furniture f : room.getFurniture()) {
            if (f.getHeight() > room.getHeight() * 0.5) {
                placeFurnitureOnGrid(grid, f, '*');
            }
        }
        
        // Add grid to output
        for (char[] row : grid) {
            output.append(new String(row)).append("\n");
        }
        
        output.append("\n* = Tall furniture visible from ceiling\n");
        
        // Add to main output and save individual file
        currentOutput.append(output);
        saveToFile("ceiling.txt", output.toString());
    }
    
    private void placeFurnitureOnGrid(char[][] grid, Furniture f) {
        placeFurnitureOnGrid(grid, f, FURNITURE_CHAR);
    }
    
    private void placeFurnitureOnGrid(char[][] grid, Furniture f, char symbol) {
        int startX = (int)(f.getX() * charactersPerMeter) + 1;
        int startY = (int)(f.getY() * charactersPerMeter) + 1;
        int endX = Math.min(startX + (int)(f.getWidth() * charactersPerMeter), grid[0].length - 1);
        int endY = Math.min(startY + (int)(f.getLength() * charactersPerMeter), grid.length - 1);
        
        // For very small furniture (1-2 characters), fill completely
        if (endX - startX <= 2 && endY - startY <= 2) {
            for (int i = startY; i < endY; i++) {
                for (int j = startX; j < endX; j++) {
                    grid[i][j] = symbol;
                }
            }
            // Add initial in center if space allows
            if (endX - startX >= 1 && endY - startY >= 1) {
                int centerY = startY + (endY - startY) / 2;
                int centerX = startX + (endX - startX) / 2;
                String initial = f.getName().substring(0, 1).toUpperCase();
                grid[centerY][centerX] = initial.charAt(0);
            }
        } else {
            // For larger furniture, draw outline with initial
            for (int i = startY; i < endY; i++) {
                for (int j = startX; j < endX; j++) {
                    if (i == startY || i == endY - 1 || j == startX || j == endX - 1) {
                        grid[i][j] = symbol;
                    } else if (i == startY + 1 && j == startX + 1) {
                        String initial = f.getName().substring(0, 1).toUpperCase();
                        grid[i][j] = initial.charAt(0);
                    }
                }
            }
        }
    }
    
    private void placeWallItemOnFloorGrid(char[][] grid, WallItem item, char symbol) {
        int pos = (int)(item.getPosition() * charactersPerMeter) + 1;
        int width = (int)(item.getWidth() * charactersPerMeter);
        
        switch (item.getWall()) {
            case NORTH:
                for (int j = pos; j < Math.min(pos + width, grid[0].length - 1); j++) {
                    grid[0][j] = symbol;
                }
                break;
            case SOUTH:
                for (int j = pos; j < Math.min(pos + width, grid[0].length - 1); j++) {
                    grid[grid.length - 1][j] = symbol;
                }
                break;
            case EAST:
                for (int i = pos; i < Math.min(pos + width, grid.length - 1); i++) {
                    grid[i][grid[0].length - 1] = symbol;
                }
                break;
            case WEST:
                for (int i = pos; i < Math.min(pos + width, grid.length - 1); i++) {
                    grid[i][0] = symbol;
                }
                break;
        }
    }
    
    private void placeWallItemOnWallGrid(char[][] grid, WallItem item, char symbol, Wall wall) {
        double wallWidth = (wall == Wall.NORTH || wall == Wall.SOUTH) ? room.getWidth() : room.getLength();
        double position = item.getPosition();
        
        // Apply same west wall perspective fix as ImageVisualizer
        if (wall == Wall.WEST) {
            position = wallWidth - item.getPosition() - item.getWidth();
        }
        
        int pos = (int)(position * charactersPerMeter) + 1;
        int width = (int)(item.getWidth() * charactersPerMeter);
        int bottomHeight = (int)(item.getBottomHeight() * charactersPerMeter);
        int height = (int)(item.getHeight() * charactersPerMeter);
        
        int startRow = grid.length - 2 - bottomHeight - height;
        int endRow = grid.length - 2 - bottomHeight;
        
        for (int i = Math.max(startRow, 1); i < Math.min(endRow, grid.length - 1); i++) {
            for (int j = pos; j < Math.min(pos + width, grid[0].length - 1); j++) {
                grid[i][j] = symbol;
            }
        }
    }
    
    private void projectFurnitureOnWall(char[][] grid, Wall wall) {
        for (Furniture f : room.getFurniture()) {
            projectSingleFurniture(grid, f, wall);
        }
    }
    
    private void projectSingleFurniture(char[][] grid, Furniture f, Wall wall) {
        double distance = 0;
        double projStart = 0;
        double projWidth = 0;
        
        switch (wall) {
            case NORTH:
                distance = f.getY();
                projStart = f.getX();
                projWidth = f.getWidth();
                break;
            case SOUTH:
                distance = room.getLength() - (f.getY() + f.getLength());
                projStart = f.getX();
                projWidth = f.getWidth();
                break;
            case EAST:
                distance = room.getWidth() - (f.getX() + f.getWidth());
                projStart = f.getY();
                projWidth = f.getLength();
                break;
            case WEST:
                distance = f.getX();
                // Flip coordinate for west wall to show from inside perspective
                projStart = room.getLength() - (f.getY() + f.getLength());
                projWidth = f.getLength();
                break;
        }
        
        if (distance < room.getLength() / 3) {
            int start = (int)(projStart * charactersPerMeter) + 1;
            int width = (int)(projWidth * charactersPerMeter);
            int bottomRow = grid.length - 2;
            int topRow = bottomRow - (int)(f.getHeight() * charactersPerMeter);
            
            char projChar = (distance < 1.0) ? '+' : '.';
            
            for (int i = Math.max(topRow, 1); i <= bottomRow; i++) {
                for (int j = start; j < Math.min(start + width, grid[0].length - 1); j++) {
                    if (grid[i][j] == EMPTY_CHAR) {
                        grid[i][j] = projChar;
                    }
                }
            }
        }
    }
    
    private void appendLegend(StringBuilder output) {
        output.append("\nLegend:\n");
        output.append("  # = Wall\n");
        output.append("  D = Door\n");
        output.append("  W = Window\n");
        output.append("  F = Furniture outline\n");
        output.append("  [Letter] = First letter of furniture name\n");
        output.append("  + = Close furniture projection\n");
        output.append("  . = Distant furniture projection\n");
        
        if (!room.getFurniture().isEmpty()) {
            output.append("\nFurniture:\n");
            for (Furniture f : room.getFurniture()) {
                output.append("  ").append(f).append("\n");
            }
        }
        
        if (!room.getDoors().isEmpty()) {
            output.append("\nDoors:\n");
            for (Door d : room.getDoors()) {
                output.append("  ").append(d).append("\n");
            }
        }
        
        if (!room.getWindows().isEmpty()) {
            output.append("\nWindows:\n");
            for (Window w : room.getWindows()) {
                output.append("  ").append(w).append("\n");
            }
        }
    }
    
    private void appendLine(String line) {
        currentOutput.append(line).append("\n");
    }
    
    private void saveToFile(String filename, String content) {
        Path filePath = Paths.get(outputDirectory, filename);
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.print(content);
            System.out.println("Text visualization saved: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving text file " + filename + ": " + e.getMessage());
        }
    }
    
    /**
     * Save just the floor plan to a file
     */
    public void saveFloorPlan(String filename) {
        StringBuilder output = new StringBuilder();
        // Call visualizeFloorPlan but capture its output separately
        // This is a simplified version for standalone floor plan saving
        visualizeFloorPlan(); // This already saves to floor_plan.txt
        System.out.println("Floor plan saved to floor_plan.txt");
    }
    
    /**
     * Get the output directory being used
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }
}