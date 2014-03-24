/**
 * MatrixWorkerWriter.java
 *    Checks the data buffer for inverted matrices and writes data back to client
 *    that requested the inversion.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 13 2014
 */

package edu.cooper.ece465;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MatrixWorkerWriter implements Runnable {

    private int numMats;
    private int matCount;
    private ObjectOutputStream output;
    private ArrayList<Matrix> matBuffer;
    private static Log LOG = LogFactory.getLog(MatrixWorkerWriter.class);

    public MatrixWorkerWriter(ObjectOutputStream o, int count, ArrayList<Matrix> matBuffer, int numMats) {
        LOG.info("New MatrixWorkerWriter created.");
        this.output = o;
        this.matCount  = count;
        this.matBuffer = matBuffer;
        this.numMats = numMats;
    }

    @Override
    public void run() {

        try {
            // Send back number of matrices
            output.writeObject(numMats);

            // Continue sending back data until all has been sent back
            for(int sentBack = 0; sentBack < matCount; sentBack++) {

                    // Check for data and send back if available
                if(!matBuffer.isEmpty()) {
                    Matrix sendBack = matBuffer.get(0);
                    matBuffer.remove(0);
                    output.writeObject(sendBack);
                    LOG.info("Sent back matrix" + (sentBack+1));
                } else {
                    Thread.sleep(1000);
                }
            }
            
        } catch (Exception e) {
            LOG.error("Unexpected exception", e);
        }

        // Sleep before exiting to ensure that all data returns to client
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            LOG.error("Thread.sleep(5000) failed.");
        }

    }
}
