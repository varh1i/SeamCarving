import edu.princeton.cs.algs4.Picture;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Picture picture = new Picture(new File("resources/7x10.png"));
        SeamCarver seamCarver = new SeamCarver(picture);
        /*seamCarver.printEnergyMatrix();

        int[] seamToRemove = seamCarver.findHorizontalSeam();
        printSeam(seamToRemove);
        System.out.println("Remove");
        seamCarver.removeHorizontalSeam(seamToRemove);
        seamCarver.printEnergyMatrix();

        seamToRemove = seamCarver.findVerticalSeam();
        printSeam(seamToRemove);
        System.out.println("Remove");
        seamCarver.removeVerticalSeam(seamToRemove);
        seamCarver.printEnergyMatrix();*/

        Picture picture1 = seamCarver.picture();

        /*System.out.println("Horizontal");
        int[] horizontalSeam = seamCarver.findHorizontalSeam();
        for (int i : horizontalSeam) {
            System.out.println(i);
        }*/


    }

    private static void printSeam(int[] seam) {
        for (int i : seam) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
