/**
 * MatrixWorkerWriter.java
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

    private ObjectOutputStream output;
    private int matCount;
    private static Log LOG = LogFactory.getLog(MatrixWorkerWriter.class);

    public MatrixWorkerWriter(ObjectOutputStream o, int count) {
        LOG.info("New MatrixWorkerWriter created.");
        this.output   = o;
        this.matCount = count;
    }

    @Override
    public void run() {
    }
}
