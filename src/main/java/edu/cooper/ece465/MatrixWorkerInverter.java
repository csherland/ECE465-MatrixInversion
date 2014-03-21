/**
 * MatrixWorkerInverter.java
 *    A child thread of the MatrixServerWorker that actually performs the matrix
 *    inversion. Does so through a parallel algorithm which spawns at most 4
 *    new threads for matrix inversion. Stores the resulting inverted matrix in
 *    the outputBuffer so that it can be sent back to the client.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 13 2014
 */

package edu.cooper.ece465;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;

public class MatrixWorkerInverter implements Runnable {

    private Matrix matrix;
    private int dimension;
    private ArrayList<Matrix> matBuffer;
    private static final int THREADS = 4;
    private static final int UPPER_TRIANGULAR = 0;
    private static final int GAUSS_JORDAN_ELIMINATE = 1;
    private static Log LOG = LogFactory.getLog(MatrixWorkerInverter.class);

    public MatrixWorkerInverter(Matrix matrix, ArrayList<Matrix> matBuffer) {
        LOG.info("New MatrixWorkerInverter created.");
        this.matrix = matrix;
        this.dimension = matrix.getDimension();
        this.matBuffer = matBuffer;
    }

    /**
     * Waits for all threads specified in listThreads to join this process before
     * continuing execution.
     *
     * @param listThreads    The list of threads to join with this process
     */
    private void joinThreads(ArrayList<Thread> listThreads) {
        for (Thread thread : listThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOG.error("Error joining thread.", e);
            }
        }
    }

    @Override
    public void run() {

        // Make the matrix upper triangular
        for (int pivot = 0; pivot < dimension; pivot++) {
            double pivotValue = matrix.getElement(pivot, pivot);
            int swappableRow  = 1 + pivot;

            // Make sure the pivotValue is non-zero, swap rows until this is the case
            while (pivotValue == 0) {
                matrix.swapRow(pivot, swappableRow);
                pivotValue = matrix.getElement(pivot, pivot);
                swappableRow++;
            }

            // Scale row by 1 / pivotValue so first element is a 1
            if (pivotValue != 1) {
                matrix.scaleRow(pivot, (1 / pivotValue));
            }

            // If matrix is small, don't create a lot of threads
            int innerBlockDim = dimension - (pivot + 1);
            int numberThreads = (innerBlockDim < THREADS) ? THREADS : innerBlockDim;

            ArrayList<Thread> listThreads = new ArrayList<Thread>();
            for (int threadNumber = 0; threadNumber < numberThreads; threadNumber++) {
                Runnable reducer = new InverterThread(matrix, (pivot + 1 + threadNumber), dimension, pivot, UPPER_TRIANGULAR);
                Thread currentReducer = new Thread(reducer);
                listThreads.add(currentReducer);
                currentReducer.start();
            }

            joinThreads(listThreads); // Wait for all threads to complete
        }

        // Make the upper triangular matrix lower triangular (diagonal)
        for (int pivot = dimension - 1; pivot > 0; pivot--) {
            // This time we know the pivot values as we have defined them are 1, can skip some steps.
            int innerBlockDim = pivot;
            int numberThreads = (innerBlockDim < THREADS) ? THREADS : innerBlockDim;

            // Perform diagonalization
            ArrayList<Thread> listThreads = new ArrayList<Thread>();
            for (int threadNumber = 0; threadNumber < numberThreads; threadNumber++) {
                Runnable reducer = new InverterThread(matrix, (pivot - 1 - threadNumber), dimension, pivot, GAUSS_JORDAN_ELIMINATE);
                Thread currentReducer = new Thread(reducer);
                listThreads.add(currentReducer);
                currentReducer.start();
            }

            joinThreads(listThreads); // Wait for all threads to complete

            matBuffer.add(matrix);
        }
    }
}
