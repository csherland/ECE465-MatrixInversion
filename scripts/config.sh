#!/bin/bash
#
# Ethan Lusterman
#
# This configuration is for ICE Lab Server tests

# ICE machines that have maven
ssh_servers=(01 06 11 12)
host=ee.cooper.edu

# on each ICE machine user account, the repo is in the same place
path_to_repo='~/developer/ECE465-MatrixInversion'

# your ICE username and password - because ICE uses kerberos, there's
# a weird config issue with setting up SSH public/private keys
# If on a mac, install sshpass with brew
#   brew install https://raw.github.com/eugeneoden/homebrew/eca9de1/Library/Formula/sshpass.rb
# If on linux, not sure, I think it's easy to install
username='your-ice-username'
pass='your-ice-password'        # DON'T COMMIT YOUR PASSWORD!!!!

# Build command prefix for each instance
#   e.g. sshpass -p <your-passsord> ssh -p 31415 <user>@ice##.ee.cooper.edu \
#           source /etc/profile; \
#           cd ~/developer/ECE465-MatrixInversion; \
#           git pull; \
#           ./scripts/loadBalancer.sh < /dev/null 2>&1 & exit;
ssh_prefix='sshpass -p '
ssh_prefix+=$pass
ssh_prefix+=' ssh -p 31415 '
ssh_prefix+=$username

# build base sshpass + ssh command
load_bal=$ssh_prefix'@ice'${ssh_servers[0]}'.'$host' '
server1=$ssh_prefix'@ice'${ssh_servers[1]}'.'$host' '
server2=$ssh_prefix'@ice'${ssh_servers[2]}'.'$host' '
server3=$ssh_prefix'@ice'${ssh_servers[3]}'.'$host' '

# load profile that loads full path with java and maven on remote
load_bal+='source /etc/profile;cd '$path_to_repo';'
server1+='source /etc/profile;cd '$path_to_repo';'
server2+='source /etc/profile;cd '$path_to_repo';'
server3+='source /etc/profile;cd '$path_to_repo';'
