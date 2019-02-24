import edu.princeton.cs.algs4.Picture;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Picture picture = new Picture(new File("resources/5x6.png"));
        SeamCarver seamCarver = new SeamCarver(picture);
        int[] verticalSeam = seamCarver.findHorizontalSeam();
        for (int i : verticalSeam) {
            System.out.println(i);
        }
    }
}
