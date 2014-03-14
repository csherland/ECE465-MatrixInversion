/**
 * MatrixWorkerInverter.java
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
    private static final int THREADS = 4;
    private static Log LOG = LogFactory.getLog(MatrixWorkerInverter.class);
    private final int UPPER_TRIANGULAR = 0;
    private final int GAUSS_JORDAN_ELIMINATE = 1;

    public MatrixWorkerInverter(Matrix matrix, int dimension) {
        LOG.info("New MatrixWorkerInverter created.");
        this.matrix = matrix;
        this.dimension = dimension;
    }

    @Override
    public void run() {
        // TODO: If number of dimensions is less than number of THREAD

        for (int pivot = 0; pivot < dimension - 1; pivot++) {
            double pivotValue = matrix.retrieve(pivot, pivot);
            int swappableRow = 1 + pivot;

            // Make sure the pivotValue is non-zero, swap rows until this is the case
            while (pivotValue == 0) {
                matrix.swapRow(pivot, swappableRow);
                pivotValue = matrix.retrieve(pivot, pivot);
                swappableRow++;
            }

            // Scale row by 1 / pivotValue so first element is a 1
            if (pivotValue != 1) {
                matrix.scaleRow(pivot, (1 / pivotValue));
            }

            int innerBlockDimension = dimension - (pivot + 1);
            int numberThreads = THREADS; // If small sub-matrix, don't create all threads

            if (innerBlockDimension < THREADS) {
                numberThreads = innerBlockDimension;
            }

            ArrayList<Thread> listThreads = new ArrayList<Thread>();
            for (int threadNumber = 0; threadNumber < numberThreads; threadNumber++) {
                Runnable reducer = new InverterThread(matrix, (pivot + 1 + threadNumber), dimension, pivot, UPPER_TRIANGULAR);
                Thread currentReducer = new Thread(reducer);
                listThreads.add(currentReducer);
                currentReducer.start();
            }

            for (Thread thread: listThreads){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // At this point we have an upper triangular matrix.

        for (int pivot = dimension - 1; pivot > 0; pivot--) {
            // This time we know the pivot values as we have defined them are 1, can skip some steps.

            int innerBlockDimension = pivot;
            int numberThreads = THREADS; // If small sub-matrix, don't create all threads

            if (innerBlockDimension < THREADS) {
                numberThreads = innerBlockDimension;
            }

            ArrayList<Thread> listThreads = new ArrayList<Thread>();
            for (int threadNumber = 0; threadNumber < numberThreads; threadNumber++) {
                Runnable reducer = new InverterThread(matrix, (pivot - 1 - threadNumber), dimension, pivot, GAUSS_JORDAN_ELIMINATE);
                Thread currentReducer = new Thread(reducer);
                listThreads.add(currentReducer);
                currentReducer.start();
            }

            for (Thread thread: listThreads){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
