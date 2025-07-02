import stdlib.StdOut;
import stdlib.StdRandom;
import stdlib.StdStats;

public class PercolationStats {
    int m;
    double[] x;
    double mean;
    double stddev;
    double confidenceLow;
    double confidenceHigh;

    // Performs m independent experiments on an n x n percolation system.
    public PercolationStats(int n, int m) {
        if (n <= 0 || m <= 0){
            throw new IllegalArgumentException("Illegal n or m");
        }
        this.m = m;
        this.x = new double[m];
        for (int i = 0; i < m; i++) {
            UFPercolation percolation = new UFPercolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n);
                int col = StdRandom.uniform(n);
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }
            this.x[i] = (double)percolation.numberOfOpenSites()/ (n*n) ;
        }
    }

    // Returns sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(x);
    }

    // Returns sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(x);
    }

    // Returns low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - ((1.96*stddev())/Math.sqrt(m));
    }

    // Returns high endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + ((1.96*stddev())/Math.sqrt(m));
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, m);
        StdOut.printf("Percolation threshold for a %d x %d system:\n", n, n);
        StdOut.printf("  Mean                = %.3f\n", stats.mean());
        StdOut.printf("  Standard deviation  = %.3f\n", stats.stddev());
        StdOut.printf("  Confidence interval = [%.3f, %.3f]\n", stats.confidenceLow(),
                stats.confidenceHigh());
    }
}