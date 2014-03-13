/**
 * RejectedExecutionHandlerImpl.java
 *  Simple Implementation of rejection handler for thread pool in the
 *  HistogramServer.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
 */

package edu.cooper.ece465;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

    private static Log LOG = LogFactory.getLog(RejectedExecutionHandlerImpl.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        LOG.info(r.toString() + " is rejected");
    }

}
