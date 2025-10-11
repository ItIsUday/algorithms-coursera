package algorithms1.collinearPoints; // Remove this line before submission

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    /**
     * Finds all line segments containing 4 or more collinear points.
     */
    
    private final LineSegment[] lineSegments;
    
    /**
     * Finds all line segments containing 4 or more points.
     * 
     * @param points Array of points to examine for collinearity
     * @throws IllegalArgumentException if points array is null, contains null points, or has duplicates
     */
    public FastCollinearPoints(Point[] points) {
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
     * Find all maximal line segments containing 4 or more collinear points.
     * Only the longest segment for each set of collinear points is returned.
     */
    private LineSegment[] findSegments(Point[] points) {
        List<LineSegment> segments = new ArrayList<>();
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        
        for (Point p : points) {
            // Sort all points by slope with respect to p
            Arrays.sort(pointsCopy, p.slopeOrder());
            
            int start = 0;
            int end = 1;
            
            while (end < pointsCopy.length) {
                // Find all points with the same slope
                while (end < pointsCopy.length && p.slopeOrder().compare(pointsCopy[start], pointsCopy[end]) == 0) end++;
                
                // Skip groups with fewer than 3 points (need 4 total including p)
                // Also skip if the group contains p itself (slope would be negative infinity)
                if (end - start < 3 || p.slopeOrder().compare(pointsCopy[start], p) == 0) {
                    start = end;
                    if (end < pointsCopy.length) end++;
                    continue;
                }
                
                // Create line segment with p and all collinear points
                List<Point> lineSegment = new ArrayList<>();
                lineSegment.add(p);
                for (int i = start; i < end; i++) lineSegment.add(pointsCopy[i]);
                
                // Sort points by natural order (y-coordinate, then x-coordinate)
                lineSegment.sort(Point::compareTo);
                
                // Only add segment if p is the smallest point (to avoid duplicates)
                if (p.equals(lineSegment.get(0))) {
                    segments.add(new LineSegment(lineSegment.get(0), 
                                               lineSegment.get(lineSegment.size() - 1)));
                }
                
                start = end;
                if (end < pointsCopy.length) end++;
            }
        }
        
        return segments.toArray(new LineSegment[0]);
    }
    
    /**
     * Returns the number of line segments
     */
    public int numberOfSegments() {
        return lineSegments.length;
    }
    
    /**
     * Returns the line segments
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
        int x = in.readInt();
        int y = in.readInt();
        points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
}
}
