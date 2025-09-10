package com.roomlayout.visualization;

import com.roomlayout.model.*;

public class ConsoleVisualizer implements Visualizer {
    private static final int SCALE = 10;
    private static final char WALL_CHAR = '#';
    private static final char EMPTY_CHAR = ' ';
    private static final char DOOR_CHAR = 'D';
    private static final char WINDOW_CHAR = 'W';
    private static final char FURNITURE_CHAR = 'F';
    
    private final Room room;
    
    public ConsoleVisualizer(Room room) {
        this.room = room;
    }
    
    @Override
    public void visualizeAll() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ROOM LAYOUT VISUALIZATION");
        System.out.println("=".repeat(60));
        System.out.println(room);
        System.out.println();
        
        visualizeFloorPlan();
        visualizeWall(Wall.NORTH);
        visualizeWall(Wall.SOUTH);
        visualizeWall(Wall.EAST);
        visualizeWall(Wall.WEST);
        visualizeCeiling();
    }
    
    @Override
    public void visualizeFloorPlan() {
        System.out.println("\n--- FLOOR PLAN (Top-down view) ---");
        System.out.println("Scale: 1 character = " + (1.0 / SCALE) + " meters");
        
        int width = (int)(room.getWidth() * SCALE);
        int length = (int)(room.getLength() * SCALE);
        char[][] grid = new char[length + 2][width + 2];
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == 0 || i == grid.length - 1 || j == 0 || j == grid[0].length - 1) {
                    grid[i][j] = WALL_CHAR;
                } else {
                    grid[i][j] = EMPTY_CHAR;
                }
            }
        }
        
        for (Furniture f : room.getFurniture()) {
            placeFurnitureOnGrid(grid, f);
        }
        
        for (Door d : room.getDoors()) {
            placeWallItemOnFloorGrid(grid, d, DOOR_CHAR);
        }
        
        for (Window w : room.getWindows()) {
            placeWallItemOnFloorGrid(grid, w, WINDOW_CHAR);
        }
        
        System.out.println("   N");
        System.out.println("W     E");
        System.out.println("   S");
        System.out.println();
        
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
        
        printLegend();
    }
    
    @Override
    public void visualizeWall(Wall wall) {
        if (wall == Wall.FLOOR || wall == Wall.CEILING) return;
        
        System.out.println("\n--- " + wall.toString().toUpperCase() + " WALL VIEW ---");
        
        double wallWidth = (wall == Wall.NORTH || wall == Wall.SOUTH) ? room.getWidth() : room.getLength();
        int width = (int)(wallWidth * SCALE);
        int height = (int)(room.getHeight() * SCALE);
        char[][] grid = new char[height + 2][width + 2];
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == 0 || i == grid.length - 1 || j == 0 || j == grid[0].length - 1) {
                    grid[i][j] = WALL_CHAR;
                } else {
                    grid[i][j] = EMPTY_CHAR;
                }
            }
        }
        
        for (Door d : room.getDoors()) {
            if (d.getWall() == wall) {
                placeWallItemOnWallGrid(grid, d, DOOR_CHAR);
            }
        }
        
        for (Window w : room.getWindows()) {
            if (w.getWall() == wall) {
                placeWallItemOnWallGrid(grid, w, WINDOW_CHAR);
            }
        }
        
        projectFurnitureOnWall(grid, wall);
        
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
    }
    
    @Override
    public void visualizeCeiling() {
        System.out.println("\n--- CEILING VIEW (Looking up) ---");
        
        int width = (int)(room.getWidth() * SCALE);
        int length = (int)(room.getLength() * SCALE);
        char[][] grid = new char[length + 2][width + 2];
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == 0 || i == grid.length - 1 || j == 0 || j == grid[0].length - 1) {
                    grid[i][j] = WALL_CHAR;
                } else {
                    grid[i][j] = '.';
                }
            }
        }
        
        for (Furniture f : room.getFurniture()) {
            if (f.getHeight() > room.getHeight() * 0.5) {
                placeFurnitureOnGrid(grid, f, '*');
            }
        }
        
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
        
        System.out.println("\n* = Tall furniture visible from ceiling");
    }
    
    private void placeFurnitureOnGrid(char[][] grid, Furniture f) {
        placeFurnitureOnGrid(grid, f, FURNITURE_CHAR);
    }
    
    private void placeFurnitureOnGrid(char[][] grid, Furniture f, char symbol) {
        int startX = (int)(f.getX() * SCALE) + 1;
        int startY = (int)(f.getY() * SCALE) + 1;
        int endX = Math.min(startX + (int)(f.getWidth() * SCALE), grid[0].length - 1);
        int endY = Math.min(startY + (int)(f.getLength() * SCALE), grid.length - 1);
        
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
    
    private void placeWallItemOnFloorGrid(char[][] grid, WallItem item, char symbol) {
        int pos = (int)(item.getPosition() * SCALE) + 1;
        int width = (int)(item.getWidth() * SCALE);
        
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
    
    private void placeWallItemOnWallGrid(char[][] grid, WallItem item, char symbol) {
        int pos = (int)(item.getPosition() * SCALE) + 1;
        int width = (int)(item.getWidth() * SCALE);
        int bottomHeight = (int)(item.getBottomHeight() * SCALE);
        int height = (int)(item.getHeight() * SCALE);
        
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
                projStart = f.getY();
                projWidth = f.getLength();
                break;
        }
        
        if (distance < room.getLength() / 3) {
            int start = (int)(projStart * SCALE) + 1;
            int width = (int)(projWidth * SCALE);
            int bottomRow = grid.length - 2;
            int topRow = bottomRow - (int)(f.getHeight() * SCALE);
            
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
    
    private void printLegend() {
        System.out.println("\nLegend:");
        System.out.println("  # = Wall");
        System.out.println("  D = Door");
        System.out.println("  W = Window");
        System.out.println("  F = Furniture outline");
        System.out.println("  [Letter] = First letter of furniture name");
        
        if (!room.getFurniture().isEmpty()) {
            System.out.println("\nFurniture:");
            for (Furniture f : room.getFurniture()) {
                System.out.println("  " + f);
            }
        }
        
        if (!room.getDoors().isEmpty()) {
            System.out.println("\nDoors:");
            for (Door d : room.getDoors()) {
                System.out.println("  " + d);
            }
        }
        
        if (!room.getWindows().isEmpty()) {
            System.out.println("\nWindows:");
            for (Window w : room.getWindows()) {
                System.out.println("  " + w);
            }
        }
    }
}