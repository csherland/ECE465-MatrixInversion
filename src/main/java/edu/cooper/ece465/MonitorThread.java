/**
 * MonitorThread.java
 *  Responsible for monitoring the state of threads in the HistogramServer thread
 *  pool. Periodically send load information on performance to the LoadBalancer.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
 */

package edu.cooper.ece465;

import java.io.*;
import java.net.*;
import org.hyperic.sigar.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MonitorThread implements Runnable {

    private ThreadPoolExecutor executor;
    private int seconds;
    private int portNumber;
    private int clientPortNumber;
    private String hostname;
    private boolean run = true;
    private static Log LOG = LogFactory.getLog(MonitorThread.class);

    public MonitorThread(ThreadPoolExecutor executor, int delay, String hostname, int portNumber, int clientPortNumber) {
        LOG.info("Spawning new monitor thread.");
        this.executor = executor;
        this.seconds  = delay;
        this.hostname = hostname;
        this.portNumber = portNumber;
        this.clientPortNumber = clientPortNumber;
    }

    public void shutdown(){
        LOG.info("Shutting down monitor thread.");
        this.run = false;
    }

    @Override
    public void run() {
        while(run){
            // Locally display information about thread pool
            LOG.info(
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                        this.executor.getPoolSize(),
                        this.executor.getCorePoolSize(),
                        this.executor.getActiveCount(),
                        this.executor.getCompletedTaskCount(),
                        this.executor.getTaskCount(),
                        this.executor.isShutdown(),
                        this.executor.isTerminated()));

            // Talk to master server with current load stats and server status
            try {
                LOG.info("Sending data to load balancer: " + hostname + " on port: " + portNumber);

                // Determine current system load
                Sigar s = new Sigar();

                // Send system information to load balancer
                Socket socket = new Socket(InetAddress.getByName(this.hostname), this.portNumber);
                ServerStatus data = new ServerStatus(socket.getLocalAddress().getHostName(), this.clientPortNumber, s.getLoadAverage());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(data);

                // Clost up connection
                oos.close();
                Thread.sleep(seconds*10000);
            } catch (Exception e) {
                LOG.error("Could not communicate with master server.", e);
            }

        }
    }
}
