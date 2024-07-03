#!/bin/sh

# Call the maintenance binary

sleep 5
ip addr add 10.0.1.6/24 dev eth2
ip route add default via 10.0.1.1
ip link set eth0 up
echo "Run Maintenance Process"
maintenance


