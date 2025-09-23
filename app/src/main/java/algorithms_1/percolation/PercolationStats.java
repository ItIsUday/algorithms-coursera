package algorithms_1.percolation; // Remove this line before submission

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE = 1.96;
    private final double[] value;

    public PercolationStats(int n, int trials) {
        int i, row, col;
        double openSites;
        int totalSites = n * n;

        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Value of N or T is less than or equal to 0");
        
        value = new double[trials];
        for (i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                row = StdRandom.uniformInt(1, n + 1);
                col = StdRandom.uniformInt(1, n + 1);
                perc.open(row, col);
            }
            openSites = perc.numberOfOpenSites();
            value[i] = openSites / totalSites;
        }
    }

    public double mean() {
        return StdStats.mean(value);
    }

    public double stddev() {
        return StdStats.stddev(value);
    }

    public double confidenceLo() {
        return (this.mean() - ((CONFIDENCE * stddev()) / Math.sqrt(value.length)));
    }

    public double confidenceHi() {
        return (this.mean() + ((CONFIDENCE * stddev()) / Math.sqrt(value.length)));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats perc = new PercolationStats(n, t);
        
        StdOut.printf("mean                    = %f\n", perc.mean());
        StdOut.printf("stddev                  = %f\n", perc.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", perc.confidenceLo(), perc.confidenceHi());
    }
}