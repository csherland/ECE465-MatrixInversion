/**
 * MatrixClient.java
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

public class MatrixClient {

    private static Log LOG = LogFactory.getLog(MatrixClient.class);

    public static void main(String[] args){

        final String INPUT_FILE  = args[2];
        final String OUTPUT_FILE = args[3];
        final String LOAD_BALANCER_NAME = args[0];
        final int LOAD_BALANCER_PORT    = Integer.parseInt(args[1]);

        LOG.info("Image client running with load balancer name:\t "
                + LOAD_BALANCER_NAME + "\n\t\t port: "
                + LOAD_BALANCER_PORT + "\n\t\t input file: "
                + INPUT_FILE + "\n\t\t output file: "
                + OUTPUT_FILE);


        // Get server info from the load balancer
        String histServerName  = null;
        Integer histServerPort = null;

        try {
            LOG.info("Getting server assignment from load balancer.");
            Socket socket = new Socket(LOAD_BALANCER_NAME, LOAD_BALANCER_PORT);
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            // Receive assignment
            histServerName = (String) input.readObject();
            histServerPort = (Integer) input.readObject();

            input.close();
            socket.close();
        } catch (IOException e) {
            LOG.fatal("Socket failed to initialize.");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            LOG.fatal("ClassNotFoundException.", e);
            System.exit(1);
        }

        // Connect to the server and have matrices equalized
        try {
            LOG.info("Connecting to histogram server: " + histServerName + " on port: " + histServerPort);
            Socket socket = new Socket(histServerName, histServerPort);

            // Start reader and writer
            Thread matWriter = new Thread(new MatrixClientWriter(socket, INPUT_DIRECTORY));
            Thread matReader = new Thread(new MatrixClientReader(socket, OUTPUT_DIRECTORY, numImgs));
            matWriter.start();
            matReader.start();

            // Wait for both to finish
            matWriter.join();
            matReader.join();

            // Close connection
            socket.close();
        } catch (UnknownHostException e) {
            LOG.fatal("Unknown host", e);
            System.exit(1);
        } catch (IOException e) {
            LOG.fatal("IO exception", e);
            System.exit(1);
        } catch (Exception e) {
            LOG.fatal("Unexpected exception", e);
            System.exit(1);
        }
    }
}
