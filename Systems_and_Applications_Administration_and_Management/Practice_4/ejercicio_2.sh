#!/bin/bash

#ls -la /proc/ | awk '{print file}' | bash

#for file in `ls -la /proc/ | awk '{print $9}'`

#ls -la /proc/ | cat $(awk '{print $9}')/stat | awk '{print $0}'

#ls -la /proc/ | awk '{cat /proc/$9/stat}'| bash

#ls -la /proc/ | $ (hola=`awk'{print proc/$9/stat}'`; print $hola)

#ls -la /proc/ | $(hola=$(awk '{printf "proc/%s/stat", $9}'); print $hola) 

#awk 'BEGIN{file="/proc/100/stat";while ((getline<file) > 0) {print}}'


ps -aef | awk '{printf "/proc/%s/stat\n", $2}' | cat $(awk '{print $0}') | awk 'BEGIN {bestV=0; bestM=0; bestUT=0; bestST=0; pidV=0; pidM=0; pidUT=0; pidST=0}\ 
{if (int($23) > bestV){pidV=$1; bestV=int($23)}};\ 
{if (int($24) > bestM){pidM=$1; bestM=int($24)}};\ 
{if (int($14) > bestUT){pidUT=$1; bestUT=int($14)}};\
{if (int($15) > bestST){pidST=$1; bestST=int($15)}}\
{printf "PID: %s \nVirtual Memory: %s \nResident Memory: %s \nUser Time: %s \nSystem Time: %s \n --- \n"\
                        , $1, $23, $24, $14, $15}\
END {printf "Best Virtual Memory: %i \nBest Resdient Memory: %i \nBest User Time: %i\nBest System Time: %i\nAv VM: %i \nAv M: %i\nAv UT: %i\nAv ST: %i\n"\
                        , pidV, pidM, pidUT, pidST, bestV/NF, bestM/NF, bestUT/NF, bestST/NF}' >> MEM.user