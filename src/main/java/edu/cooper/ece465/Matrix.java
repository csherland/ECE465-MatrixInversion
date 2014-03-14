/**
 * Matrix: a serializable int[] array with specified dimensions.
 *
 *  @author Christian Sherland
 *  @author Michael Scibor
 *  @author Ethan Lusterman
 *
 *  @version 1.0 Mar 13 2014
 */

package edu.cooper.ece465;

import java.io.Serializable;

public class Matrix implements Serializable {
    private int dimension;
    private double matrix[][];

    public Matrix(int data[]) {
        if (data.length > 0 && (data.length - 1) == data[0] * data[0]) {
            this.dimension = data[0];
            this.matrix = new double[dimension][2 * dimension];

            // Create the LEFT SECTION. Matrix to be inverted.
            for (int row = 0; row < dimension; row++) {
                for (int column = 0; column < dimension; column++) {
                    matrix[row][column] = data[((row * dimension) + column) + 1];
                }
            }

            // Create the RIGHT SECTION. Identity matrix augmented to our matrix to be inverted.
            for (int row = 0; row < dimension; row++) {
                for (int column = dimension; column < 2 * dimension; column++) {
                    if (row == (column - dimension)) {
                        matrix[row][column] = 1;
                    } else {
                        matrix[row][column] = 0;
                    }
                }
            }
        } else {
            //LOG.error("Invalid data input.");
        }
    }

    public double retrieve(int row, int column) {
        return matrix[row][column];
    }

    // Swap two rows in the matrix
    public void swapRow(int firstRow, int secondRow) {
        double tempRow[] = matrix[firstRow];
        matrix[firstRow] = matrix[secondRow];
        matrix[secondRow] = tempRow;
    }

    // Scale a row in the matrix
    public void scaleRow(int row, double scalar) {
        for (int i = 0; i < 2 * dimension; i++) {
            matrix[row][i] *= scalar;
        }
    }

    // Add multiple of one row to another row in the matrix. firstRow = firstRow + secondRow * scalar.
    public void addRow(int firstRow, int secondRow, double scalar) {
        for (int i = 0; i < 2 * dimension; i++) {
            matrix[firstRow][i] += (matrix[secondRow][i] * scalar);
        }
    }
}
