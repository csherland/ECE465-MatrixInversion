/**
 * MatrixServerWorker.java
 *    A Worker thread for the MatrixServer. Listens for new data from client and
 *    sends back inverted matrices simultaneously. Spawns a new MatrixInverter
 *    thread for each matrix to be inverted and controls threads with a thread pool.
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

public class MatrixServerWorker implements Runnable {

    private Socket socket;
    private static Log LOG = LogFactory.getLog(MatrixServerWorker.class);

    public MatrixServerWorker(Socket s) {
        LOG.info("New MatrixServerWorker created.");
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            // Streams to and from client
            ObjectInputStream input   = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            LOG.info("Reading data from client.");

            // Determine number of images to accept
            int matrixCount = (Integer) input.readObject();
            LOG.info("Expecting " + matrixCount + " images from client.");

            // Synchronized buffer to store data in
            ArrayList<Matrix> matBuffer = new ArrayList<Matrix>();
            Collections.synchronizedList(matBuffer);

            // Keep track of threads
            ArrayList<Thread> threads = new ArrayList<Thread>();

            // Make the send back thread
            MatrixWorkerWriter writer = new MatrixWorkerWriter(output, matrixCount, matBuffer);
            Thread writerThread = new Thread(writer);
            threads.add(writerThread);
            writerThread.start();

            // Read in all expected images
            for (int i = 0; i < matrixCount; i++) {
                Matrix receivedMatrix = (Matrix) input.readObject();
                LOG.info("Received matrix " + (i+1) + " from client.");
                Runnable inverterThread = new MatrixWorkerInverter(receivedMatrix, matBuffer);
                threads.add(new Thread(inverterThread));
                threads.get(i).start();
            }

            // Wait for everything to finish before ending communication
            for (Thread thread : threads) {
                thread.join();
            }
            writerThread.join();

            socket.close();
            LOG.info("Finished equalizing images.");
        } catch (IOException e) {
            LOG.error("IO Exception", e);
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found error", e);
        } catch (Exception e) {
            LOG.error("Unexpected exception:", e);
        }
    }
}
