import dsa.MinPQ;
import stdlib.StdOut;

public class Ramanujan2 {
    // Entry point.
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        MinPQ<Pair> pq = new MinPQ<Pair>();
        for (int i = 1; i * i * i < n; i++) {
            pq.insert(new Pair(i, i + 1));
        }
        Pair prev = null;
        Pair curr = null;
        while (!pq.isEmpty()) {
            prev = curr;
            curr = pq.delMin();

            if (prev != null && prev.sumOfCubes == curr.sumOfCubes && prev.sumOfCubes <= n) {
                StdOut.println(prev.sumOfCubes + " = " + prev.i + "^3 + " + prev.j + "^3 = " + curr.i + "^3 + " + curr.j + "^3");
            }
            if (curr.j * curr.j * curr.j < n) {
                pq.insert(new Pair(curr.i, curr.j + 1));
            }
        }
    }

    // A data type that encapsulates a pair of numbers (i, j) and the sum of their cubes.
    private static class Pair implements Comparable<Pair> {
        private int i;          // first number in the pair
        private int j;          // second number in the pair
        private int sumOfCubes; // i^3 + j^3

        // Constructs a pair (i, j).
        public Pair(int i, int j) {
            this.i = i;
            this.j = j;
            sumOfCubes = i * i * i + j * j * j;
        }

        // Returns a comparison of pairs this and other based on their sum-of-cubes values.
        public int compareTo(Pair other) {
            return sumOfCubes - other.sumOfCubes;
        }
    }
}
