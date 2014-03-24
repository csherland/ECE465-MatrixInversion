/**
 * MatrixClientWriter.java
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MatrixClientWriter implements Runnable {

    private Socket socket;
    private String inFile;
    private static Log LOG = LogFactory.getLog(MatrixClientWriter.class);

    public MatrixClientWriter(Socket s, String inputFile) {
        LOG.info("Constructing new MatrixClientWriter");
        this.socket = s;
        this.inFile = inputFile;
    }

    @Override
    public void run() {
        try {
            // Read the number of matrices to be equalized and send to server
            File file = new File(inFile);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            // Open input file and stream to server
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            LOG.info("Writing matrices to server.");

            // Read the rest of the lines
            while ((line = br.readLine()) != null) {
                // Get matrix data
                String[] data = line.split(" ");
                int[] matrixData = new int[data.length];
                for (int i = 0; i < data.length; i++) {
                    matrixData[i] = Integer.parseInt(data[i]);
                }

                // Send matrix to server
                output.writeObject(matrixData);
                LOG.info("Sent matrix " + i + " of " + line + " to server.");
            }

            br.close();
            LOG.info("Successfully sent all matrices to server.");

        } catch (IOException e) {
            LOG.fatal("IO exception", e);
            System.exit(1);
        } catch (Exception e) {
            LOG.fatal("Unexpected exception", e);
            System.exit(1);
        }
    }
}
