#!/bin/bash
while true;
do
    AverageProcessorLoad=($(cat /proc/loadavg))
    MemTotal=($(cat /proc/meminfo))
    TotalMemory=${MemTotal[1]} 
    ActiveMemory=${MemTotal[19]}

    AverageExecutionProcesses=$((( ${ActiveMemory} *100 )/${TotalMemory}))

    echo "CPU (avg processes) 1m: ${AverageProcessorLoad[0]}"
    echo "CPU (avg processes) 5m: ${AverageProcessorLoad[1]}"
    echo "CPU (avg processes) 15m: ${AverageProcessorLoad[2]}"
    echo "BUSY MEMORY %: ${AverageExecutionProcesses}"
    echo ""
    sleep 1
done