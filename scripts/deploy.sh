#!/bin/bash
#
# Christian Sherland
# Ethan Lusterman
# 3-23-14
#
# deploy.sh
#   A simple shell script to deploy and run this project on servers that
#   accepts command line arguments to specify the deploy location ("aws"
#   or "ice") and for ice, the username to run the project on. For aws
#   servers, the user must provide a valid ssh key file.
#
#   User is responsible for making sure that properties file is up to date for
#   proper deployment. (Maybe more work on this in the future to simplify
#   deploy.)
#
#   Script also assumes that it is run from the root directory of the project.
#
#   Sample usage:
#       ./scripts/deploy.sh ice
#

# CONFIGURATION
source scripts/config.sh
mvn_cmd='mvn clean compile exec:exec -P '

# Function to deploy to ice servers
deployICE() {
    # Running the load balancer
    echo "Running the load balancer";
    load_bal+='git pull;./scripts/loadBalancer.sh >/dev/null 2>&1; exit;'
    $load_bal
    echo "Load balancer now running.";

    # Running the first matrix server
    #echo "Running matrix server 1";
    #server1+='./scripts/matrixServer.sh </dev/null >out.log 2>&1 &'
    #$server1
    #echo "Matrix server 1 is now running.";

#    # Running the second matrix server
#    echo "Running matrix server 2";
#    server2+=$mvn'matrixServer &;exit;'
#    $server2
#    echo "Matrix server 2 is now running.";
#
#    echo "Project successfully deployed to ICE servers.";
#    echo "Ready to accept connections from client.";
}

# Function to deploy to aws
deployAWS() {
    # Running the load balancer
    echo "Running the load balancer";
    ssh -p 31415 ec2-user@ice01.ee.cooper.edu "\
        cd /path/to/remote/project/folder/;\
        git pull;
    mvn clean compile exec:exec -P loadBalancer;
    exit;";
    echo "Load balancer now running.";

    # Running the first matrix server
    echo "Running matrix server 1";
    ssh -p 31415 ec2-user@ice03.ee.cooper.edu "\
        cd /path/to/remote/project/folder/;\
        git pull;
    mvn clean compile exec:exec -P matrixServer;
    exit;";
    echo "Matrix server 1 is now running.";

    # Running the second matrix server
    echo "Running matrix server 2";
    ssh -p 31415 ec2-user@ice03.ee.cooper.edu "\
        cd /path/to/remote/project/folder/;\
        git pull;
    mvn clean compile exec:exec -P matrixServer;
    exit;";
    echo "Matrix server 2 is now running.";

    echo "Project successfully deployed to ICE servers.";
    echo "Ready to accept connections from client.";
}

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <location>";
    exit 1;
fi

# Make sure everything is up to date in git
# (You should really do this yourself though)
#echo "Ensuring that project is up to date in git";
#git add -A;
#git commit -m "Automated Deploy";
#git push origin master;
#echo "Project up to date in git.";

if [ "$1" = "ice" ]; then
    deployICE;
fi

if [ "$1" = "aws" ]; then
    deployAWS;
fi
