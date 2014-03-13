/**
 * MatrixClient.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MatrixClient {

    private static Log LOG = LogFactory.getLog(MatrixClient.class);

    public static void main(String[] args){

        if (args.length != 2) {
            LOG.fatal("Usage: java MatrixClient <input file> <output file>");
            System.exit(1);
        }

        final String INPUT_FILE  = args[0];
        final String OUTPUT_FILE = args[1];

        // Read data from input file


        // Get server info from load balancer


        // Write results to output file

    }
}
