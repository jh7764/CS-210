import dsa.Inversions;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

// A data type to represent a board in the 8-puzzle game or its generalizations.
public class Board {
    int[][] tiles;
    int n;
    int hamming;
    int manhattan;
    int blankPos;

    // Constructs a board from an n x n array; tiles[i][j] is the tile at row i and column j, with 0
    // denoting the blank tile.
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                this.tiles[i][j] = tiles[i][j];
            }
        }
        this.hamming = hamming();
        this.manhattan = manhattan();
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if (tiles[i][j] == 0){
                    this.blankPos = i * n + j + 1;
                }
            }
        }
    }

    // Returns the size of this board.
    public int size() {
        return n;
    }

    // Returns the tile at row i and column j of this board.
    public int tileAt(int i, int j) {
        return this.tiles[i][j];
    }

    // Returns Hamming distance between this board and the goal board.
    public int hamming() {
        int distance = 0;
        int moves = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != moves) {
                    distance++;
                }
                moves++;
            }
        }
        return distance;
    }

    // Returns the Manhattan distance between this board and the goal board.
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                    int x = (tiles[i][j] - 1) / n;
                    int y = (tiles[i][j] - 1) % n;
                    distance += Math.abs(x - i) + Math.abs(y - j);
                }
        }
        return distance;

    }

    // Returns true if this board is the goal board, and false otherwise.
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // Returns true if this board is solvable, and false otherwise.
    public boolean isSolvable() {
        int[] b = new int[n*n -1];
        int k = 0;
        for (int[] row : tiles){
            for (int count : row){
                if (count != 0){
                    b[k++] = count;
                }
            }
        }

        long inv = Inversions.count(b);
        if (n % 2 == 0){
            return ((blankPos - 1)/ n + inv) % 2 != 0;
        }
        else {
            return inv % 2 == 0;
        }
    }


    // Returns an iterable object containing the neighboring boards of this board.
    public Iterable<Board> neighbors() {
        int i = (blankPos - 1) / n;
        int j = (blankPos - 1) % n;
        int[][] temp;
        Board neighbor;
        LinkedQueue<Board> q = new LinkedQueue<Board>();
        if (i > 0){
            temp = cloneTiles();
            int t = temp[i][j];
            temp[i][j] = temp[i-1][j];
            temp[i-1][j] = t;
            q.enqueue(new Board(temp));
        }
        if (i < n-1){
            temp = cloneTiles();
            int t = temp[i][j];
            temp[i][j] = temp[i+1][j];
            temp[i+1][j] = t;
            q.enqueue(new Board(temp));
        }
        if (j > 0){
            temp = cloneTiles();
            int t = temp[i][j];
            temp[i][j] = temp[i][j-1];
            temp[i][j-1] = t;
            q.enqueue(new Board(temp));
        }
        if (j < n-1){
            temp = cloneTiles();
            int t = temp[i][j];
            temp[i][j] = temp[i][j+1];
            temp[i][j+1] = t;
            q.enqueue(new Board(temp));
        }
        return q;

    }

    // Returns true if this board is the same as other, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Board o = (Board) other;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != o.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Returns a string representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) {
                    s.append(" ");
                }
            }
            if (i < n - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Returns a defensive copy of tiles[][].
    private int[][] cloneTiles() {
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }
    }
}
