/**
 * ServerStatus.java
 *  Serializable information about Histogram servers. Allows load balancer to
 *  keep track of connected server info.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
 */

package edu.cooper.ece465;

import java.io.Serializable;

public class ServerStatus implements Serializable, Comparable<ServerStatus> {
    private String hostName;
    private int portNumber;
    private double serverLoad;

    public ServerStatus(String hostName, int portNumber, double serverLoad) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.serverLoad = serverLoad;
    }

    public String getKey() {
        return this.hostName + "::" + this.portNumber;
    }

    public String getHost() {
        return this.hostName;
    }

    public int getPort() {
        return this.portNumber;
    }

    public double getLoad() {
        return this.serverLoad;
    }

    @Override
    public int compareTo(ServerStatus s) {
        if (this.getLoad() > s.getLoad()) {
            return 1;
        } else if (this.getLoad() == s.getLoad()) {
            return 0;
        } else {
            return -1;
        }
    }

}
