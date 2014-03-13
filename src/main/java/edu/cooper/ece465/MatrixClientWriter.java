/**
 * MatrixClientWriter.java
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

    }
}
