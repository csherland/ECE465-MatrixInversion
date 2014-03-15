/**
 * InverterThread.java
 *    One of the threads responsible for inverting a specified matrix. Accomplishes
 *    inversion through Gauss-Jordan elimination.
 *
 * @author Michael Scibor
 * @author Christian Sherland
 * @author Ethan Lusterman
 *
 * @version 1.0 Mar 14 2014
 */

package edu.cooper.ece465;

public class InverterThread implements Runnable {

    private final int THREADS = 4;
    private final int UPPER_TRIANGULAR = 0;
    private Matrix matrix;
    private int initialRow;
    private int dimension;
    private int pivot;
    private int executionMethod;

    public InverterThread(Matrix matrix, int initialRow, int dimension, int pivot, int executionMethod) {
        this.pivot  = pivot;
        this.matrix = matrix;
        this.dimension  = dimension;
        this.initialRow = initialRow;
        this.executionMethod = executionMethod;
    }

    @Override
    public void run() {

        // Accomplishes reduction to upper triangular or lower triangluar based upon input
        if (executionMethod == UPPER_TRIANGULAR) {            
            for (int row = initialRow; row < dimension; row += THREADS) {
                double leadValue = matrix.retrieve(row, pivot);
                if (leadValue != 0) {
                    matrix.addRow(row, pivot, (leadValue * -1));
                }
            }

        } else {
            for (int row = initialRow; row >= 0; row -= THREADS) {
                double tailValue = matrix.retrieve(row, pivot);
                if (tailValue != 0) {
                    matrix.addRow(row, pivot, (tailValue * -1));
                }
            }
        }
    }
}
