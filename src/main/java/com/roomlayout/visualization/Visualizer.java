package com.roomlayout.visualization;

import com.roomlayout.model.Wall;

public interface Visualizer {
    void visualizeAll();
    void visualizeFloorPlan();
    void visualizeWall(Wall wall);
    void visualizeCeiling();
}