package algorithms1.percolation; // Remove this line before submission

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final boolean[][] grid;  // false indicates blocked site
    private int openSitesCount;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf_backwash;
    private final int virtualTop;
    private final int virtualBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size must be greater than 0");
        }
        this.n = n;
        this.grid = new boolean[n][n];  // false by default (blocked)
        this.openSitesCount = 0;
        this.uf = new WeightedQuickUnionUF(n * n + 2);  // +2 for virtual top and bottom
        this.uf_backwash = new WeightedQuickUnionUF(n * n + 1); // +1 for virtual top only
        this.virtualTop = n * n;
        this.virtualBottom = n * n + 1;
    }
    
    // Validates 0-indexed row and column
    private boolean isValid(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }
    
    // Converts 1-indexed coordinates to 0-indexed
    private int[] to0Indexed(int row, int col) {
        return new int[]{row - 1, col - 1};
    }
    
    // Maps 2D coordinates to 1D index
    private int xyTo1D(int row, int col) {
        return row * n + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        
        int[] idx = to0Indexed(row, col);
        int zeroRow = idx[0];
        int zeroCol = idx[1];
        
        grid[zeroRow][zeroCol] = true;
        openSitesCount++;
        int index = xyTo1D(zeroRow, zeroCol);
        
        // Connect to virtual top if this is a top row site
        if (zeroRow == 0) {
            uf.union(index, virtualTop);
            uf_backwash.union(index, virtualTop);
        }
        
        // Connect to virtual bottom if this is a bottom row site
        if (zeroRow == n - 1) {
            uf.union(index, virtualBottom);
        }
        
        // Check all four directions for open neighbors
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = zeroRow + dir[0];
            int newCol = zeroCol + dir[1];
            if (isValid(newRow, newCol) && grid[newRow][newCol]) {
                int neighborIndex = xyTo1D(newRow, newCol);
                uf.union(index, neighborIndex);
                uf_backwash.union(index, neighborIndex);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int[] idx = to0Indexed(row, col);
        int zeroRow = idx[0];
        int zeroCol = idx[1];
        
        if (!isValid(zeroRow, zeroCol)) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return grid[zeroRow][zeroCol];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int[] idx = to0Indexed(row, col);
        int zeroRow = idx[0];
        int zeroCol = idx[1];
        
        if (!isValid(zeroRow, zeroCol)) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        
        return grid[zeroRow][zeroCol] && 
        uf_backwash.find(xyTo1D(zeroRow, zeroCol)) == uf_backwash.find(virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }

    public static void main(String[] args) {
        System.out.println("Percolation class");
    }
}
