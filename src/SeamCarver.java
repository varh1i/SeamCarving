import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Point2D;

import java.text.DecimalFormat;

public class SeamCarver {

    private static DecimalFormat df2 = new DecimalFormat(".##");
    private final Picture picture;
    int[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
        this.energy = new int[width()][height()];

        fillEnergyMatrix();
        printEnergyMatrix();
    }

    private void printEnergyMatrix() {
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                System.out.print(energy(x,y) + "  ");
            }
            System.out.println();
        }
    }

    private void fillEnergyMatrix() {
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (x == 0 || x == width()-1 || y == 0 || y == height()-1) {
                    energy[x][y] = 1000000;
                } else {
                    energy[x][y] = energyInt(x,y);
                }
            }
        }
    }

    private int energyInt(int x, int y) {
        int leftRgb = picture.getRGB(x-1, y);
        int rightRgb = picture.getRGB(x+1, y);
        int energyX = getEnergy(leftRgb, rightRgb);

        int aboveRgb = picture.getRGB(x, y - 1);
        int belowRgb = picture.getRGB(x, y + 1);
        int energyY = getEnergy(aboveRgb, belowRgb);

        return energyX + energyY;
    }


    private int getEnergy(int leftRgb, int rightRgb) {
        int rLeft = (leftRgb >> 16) & 0xFF;
        int rRight = (rightRgb >> 16) & 0xFF;
        int rX = rLeft - rRight;

        int gLeft = (leftRgb >> 8) & 0xFF;
        int gRight = (rightRgb >> 8) & 0xFF;
        int gX = gLeft - gRight;

        int bLeft = leftRgb & 0xFF;
        int bRight = rightRgb & 0xFF;
        int bX = bLeft - bRight;

        return rX * rX + gX * gX + bX * bX;
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException();
        }
        return Math.sqrt(energy[x][y]);
    }

    private int getId(int x, int y) {
        return y * width() + x;
    }

    private int getX(int id) {
        return id % width();
    }

    private int getY(int id) {
        return id / width();
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (y == 0) {
                    distTo[x][y] = energy(x,y);
                    edgeTo[x][y] = getId(x,y);
                } else {
                    double aboveDist = distTo[x][y-1] + energy(x,y);
                    distTo[x][y] = aboveDist;
                    edgeTo[x][y] = getId(x, y-1);
                    double aboveLeftDist = x > 0 ? distTo[x-1][y-1] + energy(x,y) : Double.MAX_VALUE;
                    if (aboveLeftDist < distTo[x][y]) {
                        distTo[x][y] = aboveLeftDist;
                        edgeTo[x][y] = getId(x-1, y-1);
                    }
                    double aboveRightDist = x < width() - 2 ? distTo[x+1][y-1] + energy(x,y) : Double.MAX_VALUE;
                    if (aboveRightDist < distTo[x][y]) {
                        distTo[x][y] = aboveRightDist;
                        edgeTo[x][y] = getId(x+1, y-1);
                    }
                    System.out.print("("+getX(edgeTo[x][y]) +","+ df2.format(distTo[x][y])+")");
                }

            }
            System.out.println();
        }
        double shortestPath = Double.MAX_VALUE;
        Integer shortestDestId = null;
        for (int x = 0; x < width(); x++) {
            if (distTo[x][height()-1] < shortestPath) {
                shortestPath = distTo[x][height()-1];
                shortestDestId = getId(x, height()-1);
            }
        }

        int[] result = new int[height()];
        int lastId = shortestDestId;
        for (int i = height()-1; i > -1 ; i--) {
            int x = getX(lastId);
            int y = getY(lastId);
            result[i] = x;
            lastId = edgeTo[x][y];
        }
        return result;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException();
        }
        if (height() <= 1) {
            throw new IllegalArgumentException();
        }
        Integer previous = null;
        for (int i : seam) {
            if ( i < 0 || i >= height()) {
                throw new IllegalArgumentException();
            }
            if (previous != null) {
                if ( i <= previous+1 || i >= previous-1) {
                    throw new IllegalArgumentException();
                }
            }
            previous = i;
        }

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }
        if (width() <= 1) {
            throw new IllegalArgumentException();
        }
        Integer previous = null;
        for (int i : seam) {
            if ( i < 0 || i >= width()) {
                throw new IllegalArgumentException();
            }
            if (previous != null) {
                if ( i <= previous+1 || i >= previous-1) {
                    throw new IllegalArgumentException();
                }
            }
            previous = i;
        }
    }
}
