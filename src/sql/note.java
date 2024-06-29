package sql;

import java.util.Arrays;

class note {
    public static int a[][] = {
            { -1, 3 },
            { 0, 9 },
            { 1, -11 },
            { 4, -5 }
    };
    public static int b[][] = {
            { 1, 2, -2, 0 },
            { -3, 4, 7, 2 },
            { 6, 0, 3, 1 }
    };

    public static void malt(int aa[][], int bb[][]) {
        int row = aa.length;
        int colume = bb[0].length;

        int result[][] = new int[row][colume];
        int total = 0;
        for (int k = 0; k <= bb[0].length - 1; k++) {
            for (int j = 0; j <= aa.length - 1; j++) {
                for (int i = 0; i <= bb.length - 1; i++) {
                    int temp = aa[j][i];
                    int temp2 = bb[i][k];
                    int re = temp * temp2;
                    total += re;
                }
                result[j][k] = total;
                total = 0;
                System.err.println(result[j][k]);
            }
        }
        for (int[] ints : result) {
            System.out.println(Arrays.toString(ints));
        }
    }

    public static void main(String[] args) {
        malt(b, a);
    }
}
