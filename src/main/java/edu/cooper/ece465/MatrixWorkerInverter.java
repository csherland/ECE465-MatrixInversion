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

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MatrixWorkerInverter implements Runnable {

    private Matrix matrix;
    private static Log LOG = LogFactory.getLog(MatrixWorkerInverter.class);

    public MatrixWorkerInverter(Matrix m) {
        LOG.info("New MatrixWorkerInverter created.");
        this.matrix = m;
    }

    @Override
    public void run() {
    }
}
