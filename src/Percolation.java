/**
 * @author Viktor_Molokostov
 *
 */
public class Percolation {

    /**
     *
     */
    private boolean[][] sites;

    /**
     *
     */
    private WeightedQuickUnionUF uf, uf2;

    /**
     *
     */
    private int bottomVirtualSiteIndex;

    /**
     *
     */
    private final boolean siteOpen = true;

    /**
     *
     */
    private final boolean siteClosed = false;

    /**
     *
     */
    private final int topVirtualSiteIndex = 0;

    /**
     * create N-by-N grid, with all sites blocked.
     *
     * @param n size
     */
    public Percolation(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        sites = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                close(i, j);
            }
        }
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 1);
        bottomVirtualSiteIndex = n * n + 1;
    }

    /**
     * open site (row i, column j) if it is not already.
     *
     * @param i row
     * @param j column
     */
    public void open(final int i, final int j) {
        int row = toIndex(i);
        int col = toIndex(j);
        sites[row][col] = siteOpen;
        int currentIndex = toUfIndex(i, j);

        // if site is not in top row
        if (1 != i) {
            if (isOpen(i - 1, j)) {
                uf.union(currentIndex, toUfIndex(i - 1, j));
                uf2.union(currentIndex, toUfIndex(i - 1, j));
            }
        } else {
            uf.union(topVirtualSiteIndex, currentIndex);
            uf2.union(topVirtualSiteIndex, currentIndex);
        }

        // if site is not in bottom row
        if (sites.length != i) {
            if (isOpen(i + 1, j)) {
                uf.union(toUfIndex(i + 1, j), currentIndex);
                uf2.union(toUfIndex(i + 1, j), currentIndex);
            }
        } else {
            uf.union(bottomVirtualSiteIndex, currentIndex);
        }

        // if site is not in most left column and site on the left is open
        if (j != 1 && isOpen(i, j - 1)) {
            uf.union(currentIndex, toUfIndex(i, j - 1));
            uf2.union(currentIndex, toUfIndex(i, j - 1));
        }

        // if site is not in most right column and site on the right is open
        if (j != sites.length && isOpen(i, j + 1)) {
            uf.union(currentIndex, toUfIndex(i, j + 1));
            uf2.union(currentIndex, toUfIndex(i, j + 1));
        }
    }

    /**
     * Is site (row, column) open?
     *
     * @param i row
     * @param j column
     * @return true if open
     */
    public final boolean isOpen(final int i, final int j) {
        return sites[toIndex(i)][toIndex(j)] == siteOpen;
    }

    /**
     * is site (row i, column j) full?
     *
     * @param i row
     * @param j column
     * @return is full
     */
    public final boolean isFull(final int i, final int j) {
        if (i <= 0 || j <= 0) {
            throw new IllegalArgumentException();
        }
        int currentIndex = toUfIndex(i, j);
        return uf2.connected(topVirtualSiteIndex, currentIndex);
    }

    /**
     * does the system percolate?
     *
     * @return Boolean
     */
    public final boolean percolates() {
        return uf.connected(topVirtualSiteIndex, bottomVirtualSiteIndex);
    }

    /**
     * Close site (row i, column j) if it is not already.
     *
     * @param row row (0-started)
     * @param col column (0-started)
     */
    private void close(final int row, final int col) {
        sites[row][col] = siteClosed;
    }

    /**
     * Converts 2D into 1D indexes.
     *
     * @param i
     *            row number
     * @param j
     *            column number
     * @return index in array for UF class
     */
    private int toUfIndex(final int i, final int j) {
        return (i - 1) * sites.length + j;
    }

    /**
     * Converts PercolationStats index to usual 0-started index.
     *
     * @param i 1-started index
     * @return 0-started index
     */
    private int toIndex(final int i) {
        return i - 1;
    }
} // Percolation
