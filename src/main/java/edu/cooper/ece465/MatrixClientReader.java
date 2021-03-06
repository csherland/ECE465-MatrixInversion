/**
 * MatrixClientReader.java
 *  Sends invertible matrices specified in the input file command line argument
 *  to a server and waits to read back inverted results. Saves results to the
 *  output file specified as a command line argument.
 *
 * @author Christian Sherland
 * @author Michael Scibor
 * @author Ethan Lusterman
 *
 * @version 1.0 Mar 13 2014
 */

package edu.cooper.ece465;

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MatrixClientReader implements Runnable {

    private Socket socket;
    private String outFile;
    private static Log LOG = LogFactory.getLog(MatrixClientReader.class);

    public MatrixClientReader(Socket s, String outputFile) {
        LOG.info("Constructing new MatrixClientReader.");
        this.socket  = s;
        this.outFile = outputFile;
    }

    @Override
    public void run() {
        try {
            // Create output file
            File file = new File(outFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

            // Wait for inverted matrices from Server
            LOG.info("Waiting for inverted matrices.");
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            int numMats = (Integer) input.readObject();
            bw.write(numMats);


            for (int i = 0; i < numMats; i++) {
                // Get result and write output
                Matrix invertedMat = (Matrix) input.readObject();
                double[] mat = invertedMat.getMatrixArray();

                // Write results to output file
                bw.write(Arrays.toString(mat));
                LOG.info("Wrote matrix " + i + " of " + numMats + " matrices to " + outFile);
            }

            bw.close();
            LOG.info("Successfully wrote all matrices to " + outFile);

        } catch (IOException e) {
            LOG.fatal("IO exception", e);
            System.exit(1);
        } catch (Exception e) {
            LOG.fatal("Unexpected exception", e);
            System.exit(1);
        }
    }
}
