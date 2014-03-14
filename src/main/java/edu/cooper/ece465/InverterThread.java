package edu.cooper.ece465;

public class InverterThread implements Runnable {
    private Matrix matrix;
    private int initialRow;
    private int dimension;
    private int pivot;
    private int executionMethod;
    private final int THREADS = 4;
    private final int UPPER_TRIANGULAR = 0;

    public InverterThread(Matrix matrix, int initialRow, int dimension, int pivot, int executionMethod) {
        this.matrix = matrix;
        this.initialRow = initialRow;
        this.dimension = dimension;
        this.pivot = pivot;
        this.executionMethod = executionMethod;
    }

    @Override
    public void run() {
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

