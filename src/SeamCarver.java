import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private int[][] pictureArray;
    private int[][] energy;
//    private DecimalFormat df = new DecimalFormat("#.00");

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.pictureArray = new int[picture.width()][picture.height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                pictureArray[x][y] = picture.getRGB(x,y);
            }
        }
        fillEnergyMatrix();
    }

    /*public void printEnergyMatrix() {
        System.out.println("\nEnergy matrix: ");
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                System.out.print(df.format(energy(x,y)) + "  ");
            }
            System.out.println();
        }
    }*/

    private void fillEnergyMatrix() {
        this.energy = new int[width()][height()];
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
        int leftRgb = pictureArray[x-1][y];
        int rightRgb = pictureArray[x+1][y];
        int energyX = getEnergy(leftRgb, rightRgb);

        int aboveRgb = pictureArray[x][y - 1];
        int belowRgb = pictureArray[x][y + 1];
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
        Picture picture = new Picture(width(),height());
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                picture.setRGB(x,y,pictureArray[x][y]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return this.pictureArray.length;
    }

    // height of current picture
    public int height() {
        return this.pictureArray[0].length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException();
        }
        return Math.sqrt(energy[x][y]);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return shortestPath(transpose(this.energy));
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return shortestPath(this.energy);
    }

    private int[][] transpose(int[][] matrix) {
        int[][] result = new int[matrix[0].length][matrix.length];
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                result[y][x] = matrix[x][y];
            }
        }
        return result;
    }

    private int[] shortestPath(int[][] energy) {
//        System.out.println("Shortest path");
        int width = energy.length;
        int height = energy[0].length;
        double[][] distTo = new double[width][height];
        int[][] edgeTo = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0) {
                    distTo[x][y] = Math.sqrt(energy[x][y]);
                    edgeTo[x][y] = getId(x,y, width);
//                    System.out.print("("+getX(edgeTo[x][y], width) +","+ df.format(distTo[x][y])+")");
                } else {
                    double aboveDist = distTo[x][y-1] + Math.sqrt(energy[x][y]);
                    distTo[x][y] = aboveDist;
                    edgeTo[x][y] = getId(x, y-1, width);
                    double aboveLeftDist = x > 0 ? distTo[x-1][y-1] + Math.sqrt(energy[x][y]) : Double.MAX_VALUE;
                    if (aboveLeftDist < distTo[x][y]) {
                        distTo[x][y] = aboveLeftDist;
                        edgeTo[x][y] = getId(x-1, y-1, width);
                    }
                    double aboveRightDist = x < width - 2 ? distTo[x+1][y-1] + Math.sqrt(energy[x][y]) : Double.MAX_VALUE;
                    if (aboveRightDist < distTo[x][y]) {
                        distTo[x][y] = aboveRightDist;
                        edgeTo[x][y] = getId(x+1, y-1, width);
                    }
//                    System.out.print("("+getX(edgeTo[x][y], width) +","+ df.format(distTo[x][y])+")");
                }

            }
//            System.out.println();
        }
        double shortestPath = Double.MAX_VALUE;
        Integer shortestDestId = null;
        for (int x = 0; x < width; x++) {
            if (distTo[x][height-1] < shortestPath) {
                shortestPath = distTo[x][height-1];
                shortestDestId = getId(x, height-1, width);
            }
        }
//        System.out.println("Total energy: " + shortestPath);

        int[] result = new int[height];
        int lastId = shortestDestId;
        for (int i = height-1; i > -1 ; i--) {
            int x = getX(lastId, width);
            int y = getY(lastId, width);
            result[i] = x;
            lastId = edgeTo[x][y];
        }
        return result;
    }

    private int getId(int x, int y, int width) {
        return y * width + x;
    }

    private int getX(int id, int width) {
        return id % width;
    }

    private int getY(int id, int width) {
        return id / width;
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
                if ( i > previous+1 || i < previous-1) {
                    throw new IllegalArgumentException();
                }
            }
            previous = i;
        }
        int[][] seamRemovedPicture = new int[width()][height()-1];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (y < seam[x]) {
                    seamRemovedPicture[x][y] = pictureArray[x][y];
                } else if (y > seam[x]) {
                    seamRemovedPicture[x][y-1] = pictureArray[x][y];
                }
            }
        }
        pictureArray = seamRemovedPicture;
        fillEnergyMatrix();
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
                if ( i > previous+1 || i < previous-1) {
                    throw new IllegalArgumentException();
                }
            }
            previous = i;
        }

        int[][] seamRemovedPicture = new int[width()-1][height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (x < seam[y]) {
                    seamRemovedPicture[x][y] = pictureArray[x][y];
                } else if (x > seam[y]) {
                    seamRemovedPicture[x-1][y] = pictureArray[x][y];
                }
            }
        }
        pictureArray = seamRemovedPicture;
        fillEnergyMatrix();
    }
}
