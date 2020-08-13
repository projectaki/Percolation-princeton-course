import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[] open;
    private final int[] ids;

    private final int gridSize;
    private final WeightedQuickUnionUF uf;
    private int nOfOpen;


    // creates n-by-n grid, with all sites initially blocked
    //
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        nOfOpen = 0;
        gridSize = n;
        int idsize = (int) Math.pow(n, 2) + 2;
        open = new int[idsize];
        ids = new int[idsize];

        uf = new WeightedQuickUnionUF(idsize);

        for (int i = 0; i < idsize; i++) {
            open[i] = 0;

            ids[i] = i;

        }


    }

    private int convert(int i, int j) {
        return ((i - 1) * gridSize + (j));
    }

    private void validate(int row, int col) {
        if (row < 1 || col > gridSize) {
            throw new IllegalArgumentException();
        }
    }


    // opens the site (row, col) if it is not open already
    // check if not open. If not open then check for adjacent opens, where there is adjacent open call union.
    public void open(int row, int col) {
        validate(row, col);
        // System.out.println("Is " + row + "," + col + " open " + isOpen(row, col));
        if (!isOpen(row, col)) {
            if (gridSize == 1) {
                uf.union(ids[1], ids[0]);
                uf.union(ids[1], ids[(int) Math.pow(gridSize, 2) + 1]);
            }
            int firstOrLast = firstOrLastRow(row);
            // System.out.println(
            // "First or last row, 1 is first, 2 is last, 0 is neither :" + firstOrLast);
            int ufid = convert(row, col);

            if (firstOrLast == 1) {
                uf.union(ids[0], ids[ufid]);
            }
            if (firstOrLast == 2) {
                uf.union(ids[(int) Math.pow(gridSize, 2) + 1], ids[ufid]);

            }
            // System.out.println("Converted id of " + row + "," + col + "is " + ufid);
            open[ufid] = 1;
            // System.out.println("This value should be 1: " + open[ufid]);
            nOfOpen++;
            // System.out.println("Number of open: " + nOfOpen);

            int[] coords = findAdj(row, col);
            // System.out.println(coords);
            int topCheck = convert(coords[0], coords[1]);
            int rightCheck = convert(coords[2], coords[3]);
            int botCheck = convert(coords[4], coords[5]);
            // System.out.println("botcheck: " + botCheck);
            int leftCheck = convert(coords[6], coords[7]);

            if ((coords[0] >= 1 && coords[0] <= gridSize) && (coords[1] >= 1
                    && coords[1] <= gridSize)) {
                if (open[topCheck] == 1) {
                    uf.union(ids[ufid], ids[topCheck]);
                }
            }
            if ((coords[2] >= 1 && coords[2] <= gridSize) && (coords[3] >= 1
                    && coords[3] <= gridSize)) {
                if (open[rightCheck] == 1) {
                    uf.union(ids[ufid], ids[rightCheck]);
                    // System.out.println(
                    // coords.get(2) + coords.get(3) + rightCheck + "union of: " + ids[ufid]
                    //  + "and" + ids[rightCheck]);
                    // System.out.println(uf.find(ids[ufid]) == uf.find(ids[rightCheck]));
                }
            }
            if ((coords[4] >= 1 && coords[4] <= gridSize) && (coords[5] >= 1
                    && coords[5] <= gridSize)) {
                if (open[botCheck] == 1) {
                    uf.union(ids[ufid], ids[botCheck]);
                    // System.out.println(
                    //  coords.get(4) + coords.get(5) + botCheck + "union of: " + ids[ufid]
                    //      + "and" + ids[botCheck]);
                    //    System.out.println(uf.find(ids[ufid]) == uf.find(ids[botCheck]));
                }
            }
            if ((coords[6] >= 1 && coords[6] <= gridSize) && (coords[7] >= 1
                    && coords[7] <= gridSize)) {
                if (open[leftCheck] == 1) {
                    uf.union(ids[ufid], ids[leftCheck]);
                    //  System.out.println(
                    //      coords.get(6) + coords.get(7) + leftCheck + "union of: " + ids[ufid]
                    //           + "and" + ids[leftCheck]);
                    //   System.out.println(uf.find(ids[ufid]) == uf.find(ids[leftCheck]));
                }
            }


        }
    }

    private int firstOrLastRow(int row) {
        if (row == 1) {
            return 1;
        }
        if (row == gridSize) {
            return 2;
        }
        else return 0;
    }
    // is the site (row, col) open?


    public boolean isOpen(int row, int col) {
        validate(row, col);
        int ufid = convert(row, col);
        return (open[ufid] != 0);


    }

    private int[] findAdj(int row, int col) {

        return new int[] { row - 1, col, row, col + 1, row + 1, col, row, col - 1 };
    }

    // is the site (row, col) full?
    //
    public boolean isFull(int row, int col) {
        validate(row, col);
        int ufid = convert(row, col);
        return uf.find(ids[ufid]) == uf.find(ids[0]);

    }

    // returns the number of open sites
    // variable to keep track maybe
    public int numberOfOpenSites() {

        return nOfOpen;
    }

    // does the system percolate?
    // implement virtual top and bottom, call connected on them.
    // how should i implement virtual top/bottom 0 and n
    public boolean percolates() {
        return uf.find(ids[0]) == uf.find(ids[(int) Math.pow(gridSize, 2) + 1]);

    }

    private int[] random() {
        int[] xAndY = new int[2];
        xAndY[0] = StdRandom.uniform(1, gridSize + 1);
        xAndY[1] = StdRandom.uniform(1, gridSize + 1);

        return xAndY;
    }

    // test client (optional)

    public static void main(String[] args) {
        int n = 20;
        Percolation p = new Percolation(n);


        while (!p.percolates()) {
            //  System.out.println("Percolates or not: " + p.percolates());
            int[] temp = p.random();
            //   System.out.println("temp numbers: " + temp.get(0) + "," + temp.get(1));

            p.open(temp[0], temp[1]);

        }
        System.out.println(p.nOfOpen);

    }
}
