package heatmaps;

import org.jtransforms.fft.DoubleFFT_2D;

public class FFTHelper {

    /**
     * Convert an array of real numbers to an array of complex numbers in the form wanted by this fft library
     * @param realArray the matrix to convert
     * @return a complex matrix
     */
    private static double[][] realToImArray(double[][] realArray, int rows, int cols) {
        double[][] output = new double[rows][cols * 2];
        for (int i = 0; i < realArray.length; i++) {
            for (int j = 0; j < realArray[0].length; j++) {
                output[i][2 * j] = realArray[i][j];
            }
        }
        return output;
    }

    private static double[][] pointwiseMultiplyImArrays(double[][] a, double[][] b) {
        double[][] output = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length / 2; j++) {
                //(x + iy)(z + iw) = (xz - yw) + i(xw + yz)
                output[i][2 * j] = a[i][2 * j] * b[i][2 * j] - a[i][2 * j + 1] * b[i][2 * j + 1];
                output[i][2 * j + 1] = a[i][2 * j] * b[i][2 * j + 1] + a[i][2 * j + 1] * b[i][2 * j];
            }
        }
        return output;
    }

    public static double[][] convolve(double[][] data, double[][] kernel, boolean centerKernel) {

        int paddedRows = data.length + kernel.length - 1;
        int paddedCols = data[0].length + kernel[0].length - 1;
        double[][] d = realToImArray(data, paddedRows, paddedCols);
        double[][] k = realToImArray(kernel, paddedRows, paddedCols);
        DoubleFFT_2D transformer = new DoubleFFT_2D(paddedRows, paddedCols);

        transformer.complexForward(d);
        transformer.complexForward(k);

        double[][] complexOutput = pointwiseMultiplyImArrays(d,k);

        transformer.complexInverse(complexOutput, true);

        double[][] realOutput = new double[data.length][data[0].length];

        int dx = 0;
        int dy = 0;
        if (centerKernel) {
            dx = kernel.length / 2;
            dy = kernel[0].length / 2;
        }

        for (int i = 0; i < realOutput.length; i++) {
            for (int j = 0; j < realOutput[0].length; j += 1) {
                realOutput[i][j] = complexOutput[i + dx][(j + dy) * 2];
                if (Math.abs(complexOutput[i + dx][(j + dy) * 2 + 1]) > 0.00000000000001D) {
                    System.err.println("uh oh");
                }
            }
        }
        System.out.println("done");
        return realOutput;
    }
}
