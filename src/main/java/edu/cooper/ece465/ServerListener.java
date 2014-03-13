/**
 * ServerListener.java
 *  A runnable that listens for new histogram server connections
 *  and stores information on the server within the specified
 *  priority queue and hashmap.
 *
 *  Listens for new matrix inversion server connections on the port specified
 *  by portNumber.
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

public class ServerListener implements Runnable {

    private int port;
    private PriorityQueue<ServerStatus> queue;
    private HashMap<String, ServerStatus> hashMap;
    private ServerSocket serverSocket;
    private static Log LOG = LogFactory.getLog(ServerListener.class);

    public ServerListener(PriorityQueue<ServerStatus> queue, HashMap<String, ServerStatus> hm, int portNumber) throws IOException {
        LOG.info("Creating new server listener.");
        this.queue   = queue;
        this.hashMap = hm;
        this.port    = portNumber;
        this.serverSocket = new ServerSocket(portNumber);
    }

    @Override
    public void run() {
        LOG.info("Listening for new server connections on port: " + port);
        while (true) try {
            Socket socket = serverSocket.accept();
            LOG.info("New connection from histogram server");

            // Get server status
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ServerStatus status   = (ServerStatus) ois.readObject();

            // If the server is in the queue, remove it (so it is updated on add)
            String key = status.getKey();
            if (hashMap.containsKey(key)) {
                queue.remove(hashMap.get(key));
            }

            // Add key into the hashtable and queue
            hashMap.put(key, status);
            queue.add(status);

            // Close up connection
            ois.close();
            socket.close();
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found", e);
        } catch (IOException e) {
            LOG.error("IO Exception", e);
        }
    }
}
