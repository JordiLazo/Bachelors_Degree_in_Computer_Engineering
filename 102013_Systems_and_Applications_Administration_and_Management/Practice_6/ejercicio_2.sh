#! /bin/bash

for i in $(seq 60)
do
    awk '
        $1 == "MemTotal:" {MemTotal = $2 " " $3}
        $1 == "MemFree:" {MemFree = $2 " " $3}
        END {
            print "Total: " MemTotal "\tFree: " MemFree "\tPercentatge Free: " MemFree/MemTotal*100 "%";
            if (MemFree < MemTotal*0.1) {
                print "Cal crear Swap\n"
                system("./createswap.sh")
            }
        }
    ' /proc/meminfo
 
    sleep 1
done
