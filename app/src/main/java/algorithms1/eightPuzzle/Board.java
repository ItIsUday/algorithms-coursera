package algorithms1.eightPuzzle; // Remove this line before submission

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int[][] board;
    private final int dim;
    
    /**
     * Create a board from an n-by-n array of tiles,
     * where tiles[row][col] = tile at (row, col)
     */
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("Tiles cannot be null");
        }
        
        this.dim = tiles.length;
        if (this.dim < 2) {
            throw new IllegalArgumentException("Board can't be smaller than 2 x 2");
        }
        
        // Deep copy the tiles
        this.board = new int[this.dim][this.dim];
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                this.board[i][j] = tiles[i][j];
            }
        }
    }
    
    /**
     * String representation of this board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dim).append("\n");
        
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                sb.append(this.board[i][j]);
                if (j < this.dim - 1) {
                    sb.append(" ");
                }
            }
            if (i < this.dim - 1) {
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Board dimension n
     */
    public int dimension() {
        return this.dim;
    }
    
    
    /**
     * Number of tiles out of place
     */
    public int hamming() {
        int dist = 0;
        int pos = 1;
        
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                int val = this.board[i][j];
                if (val != 0 && val != pos) {
                    dist++;
                }
                pos++;
            }
        }
        
        return dist;
    }
    
    /**
     * Sum of Manhattan distances between tiles and goal
     */
    public int manhattan() {
        int dist = 0;
        
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                int val = this.board[i][j];
                if (val == 0) continue;
                
                int actualRow = (val - 1) / this.dim;
                int actualCol = (val - 1) % this.dim;
                
                dist += Math.abs(i - actualRow) + Math.abs(j - actualCol);
            }
        }
        
        return dist;
    }
    
    /**
     * Is this board the goal board?
     */
    public boolean isGoal() {
        int expectedVal = 1;
        
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                int val = this.board[i][j];
                
                // The last position should be 0 (empty space)
                if (i == this.dim - 1 && j == this.dim - 1) {
                    if (val != 0) return false;
                } else {
                    if (val != expectedVal) return false;
                    expectedVal++;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Does this board equal other?
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        
        Board otherBoard = (Board) other;
        
        if (this.dim != otherBoard.dim) return false;
        
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                if (this.board[i][j] != otherBoard.board[i][j]) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * All neighboring boards
     */
    public Iterable<Board> neighbors() {
        List<Board> neighborsList = new ArrayList<>();
        
        // Find the position of the empty tile (0)
        int emptyRow = -1, emptyCol = -1;
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                if (this.board[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                    break;
                }
            }
            if (emptyRow != -1) break;
        }
        
        // Generate neighbors by moving the empty tile in four directions
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // up, down, left, right
        
        for (int[] dir : directions) {
            int newRow = emptyRow + dir[0];
            int newCol = emptyCol + dir[1];
            
            // Check if the new position is within bounds
            if (newRow >= 0 && newRow < this.dim && newCol >= 0 && newCol < this.dim) {
                // Create a new board by swapping the empty tile with the tile at new position
                int[][] newBoard = new int[this.dim][this.dim];
                for (int i = 0; i < this.dim; i++) {
                    for (int j = 0; j < this.dim; j++) {
                        newBoard[i][j] = this.board[i][j];
                    }
                }
                
                // Swap the empty tile with the tile at the new position
                newBoard[emptyRow][emptyCol] = newBoard[newRow][newCol];
                newBoard[newRow][newCol] = 0;
                
                neighborsList.add(new Board(newBoard));
            }
        }
        
        return neighborsList;
    }
    
    /**
     * A board that is obtained by exchanging any pair of tiles
     */
    public Board twin() {
        // Create a copy of the board
        int[][] twinBoard = new int[this.dim][this.dim];
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                twinBoard[i][j] = this.board[i][j];
            }
        }
        
        // Find two non-zero positions to swap
        int[] pos1 = null, pos2 = null;
        
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                if (twinBoard[i][j] != 0) {
                    if (pos1 == null) {
                        pos1 = new int[]{i, j};
                    } else {
                        pos2 = new int[]{i, j};
                        break;
                    }
                }
            }
            if (pos2 != null) break;
        }
        
        // Swap the two tiles
        if (pos1 != null && pos2 != null) {
            int temp = twinBoard[pos1[0]][pos1[1]];
            twinBoard[pos1[0]][pos1[1]] = twinBoard[pos2[0]][pos2[1]];
            twinBoard[pos2[0]][pos2[1]] = temp;
        }
        
        return new Board(twinBoard);
    }
}
