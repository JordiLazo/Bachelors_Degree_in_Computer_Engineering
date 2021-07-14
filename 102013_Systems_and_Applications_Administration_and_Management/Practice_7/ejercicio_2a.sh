#!/bin/bash
repquota -a | sed "1,5d" | awk '($3 > $4) && ($4 != 0) {print $1 " ha sobrespassat el Soft Limit"}'
exit 0
