/*----------------------------------------------------------------
 *  Assignment 1.
 *
 *  Author:        Viktor Molokostov
 *  Written:       8/29/2012
 *  Last updated:  6/29/2014
 *
 *  Compilation:   javac PercolationStats.java
 *  Execution:     java PercolationStats 200 100
 *
 *  Prints mean, standard deviation and 95% confidence interval.
 *
 *  % java PercolationStats 200 100
 *  mean                    = 0.5929934999999997
 *  stddev                  = 0.00876990421552567
 *  95% confidence interval = 0.5912745987737567, 0.5947124012262428
 *
 *----------------------------------------------------------------*/

/**
 * Percolation statistics class.
 *
 * @author Viktor_Molokostov
 */
public class PercolationStats {

    /**
     * * Size of an N-by-N grid.
     */
    private int gridSize;

    /**
     * Percolate numbers for each experiment.
     */
    private double[] x;

    /**
     * Used in formula.
     */
    private static final double COEFFICIENT = 1.96;

    /**
     * Perform T independent computational experiments on an N-by-N grid.
     *
     * @param n
     *            grid size
     * @param t
     *            number of experiments
     */
    public PercolationStats(final int n, final int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException();
        }
        gridSize = n;
        x = new double[t];
        int totalSites = gridSize * gridSize;

        for (int i = 0; i < t; i++) {
            int num = percolateNumber();
            x[i] = (double) num / totalSites;
        }
    }

    /**
     * Sample mean of percolation threshold.
     *
     * @return mean
     */
    public final double mean() {
        return StdStats.mean(x);
    }

    /**
     * Sample standard deviation of percolation threshold.
     *
     * @return standard deviation
     */
    public final double stddev() {
        return StdStats.stddev(x);
    }

    /**
     * Sample lower bound of the 95% confidence interval.
     *
     * @return lower bound of the 95% confidence interval
     */
    public final double confidenceLo() {
        return mean() - COEFFICIENT * stddev() / Math.sqrt(x.length);
    }

    /**
     * Sample upper bound of the 95% confidence interval.
     *
     * @return upper bound of the 95% confidence interval
     */
    public final double confidenceHi() {
        return mean() + COEFFICIENT * stddev() / Math.sqrt(x.length);
    }

    /**
     * Test client.
     *
     * @param args
     *            arguments passed from command line
     */
    public static void main(final String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, t);

        System.out.println("mean                    = " + stats.mean());

        System.out.println("stddev                  = " + stats.stddev());

        System.out.println("95% confidence interval = " + stats.confidenceLo()
                + ", " + stats.confidenceHi());
    }

    /**
     * Counts percolation number for one specific grid.
     *
     * @return Integer
     */
    private int percolateNumber() {
        int count = 0;
        // sut means system under test
        Percolation sut = new Percolation(gridSize);
        while (!sut.percolates()) {
            int i = StdRandom.uniform(gridSize) + 1;
            int j = StdRandom.uniform(gridSize) + 1;
            if (sut.isOpen(i, j)) {
                continue;
            }
            sut.open(i, j);
            count++;
        }
        return count;
    }
}
