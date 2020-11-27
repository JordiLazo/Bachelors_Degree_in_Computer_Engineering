#! /bin/sh
case "$1" in
    start)
    ;;

    stop)
        umount /tmp/raid0
    ;;
    ´)
        echo "Use: /etc/init.d/umountraid {start|stop}"
        exit -1
    ;;
esac
exit 0