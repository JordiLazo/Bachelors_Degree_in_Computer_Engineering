#!/bin/bash
evolucio_memoria() {
    cat /proc/meminfo | awk 'NR == 1 || NR == 2 || NR == 3 || NR == 7 || NR == 26' >> mem_lliure.lst
    df >> mem_lliure.lst
}
date > mem_lliure.lst
for i in `seq 60` 
do
    evolucio_memoria
    sleep 1
done
date >> mem_lliure.lst
