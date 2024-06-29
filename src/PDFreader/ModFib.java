package PDFreader;

import java.io.*;
import java.util.*;

public class ModFib {
    public static void main(String[] args) {
        Scanner kScanner = new Scanner(System.in);
        System.err.println("term: ");
        int ai = kScanner.nextInt();
        System.err.println(ModFibno(ai));
    }

    public static int ModFibno(int ai) {
        int total = 0;
        if (ai == 0) {
            total = 3;
        } else if (ai == 1) {
            total = 5;
        } else if (ai == 2) {
            total = 8;
        } else {
            total = ModFibno(ai - 3) + ModFibno(ai - 2) + ModFibno(ai - 1);
        }
        return total;

    }

}
