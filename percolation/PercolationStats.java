/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONSTANT = 1.96;
    private final double[] percResults;
    private final int numbOfTrials;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        numbOfTrials = trials;
        percResults = new double[trials];


        while (trials != 0) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                // System.out.println("Percolates or not: " + p.percolates());
                int[] xAndY = new int[2];
                xAndY[0] = StdRandom.uniform(1, n + 1);
                xAndY[1] = StdRandom.uniform(1, n + 1);

                // System.out.println("temp numbers: " + temp.get(0) + "," + temp.get(1));

                p.open(xAndY[0], xAndY[1]);

            }
            //   System.out.println("OPEN SIIITES: " + p.numberOfOpenSites());
            trials--;
            percResults[trials] = p.numberOfOpenSites() / Math.pow(n, 2);
        }
        //   System.out.println(Arrays.toString(percResults));

    }

    // sample mean of percolation threshold
    public double mean() {

        return StdStats.mean(percResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {

        return StdStats.stddev(percResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {

        return mean() - (CONSTANT * stddev() / Math.sqrt(numbOfTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONSTANT * stddev() / Math.sqrt(numbOfTrials));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(n, t);
        System.out.println("Mean:  " + " " + p.mean());
        System.out.println("stddev: " + " " + p.stddev());
        System.out.println(
                "Confidence interval: [" + p.confidenceLo() + ", " + p.confidenceHi() + "]");

    }
}
