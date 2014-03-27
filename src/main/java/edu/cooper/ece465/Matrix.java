/**
 * Matrix.java:
 *    A serializable 2-dim array with specified dimension. Implements several
 *    methods useful for matrix inversion also. Only valid for square matrices
 *    (as this is all we care about for matrix inversion).
 *
 *    Class also assumes that the matrix is invertible (i.e. has a non-zero determinant)
 *
 *  @author Christian Sherland
 *  @author Michael Scibor
 *  @author Ethan Lusterman
 *
 *  @version 1.0 Mar 13 2014
 */

package edu.cooper.ece465;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Matrix implements Serializable {

    private int dimension;
    private int matrixIDNumber;
    private double matrix[][];
    private static Log LOG = LogFactory.getLog(LoadBalancer.class);

    public Matrix(int data[]) {
        // Check that the data is of the correct format
        if (data.length > 0 && (data.length - 2) == data[1] * data[1]) {
            this.matrixIDNumber = data[0];
            this.dimension = data[1];
            this.matrix = new double[dimension][2 * dimension];

            // Create the LEFT SECTION. Matrix to be inverted.
            for (int row = 0; row < dimension; row++) {
                for (int column = 0; column < dimension; column++) {
                    matrix[row][column] = data[((row * dimension) + column) + 2];
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
            LOG.error("Invalid input data");
        }
    }

    /**
     * Returns the element in the matrix at the requested row and column. If the
     * specified indicies are out of the bounds of the matrix then this method
     * throws an IndexOutOfBounds exception.
     *
     * @param row    The row of the requested element
     * @param col    The column of the requested element
     * @return       The element in the specified row and column
     */
    public double getElement(int row, int col) {
        if ((row > dimension) || (col > dimension)) {
            throw new IndexOutOfBoundsException("Error retriving element.");
        }

        return matrix[row][col];
    }

    /**
     * @returns     The dimension property of the matrix object
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * @return    The matrix identification number
     */
    public int getID() {
        return matrixIDNumber;
    }

    /**
     * Converts the matrix in this class back to the array input form expected
     * in the class constructor and returns that.
     *
     * @return    The matrix contained in this object as an array
     */
     public double[] getMatrixArray() {
        double[] matArray = new double[dimension*dimension+2];

        matArray[0] = matrixIDNumber;
        matArray[1] = dimension;
        
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                matArray[i+j+2] = matrix[i][j];
            }
        }

         return matArray;
     }

    /**
     * Swaps the rows specified by firstRow and secondRow. Throws an IndexOutOfBounds
     * exception if either firstRow or secondRow is not within the bounds of the
     * matrix
     *
     * @param firstRow     The first row to swap
     * @param secondRow    The second row to swap
     */
    public void swapRow(int firstRow, int secondRow) {
        if ((firstRow >= dimension) || (secondRow >= dimension)) {
            throw new IndexOutOfBoundsException("Error swapping rows.");
        }

        double tempRow[] = matrix[firstRow];
        matrix[firstRow] = matrix[secondRow];
        matrix[secondRow] = tempRow;
    }

    /**
     * Scales the specified row of the matrix by the ammount specified in scale.
     * Throws an IndexOutOfBounds exception if row is greater than dimension.
     *
     * @param row        The index of the row to be scaled
     * @param scale      The ammount by which to scale the row
     */
    public void scaleRow(int row, double scale) {
        if (row >= dimension) {
            throw new IndexOutOfBoundsException("Error scaling row");
        }

        for (int i = 0; i < 2 * dimension; i++) {
            matrix[row][i] *= scale;
        }
    }

    /**
     * Adds a multiple of one row of the matrix to another row of the matrix.
     * Throws IndexOutOfBounds exception if either row is not within the dimensions
     * of the matrix.
     *
     * @param firstRow     The row that will be modified by addition
     * @param secondRow    The row to be added to the first row
     * @param scale        The ammount by which to scale the second row before adding
     */
    public void addRow(int firstRow, int secondRow, double scale) {
        if ((firstRow >= dimension) || (secondRow >= dimension)) {
            throw new IndexOutOfBoundsException("Error adding row");
        }

        for (int i = 0; i < 2 * dimension; i++) {
            matrix[firstRow][i] += (matrix[secondRow][i] * scale);
        }
    }
}
