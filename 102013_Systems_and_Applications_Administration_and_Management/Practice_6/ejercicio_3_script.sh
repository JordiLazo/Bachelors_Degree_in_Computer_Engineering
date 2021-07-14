#!/bin/bash
 
mkdir /tmp/ramdisk
ROOTUSERNAME=root
MOUNTPT=/tmp/ramdisk
SIZE=2024
BLOCKSIZE=1024
DEVICE=/dev/ram0
 
 
username=`id -nu`
[ "$username" != "$ROOTUSERNAME" ] && echo "no autoritzat" && exit 1
[ ! -d "$MOUNTPT" ] && mkdir $MOUNTPT
 
 
dd if=/dev/zero of=$DEVICE count=$SIZE bs=$BLOCKSIZE
/sbin/mke2fs $DEVICE
 
mount $DEVICE $MOUNTPT
chmod 777 $MOUNTPT
echo $MOUNTPT " disponible"
exit 0
