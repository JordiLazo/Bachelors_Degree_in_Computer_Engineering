#!/bin/sh

if test $# -ne 1; then
    #ps: show current processes
    #awk: get pid by cutting it from ps
        #NR>1: removes first line (removes header) 
    #head -n -4: removes last 3 processes that caused errors
    for pid in `ps -f | awk 'NR>1 {print $2}' | head -n -4` 
    do 
        echo "PID: $pid"
        #Number of active files
        counter=0
        for file in `ls -la /proc/$pid/fd` 
        do 
            counter=$((counter+1))
        done
        echo "Counter: $counter"
        #virtual size
        virt=$(cat /proc/$pid/statm | awk '{print $1}' )
        echo "Virtual size : $virt "
        #resident size
        resd=$(cat /proc/$pid/statm | awk '{print $2}' )
        echo "resident size : $resd \n"
    done
    exit
fi
ls -la /proc/$1/fd
