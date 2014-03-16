/**
 * MatrixServer.java
 *    A server that accepts incoming connections from clients and spawns a worker
 *    to handle matrix inversion of input data from client.
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
import java.util.concurrent.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MatrixServer {

    private static final int THREAD_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE    = 4;
    private static final int KEEP_ALIVE_TIME  = 5;
    private static final int WORK_QUEUE_SIZE  = 2;
    private static Log LOG = LogFactory.getLog(MatrixServer.class);

    public static void main(String[] args){

        if (args.length != 2) {
            LOG.fatal("Usage: java MatrixServer <load balancer hostname> <load balancer port> <client port number>");
            System.exit(1);
        }

        // Listening ports
        final String LB_NAME  = args[0];
        final int LB_PORT     = Integer.parseInt(args[2]);
        final int CLIENT_PORT = Integer.parseInt(args[1]);

        // Setup the thread pool
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(THREAD_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(WORK_QUEUE_SIZE),
                threadFactory, rejectionHandler);

        // Start the monitoring thread (sends information to load balancer)
        MonitorThread monitor = new MonitorThread(executorPool, 3, LB_NAME, LB_PORT, CLIENT_PORT);
        Thread monitorThread  = new Thread(monitor);
        monitorThread.start();

        // Listen for client connections
        try {
            ServerSocket serverSocket = new ServerSocket(CLIENT_PORT);
            LOG.info("Listening for new client connections.");
            while(true) {
                Socket socket = serverSocket.accept();
                LOG.info("Accepted new client connection.");

                // Handle new client connection in a thread
                executorPool.execute(new MatrixServerWorker(socket));
            }
        } catch (IOException e) {
            LOG.fatal("ServerSocket not functional.");
        }

        // Shutdown threadpool and monitor
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            LOG.error("Thread sleep error.", e);
        }

        // Cleanup
        executorPool.shutdown();
        monitor.shutdown();

        LOG.info("Histogram server shut down.");
    }
}
