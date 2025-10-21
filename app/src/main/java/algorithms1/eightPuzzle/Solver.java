package algorithms1.eightPuzzle; // Remove this line before submission

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {
    
    private static class Node implements Comparable<Node> {
        private final int priority;
        private final int moves;
        private final Board board;
        private final Node previous;
        
        public Node(int moves, Board board, Node previous) {
            this.moves = moves;
            this.board = board;
            this.previous = previous;
            this.priority = moves + board.manhattan();
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.priority, other.priority);
        }
    }
    
    private final boolean solvable;
    private final int minMoves;
    private final Node solutionNode;
    
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board cannot be null");
        }
        
        // Find a solution to the initial board (using the A* algorithm)
        // Solvability can also be identified by using inversion method
        Board twinBoard = initial.twin();
        MinPQ<Node> heap = new MinPQ<>();
        MinPQ<Node> theap = new MinPQ<>();
        
        heap.insert(new Node(0, initial, null));
        theap.insert(new Node(0, twinBoard, null));
        
        while (!heap.isEmpty() && !heap.min().board.isGoal() &&
               !theap.isEmpty() && !theap.min().board.isGoal()) {
            
            Node node = heap.delMin();
            Node tnode = theap.delMin();
            
            for (Board neighbor : node.board.neighbors()) {
                if (node.previous != null && neighbor.equals(node.previous.board)) {
                    continue;
                }
                heap.insert(new Node(node.moves + 1, neighbor, node));
            }
            
            for (Board neighbor : tnode.board.neighbors()) {
                if (tnode.previous != null && neighbor.equals(tnode.previous.board)) {
                    continue;
                }
                theap.insert(new Node(tnode.moves + 1, neighbor, tnode));
            }
        }
        
        if (!heap.isEmpty() && heap.min().board.isGoal()) {
            this.solvable = true;
            this.minMoves = heap.min().moves;
            this.solutionNode = heap.min();
        } else {
            this.solvable = false;
            this.minMoves = -1;
            this.solutionNode = null;
        }
    }
    
    public boolean isSolvable() {
        return solvable;
    }
    
    public int moves() {
        return minMoves;
    }
    
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        
        Stack<Board> path = new Stack<>();
        Node node = solutionNode;
        while (node != null) {
            path.push(node.board);
            node = node.previous;
        }
        
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    @Override
                    public boolean hasNext() {
                        return !path.isEmpty();
                    }
                    
                    @Override
                    public Board next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return path.pop();
                    }
                };
            }
        };
    }

    public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
}
}
