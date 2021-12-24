package heatmaps;

import org.jtransforms.fft.DoubleFFT_2D;

public class FFTHelper {

    private static final double EPSILON = 1e-10;

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
                double val = complexOutput[i + dx][(j + dy) * 2];

                if (Math.abs(val) < EPSILON)
                    val = 0;
                if (val < 0)
                    System.err.println("Illegal negative value in FFT result " + val);

                realOutput[i][j] = val;
                if (Math.abs(complexOutput[i + dx][(j + dy) * 2 + 1]) > EPSILON) {
                    System.err.println("Large imaginary part in FFT");
                }
            }
        }
        return realOutput;
    }

    public static double[][] convolveLikeASanePerson(double[][] data, double[][] kernel) {
        double[][] result =  new double[data.length][data[0].length];
        if (kernel.length % 2 == 0 || kernel[0].length % 2 == 0)
            System.err.println("Kernel not of odd width");
        int xCenter = kernel.length / 2;
        int zCenter = kernel[0].length / 2;
        for (int dRow = 0; dRow < data.length; dRow++) {
            for (int dCol = 0; dCol < data[0].length; dCol++) {
                for (int kRow = 0; kRow < kernel.length; kRow++) {
                    for (int kCol = 0; kCol < kernel[0].length; kCol++) {
                        int x = dRow + kRow - xCenter;
                        int z = dCol + kCol - zCenter;
                        if (x >= 0 && x < data.length && z >= 0 && z < data[0].length)
                            result[x][z] += kernel[kRow][kCol] * data[dRow][dCol];
                    }
                }
            }
        }
        return result;
    }
}
