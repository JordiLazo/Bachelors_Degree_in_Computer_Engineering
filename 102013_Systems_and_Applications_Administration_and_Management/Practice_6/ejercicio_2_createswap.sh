#!/bin/bash

ROOT_UID=0
FILE=/tmp/swap
BLOCKSIZE=1024;MINBLOCKS=1024
[ "$UID" -ne "$ROOT_UID" ] && echo "no autoritzat" && exit 1
blocks=${1:-$MINBLOCKS}
[ "$blocks" -lt "$MINBLOCKS" ] && echo "blocks>$MINBLOCKS" && exit 2
dd if=/dev/zero of=$FILE bs=$BLOCKSIZE count=$blocks
/sbin/mkswap -f $FILE $blocks
/sbin/swapon $FILE
echo "Fitxer Swap creat i activat"
exit 0
