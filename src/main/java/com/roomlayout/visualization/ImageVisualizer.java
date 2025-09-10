package com.roomlayout.visualization;

import com.roomlayout.model.*;
import com.roomlayout.model.Window;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageVisualizer implements Visualizer {
    private static final int SCALE = 50;
    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color FLOOR_COLOR = Color.LIGHT_GRAY;
    private static final Color FURNITURE_COLOR = new Color(255, 46, 99);
    private static final Color DOOR_COLOR = new Color(37, 42, 52);
    private static final Color WINDOW_COLOR = new Color(8, 217, 214);
    private static final Color CEILING_COLOR = new Color(234, 234, 234);

    private final Room room;
    private final String outputDirectory;

    public ImageVisualizer(Room room, String outputDirectory) {
        this.room = room;
        this.outputDirectory = outputDirectory;
        File outputDir = new File(outputDirectory);
        outputDir.mkdirs();


    }

    @Override
    public void visualizeAll() {
        visualizeFloorPlan();
        visualizeWall(Wall.NORTH);
        visualizeWall(Wall.SOUTH);
        visualizeWall(Wall.EAST);
        visualizeWall(Wall.WEST);
        visualizeCeiling();
        System.out.println("Images saved to: " + outputDirectory);
    }

    @Override
    public void visualizeFloorPlan() {
        int width = (int) (room.getWidth() * SCALE) + 40;
        int height = (int) (room.getLength() * SCALE) + 40;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(FLOOR_COLOR);
        g.fillRect(20, 20, (int) (room.getWidth() * SCALE), (int) (room.getLength() * SCALE));

        g.setColor(WALL_COLOR);
        g.setStroke(new BasicStroke(3));
        g.drawRect(20, 20, (int) (room.getWidth() * SCALE), (int) (room.getLength() * SCALE));

        for (Furniture f : room.getFurniture()) {
            drawFurniture(g, f);
        }

        for (Door d : room.getDoors()) {
            drawDoorOnFloorPlan(g, d);
        }

        for (Window w : room.getWindows()) {
            drawWindowOnFloorPlan(g, w);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("N", width / 2 - 5, 15);
        g.drawString("S", width / 2 - 5, height - 5);
        g.drawString("W", 5, height / 2);
        g.drawString("E", width - 15, height / 2);

        drawLegend(g, width - 150, height - 100);

        g.dispose();
        saveImage(image, "floor_plan.png");
    }

    @Override
    public void visualizeWall(Wall wall) {
        if (wall == Wall.FLOOR || wall == Wall.CEILING) return;

        double wallWidth = (wall == Wall.NORTH || wall == Wall.SOUTH) ? room.getWidth() : room.getLength();
        int width = (int) (wallWidth * SCALE) + 40;
        int height = (int) (room.getHeight() * SCALE) + 40;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(250, 240, 230));
        g.fillRect(20, 20, (int) (wallWidth * SCALE), (int) (room.getHeight() * SCALE));

        g.setColor(WALL_COLOR);
        g.setStroke(new BasicStroke(2));
        g.drawRect(20, 20, (int) (wallWidth * SCALE), (int) (room.getHeight() * SCALE));

        for (Door d : room.getDoors()) {
            if (d.getWall() == wall) {
                drawDoorOnWall(g, d);
            }
        }

        for (Window w : room.getWindows()) {
            if (w.getWall() == wall) {
                drawWindowOnWall(g, w);
            }
        }

        drawFurnitureProjections(g, wall, wallWidth);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(wall.toString() + " Wall View", width / 2 - 40, 15);

        g.dispose();
        saveImage(image, wall.toString().toLowerCase() + "_wall.png");
    }

    @Override
    public void visualizeCeiling() {
        int width = (int) (room.getWidth() * SCALE) + 40;
        int height = (int) (room.getLength() * SCALE) + 40;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(CEILING_COLOR);
        g.fillRect(20, 20, (int) (room.getWidth() * SCALE), (int) (room.getLength() * SCALE));

        g.setColor(WALL_COLOR);
        g.setStroke(new BasicStroke(3));
        g.drawRect(20, 20, (int) (room.getWidth() * SCALE), (int) (room.getLength() * SCALE));

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        for (Furniture f : room.getFurniture()) {
            if (f.getHeight() > room.getHeight() * 0.5) {
                drawFurniture(g, f);
            }
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Ceiling View", width / 2 - 40, 15);

        g.dispose();
        saveImage(image, "ceiling.png");
    }

    private void drawFurniture(Graphics2D g, Furniture f) {
        int x = (int) (f.getX() * SCALE) + 20;
        int y = (int) (f.getY() * SCALE) + 20;
        int width = (int) (f.getWidth() * SCALE);
        int length = (int) (f.getLength() * SCALE);

        g.setColor(FURNITURE_COLOR);
        g.fillRect(x, y, width, length);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        g.drawRect(x, y, width, length);

        g.setFont(new Font("Arial", Font.PLAIN, 10));
        FontMetrics fm = g.getFontMetrics();
        String name = f.getName();
        int textWidth = fm.stringWidth(name);
        if (textWidth < width - 4) {
            g.drawString(name, x + (width - textWidth) / 2, y + length / 2);
        } else {
            String initial = name.substring(0, 1);
            g.drawString(initial, x + width / 2 - 3, y + length / 2);
        }
    }

    private void drawDoorOnFloorPlan(Graphics2D g, Door d) {
        g.setColor(DOOR_COLOR);
        g.setStroke(new BasicStroke(4));

        int pos = (int) (d.getPosition() * SCALE) + 20;
        int width = (int) (d.getWidth() * SCALE);

        switch (d.getWall()) {
            case NORTH:
                g.drawLine(pos, 20, pos + width, 20);
                break;
            case SOUTH:
                g.drawLine(pos, 20 + (int) (room.getLength() * SCALE), pos + width, 20 + (int) (room.getLength() * SCALE));
                break;
            case EAST:
                g.drawLine(20 + (int) (room.getWidth() * SCALE), pos, 20 + (int) (room.getWidth() * SCALE), pos + width);
                break;
            case WEST:
                g.drawLine(20, pos, 20, pos + width);
                break;
        }
    }

    private void drawWindowOnFloorPlan(Graphics2D g, Window w) {
        g.setColor(WINDOW_COLOR);
        g.setStroke(new BasicStroke(3));

        int pos = (int) (w.getPosition() * SCALE) + 20;
        int width = (int) (w.getWidth() * SCALE);

        switch (w.getWall()) {
            case NORTH:
                g.drawLine(pos, 20, pos + width, 20);
                break;
            case SOUTH:
                g.drawLine(pos, 20 + (int) (room.getLength() * SCALE), pos + width, 20 + (int) (room.getLength() * SCALE));
                break;
            case EAST:
                g.drawLine(20 + (int) (room.getWidth() * SCALE), pos, 20 + (int) (room.getWidth() * SCALE), pos + width);
                break;
            case WEST:
                g.drawLine(20, pos, 20, pos + width);
                break;
        }
    }

    private void drawDoorOnWall(Graphics2D g, Door d) {
        int x = (int) (d.getPosition() * SCALE) + 20;
        int width = (int) (d.getWidth() * SCALE);
        int height = (int) (d.getHeight() * SCALE);
        int y = 20 + (int) (room.getHeight() * SCALE) - height;

        g.setColor(DOOR_COLOR);
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        g.setColor(Color.YELLOW);
        g.fillOval(x + width - 15, y + height / 2 - 3, 6, 6);
    }

    private void drawWindowOnWall(Graphics2D g, Window w) {
        int x = (int) (w.getPosition() * SCALE) + 20;
        int width = (int) (w.getWidth() * SCALE);
        int height = (int) (w.getHeight() * SCALE);
        int bottomHeight = (int) (w.getBottomHeight() * SCALE);
        int y = 20 + (int) (room.getHeight() * SCALE) - bottomHeight - height;

        g.setColor(WINDOW_COLOR);
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, width, height);

        g.drawLine(x + width / 2, y, x + width / 2, y + height);
        g.drawLine(x, y + height / 2, x + width, y + height / 2);
    }

    private void drawFurnitureProjections(Graphics2D g, Wall wall, double wallWidth) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        for (Furniture f : room.getFurniture()) {
            double distance = calculateDistanceToWall(f, wall);
            if (distance < room.getLength() / 2) {
                double projStart = calculateProjectionStart(f, wall);
                double projWidth = calculateProjectionWidth(f, wall);

                int x = (int) (projStart * SCALE) + 20;
                int width = (int) (projWidth * SCALE);
                int y = 20 + (int) (room.getHeight() * SCALE) - (int) (f.getHeight() * SCALE);
                int height = (int) (f.getHeight() * SCALE);

                float alpha = (float) (0.3 * (1 - distance / (room.getLength() / 2)));
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g.setColor(FURNITURE_COLOR);
                g.fillRect(x, y, width, height);
            }
        }

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private double calculateDistanceToWall(Furniture f, Wall wall) {
        switch (wall) {
            case NORTH:
                return f.getY();
            case SOUTH:
                return room.getLength() - (f.getY() + f.getLength());
            case EAST:
                return room.getWidth() - (f.getX() + f.getWidth());
            case WEST:
                return f.getX();
            default:
                return Double.MAX_VALUE;
        }
    }

    private double calculateProjectionStart(Furniture f, Wall wall) {
        switch (wall) {
            case NORTH:
            case SOUTH:
                return f.getX();
            case EAST:
            case WEST:
                return f.getY();
            default:
                return 0;
        }
    }

    private double calculateProjectionWidth(Furniture f, Wall wall) {
        switch (wall) {
            case NORTH:
            case SOUTH:
                return f.getWidth();
            case EAST:
            case WEST:
                return f.getLength();
            default:
                return 0;
        }
    }

    private void drawLegend(Graphics2D g, int x, int y) {
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.setColor(Color.BLACK);
        g.drawString("Legend:", x, y);

        g.setColor(FURNITURE_COLOR);
        g.fillRect(x, y + 5, 10, 10);
        g.setColor(Color.BLACK);
        g.drawString("Furniture", x + 15, y + 13);

        g.setColor(DOOR_COLOR);
        g.fillRect(x, y + 20, 10, 10);
        g.setColor(Color.BLACK);
        g.drawString("Door", x + 15, y + 28);

        g.setColor(WINDOW_COLOR);
        g.fillRect(x, y + 35, 10, 10);
        g.setColor(Color.BLACK);
        g.drawString("Window", x + 15, y + 43);
    }

    private void saveImage(BufferedImage image, String filename) {
        Path path = Paths.get(outputDirectory, filename);
        try (OutputStream out = Files.newOutputStream(path)) {
            ImageIO.write(image, "png", out);
            System.out.println("Saved: " + path);
        } catch (IOException e) {
            System.err.println("Error saving image " + filename + ": " + e.getMessage());
        }
    }
}