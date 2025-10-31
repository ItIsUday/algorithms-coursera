package algorithms1.kdTrees; // Remove this line before submission

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;

    private static final class Node {
        private final Point2D point;
        private final int level;
        private Node left;
        private Node right;

        private Node(Point2D point) {
            this(point, 0);
        }

        private Node(Point2D point, int level) {
            this.point = point;
            this.level = level;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV rect) {
        if (node == null) {
            return;
        }
        // Draw the splitting axis for the current node.
        StdDraw.setPenRadius(0.002);
        if (node.level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), rect.ymin(), node.point.x(), rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), node.point.y(), rect.xmax(), node.point.y());
        }

    StdDraw.setPenRadius(0.01);
    StdDraw.setPenColor(StdDraw.BLACK);
    node.point.draw();

    double labelOffset = 0.01;
    StdDraw.text(node.point.x() + labelOffset, node.point.y() + labelOffset,
        String.format("(%.3f, %.3f)", node.point.x(), node.point.y()));

        if (node.level % 2 == 0) {
            draw(node.left, new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax()));
            draw(node.right, new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        } else {
            draw(node.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y()));
            draw(node.right, new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax()));
        }
    }

    public boolean contains(Point2D p) {
        validatePoint(p);

        Node node = root;
        while (node != null) {
            if (node.point.equals(p)) {
                return true;
            }

            int axis = node.level % 2;
            boolean goLeft = axis == 0
                    ? p.x() < node.point.x()
                    : p.y() < node.point.y();
            node = goLeft ? node.left : node.right;
        }

        return false;
    }

    public void insert(Point2D p) {
        validatePoint(p);

        if (isEmpty()) {
            root = new Node(p);
            size = 1;
            return;
        }

        Node node = root;
        while (true) {
            if (node.point.equals(p)) {
                return; // Do not insert duplicates
            }

            int axis = node.level % 2;
            boolean goLeft = axis == 0
                    ? p.x() < node.point.x()
                    : p.y() < node.point.y();

            if (goLeft) {
                if (node.left == null) {
                    node.left = new Node(p, node.level + 1);
                    break;
                }
                node = node.left;
            } else {
                if (node.right == null) {
                    node.right = new Node(p, node.level + 1);
                    break;
                }
                node = node.right;
            }
        }

        size++;
    }

    public Iterable<Point2D> range(RectHV rect) {
        validateRect(rect);

        List<Point2D> result = new ArrayList<>();
        range(rect, root, result);
        return result;
    }

    private void range(RectHV rect, Node node, List<Point2D> acc) {
        if (node == null) {
            return;
        }

        if (rect.contains(node.point)) {
            acc.add(node.point);
        }

        int axis = node.level % 2;
        boolean goLeft = axis == 0
                ? rect.xmin() < node.point.x()
                : rect.ymin() < node.point.y();
        boolean goRight = axis == 0
                ? rect.xmax() >= node.point.x()
                : rect.ymax() >= node.point.y();

        if (goLeft) {
            range(rect, node.left, acc);
        }
        if (goRight) {
            range(rect, node.right, acc);
        }
    }

    public Point2D nearest(Point2D p) {
        validatePoint(p);
        return isEmpty() ? null : nearest(p, root, root.point);
    }

    private Point2D nearest(Point2D target, Node node, Point2D best) {
        if (node == null) {
            return best;
        }

        double bestDistance = target.distanceSquaredTo(best);
        double currentDistance = target.distanceSquaredTo(node.point);
        if (currentDistance < bestDistance) {
            best = node.point;
            bestDistance = currentDistance;
        }

        int axis = node.level % 2;
        boolean goLeft = axis == 0
                ? target.x() < node.point.x()
                : target.y() < node.point.y();
        Node firstSide = goLeft ? node.left : node.right;
        Node secondSide = goLeft ? node.right : node.left;

        best = nearest(target, firstSide, best);
        bestDistance = target.distanceSquaredTo(best);

        double splitDistance = axis == 0
                ? target.x() - node.point.x()
                : target.y() - node.point.y();
        if (splitDistance * splitDistance < bestDistance) {
            best = nearest(target, secondSide, best);
        }

        return best;
    }

    private void validatePoint(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("point must not be null");
        }
    }

    private void validateRect(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect must not be null");
        }
    }
}
