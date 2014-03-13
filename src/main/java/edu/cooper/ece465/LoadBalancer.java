/**
 * LoadBalancer.java
 *  Receives requests from Client objects to be connected to an available Server.
 *  Tells the Client which Server is available.
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
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoadBalancer {

    private static Log LOG = LogFactory.getLog(LoadBalancer.class);

    public static void main(String[] args){

        if (args.length != 2) {
            LOG.fatal("Usage: java LoadBalancer <client port number> <server port number>");
            System.exit(1);
        }

        // Listening ports
        final int CLIENT_PORT = Integer.parseInt(args[0]);
        final int SERVER_PORT = Integer.parseInt(args[1]);

        // Listen for connections from HistogramServers
        PriorityQueue<ServerStatus> queue = new PriorityQueue<ServerStatus>();
        HashMap<String, ServerStatus> hm  = new HashMap<String, ServerStatus>();

        // Set up listener for info on histogram servers
        try {
            Runnable listener = new ServerListener(queue, hm, SERVER_PORT);
            (new Thread(listener)).start();
        } catch (Exception e) {
            LOG.fatal("Could not create listener", e);
            System.exit(1);
        }

        try {
            // Listen for connections from clients and connect them with the best HistogramServer
            ServerSocket serverSocket = new ServerSocket(CLIENT_PORT);

            LOG.info("Listening for new client connections on port: "+ CLIENT_PORT);
            while(true){

                Socket socket = serverSocket.accept();
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                LOG.info("New connection from client.");

                // Determine best server to give to client
                ServerStatus bestServer = queue.peek();
                output.writeObject(bestServer.getHost());
                output.writeObject(bestServer.getPort());
                LOG.info("Sending best server :" + bestServer.getHost() + ":" + bestServer.getPort());

                // Close connection to client
                output.close();
                socket.close();
            }
        } catch (IOException e) {
            LOG.fatal("Error: Could not listen on port" + CLIENT_PORT, e);
            System.exit(1);
        }
    }
}
