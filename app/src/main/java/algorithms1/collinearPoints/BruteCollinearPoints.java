package algorithms1.collinearPoints; // Remove this line before submission

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Finds all line segments containing 4 collinear points using brute force approach.
 */
public class BruteCollinearPoints {
    
    private final LineSegment[] lineSegments;
    
    /**
     * Finds all line segments containing 4 points.
     * 
     * @param points Array of points to examine for collinearity
     * @throws IllegalArgumentException if points array is null, contains null points, or duplicate points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array cannot be null");
        }
        
        // Check for null points and duplicates
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point cannot be null");
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException("Point cannot be null");
                }
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Duplicate points are not allowed");
                }
            }
        }
        
        this.lineSegments = findSegments(points);
    }
    
    /**
     * Find all line segments with 4 collinear points
     * 
     * @param points Array of points to examine
     * @return Array of line segments containing 4 collinear points
     */
    private LineSegment[] findSegments(Point[] points) {
        int n = points.length;
        ArrayList<LineSegment> segments = new ArrayList<>();
        
        for (int p = 0; p < n; p++) {
            for (int q = p + 1; q < n; q++) {
                for (int r = q + 1; r < n; r++) {
                    for (int s = r + 1; s < n; s++) {
                        Point p1 = points[p];
                        Point p2 = points[q];
                        Point p3 = points[r];
                        Point p4 = points[s];
                        
                        double slope1 = p1.slopeTo(p2);
                        double slope2 = p1.slopeTo(p3);
                        double slope3 = p1.slopeTo(p4);
                        
                        if (Double.compare(slope1, slope2) == 0 && Double.compare(slope2, slope3) == 0) {
                            Point[] collinearPoints = {p1, p2, p3, p4};
                            Arrays.sort(collinearPoints);
                            segments.add(new LineSegment(collinearPoints[0], collinearPoints[3]));
                        }
                    }
                }
            }
        }
        
        return segments.toArray(new LineSegment[0]);
    }
    
    /**
     * Returns the number of line segments
     * 
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return lineSegments.length;
    }
    
    /**
     * Returns the line segments
     * 
     * @return array of line segments
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }
}
