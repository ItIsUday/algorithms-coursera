package algorithms1.kdTrees;

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> points;

    // Constructor
    public PointSET() {
        points = new SET<Point2D>();
    }

    // Check if the set is empty
    public boolean isEmpty() {
        return points.size() == 0;
    }

    // Return the size of the set
    public int size() {
        return points.size();
    }

    // Insert a point into the set
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        points.add(p);
    }

    // Check if the set contains a point
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        return points.contains(p);
    }

    // Draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // Return all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle cannot be null");
        
        ArrayList<Point2D> pointsInRange = new ArrayList<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                pointsInRange.add(p);
            }
        }
        return pointsInRange;
    }

    // Return the nearest point to the query point (null if set is empty)
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (isEmpty()) return null;
        
        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        
        for (Point2D point : points) {
            double distance = p.distanceSquaredTo(point);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = point;
            }
        }
        
        return nearest;
    }
}
