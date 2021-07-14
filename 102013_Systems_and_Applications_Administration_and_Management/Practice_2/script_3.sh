#!/bin/sh

if test $# -ne 1; then
    username=$1
else
    username=$USER
fi

for pid in `ps -u $username -f | awk 'NR>1 {print $2}' | head -n -4` 
do
    virt=$(cat /proc/$pid/statm | awk '{print $1}' )
    if test $virt -gt 1000; then
        echo "$pid"
    fi
done

read input
kill $input