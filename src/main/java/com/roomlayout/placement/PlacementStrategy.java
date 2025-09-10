package com.roomlayout.placement;

import com.roomlayout.model.Point2D;

public interface PlacementStrategy {
    Point2D calculatePosition(PlacementContext context);
}