import edu.princeton.cs.algs4.Picture;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Picture picture = new Picture(new File("resources/12x10.png"));
        SeamCarver seamCarver = new SeamCarver(picture);

        System.out.println("Remove seam");
        int[] seamToRemove = new int[12];
        for (int i = 0; i < 12; i++) {
            seamToRemove[i] = 0;
        }
        seamCarver.removeHorizontalSeam(seamToRemove);
//        seamCarver.printEnergyMatrix();

        /*int[] verticalSeam = seamCarver.findVerticalSeam();
        System.out.println("Vertical");
        for (int i : verticalSeam) {
            System.out.println(i);
        }

        System.out.println("Horizontal");
        int[] horizontalSeam = seamCarver.findHorizontalSeam();
        for (int i : horizontalSeam) {
            System.out.println(i);
        }*/


    }
}
