import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // Array for open site (boolean, 0 closed, 1 open)
    private int[] open;
    // array to hold ids
    private final int[] ids;
    // length/width of the matrix
    private final int gridSize;
    // Quick Union data structure
    private final WeightedQuickUnionUF uf;
    // Keep track of open sites
    private int nOfOpen;


    // creates n-by-n grid, with all sites initially blocked
    //
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        nOfOpen = 0;
        gridSize = n;
        // +2 because of the 2 virtual nodes
        int idsize = (int) Math.pow(n, 2) + 2;
        open = new int[idsize];
        ids = new int[idsize];

        uf = new WeightedQuickUnionUF(idsize);

        for (int i = 0; i < idsize; i++) {
            open[i] = 0;

            ids[i] = i;

        }


    }

    // Convert Coordinates to sequence of IDs (coords start from 1,1)
    private int convert(int i, int j) {
        return ((i - 1) * gridSize + (j));
    }

    //check if coordinates are legal
    private void validate(int row, int col) {
        if (row < 1 || col > gridSize) {
            throw new IllegalArgumentException();
        }
    }


    // opens the site (row, col) if it is not open already
    // check if not open. If not open then check for adjacent opens, where there is adjacent open call union.
    public void open(int row, int col) {
        validate(row, col);
        // Only open if not already open
        if (!isOpen(row, col)) {
            // Corner case for when there is only 1 block, when n = 1
            if (gridSize == 1) {
                uf.union(ids[1], ids[0]);
                uf.union(ids[1], ids[(int) Math.pow(gridSize, 2) + 1]);
            }

            int firstOrLast = firstOrLastRow(row);

            int ufid = convert(row, col);
            // If its first row connect it to top virtual node
            if (firstOrLast == 1) {
                uf.union(ids[0], ids[ufid]);
            }
            // If its last row connect it to bottom virtual node
            if (firstOrLast == 2) {
                uf.union(ids[(int) Math.pow(gridSize, 2) + 1], ids[ufid]);

            }
            open[ufid] = 1;
            nOfOpen++;
            int[] coords = findAdj(row, col);
            int topCheck = convert(coords[0], coords[1]);
            int rightCheck = convert(coords[2], coords[3]);
            int botCheck = convert(coords[4], coords[5]);
            int leftCheck = convert(coords[6], coords[7]);
            // Check adjacent coordinates and if they are legal, check if they are open and if they are call union
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

                }
            }
            if ((coords[4] >= 1 && coords[4] <= gridSize) && (coords[5] >= 1
                    && coords[5] <= gridSize)) {
                if (open[botCheck] == 1) {
                    uf.union(ids[ufid], ids[botCheck]);

                }
            }
            if ((coords[6] >= 1 && coords[6] <= gridSize) && (coords[7] >= 1
                    && coords[7] <= gridSize)) {
                if (open[leftCheck] == 1) {
                    uf.union(ids[ufid], ids[leftCheck]);

                }
            }


        }
    }

    // Check if the row is in the first or last row
    private int firstOrLastRow(int row) {
        if (row == 1) {
            return 1;
        }
        if (row == gridSize) {
            return 2;
        } else return 0;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        int ufid = convert(row, col);
        return (open[ufid] != 0);
    }

    // return an array of adjacent coordinates, size = 8, top -> right -> bottom -> left, x and y
    private int[] findAdj(int row, int col) {

        return new int[]{row - 1, col, row, col + 1, row + 1, col, row, col - 1};
    }

    // is the site (row, col) full?
    // Is the site connected to the top virtual node
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

    // Percolates if top and bottom virtual node are connected
    public boolean percolates() {
        return uf.find(ids[0]) == uf.find(ids[(int) Math.pow(gridSize, 2) + 1]);

    }

    // Generate random coordinates
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
