package me.chriss99.math;

import java.util.Arrays;

public class GFG {
    public static float[] calcConvolution(float[] a, float[] b) {
        float[] c = new float[(a.length + b.length - 1)];

        for (int i = 0; i < a.length; ++i)
            for (int j = 0; j < b.length; ++j)
                c[i + j] += a[i] * b[j];

        return c;
    }

    public static float[] calcAveraged(float[] a, int size) {
        float multiplier = 1f/((float) size);
        float[] b = new float[size];
        Arrays.fill(b, multiplier);

        float[] averaged = new float[(a.length + b.length - 1)];
        int[] counter = new int[averaged.length];

        for (int i = 0; i < a.length; ++i)
            for (int j = 0; j < b.length; ++j) {
                averaged[i + j] += a[i] * b[j];
                counter[i + j]++;
            }

        for (int i = 0; i < averaged.length; i++)
            averaged[i] = averaged[i] * (((float) size) /((float) counter[i]));

        return averaged;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(calcConvolution(new float[]{1, 2, 3}, new float[]{4, 5, 6})));
    }
}