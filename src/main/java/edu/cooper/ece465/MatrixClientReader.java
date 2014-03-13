/**
 * MatrixClientReader.java
 *  Sends invertible matrices specified in the input
 *  file command line argument to a server and waits
 *  to read back inverted results. Saves results to
 *  the output file specified as a command line argument.
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

public class MatrixClientReader implements Runnable {

    private Socket socket;
    private String outFile;
    private static Log LOG = LogFactory.getLog(MatrixClientReader.class);

    public MatrixClientReader(Socket s, String outputFile) {
        LOG.info("Constructing new MatrixClientReader");
        this.socket  = s;
        this.outFile = outputFile;
    }

    @Override
    public void run() {

    }
}
