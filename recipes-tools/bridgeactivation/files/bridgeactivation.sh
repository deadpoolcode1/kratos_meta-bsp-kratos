#!/bin/sh

start_bridge() {
    brctl addbr br0
    brctl addif br0 eth0
    brctl addif br0 eth1
    ifconfig eth0 0.0.0.0
    ifconfig eth1 0.0.0.0
    ifconfig br0 10.0.1.3
    ifconfig br0 up
}

stop_bridge() {
    ifconfig br0 down
    brctl delbr br0
}

case "$1" in
    start)
        start_bridge
        ;;
    stop)
        stop_bridge
        ;;
    *)
        echo "Usage: $0 {start|stop}"
        exit 1
        ;;
esac

