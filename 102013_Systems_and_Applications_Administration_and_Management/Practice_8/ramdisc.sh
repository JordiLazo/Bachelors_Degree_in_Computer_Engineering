#!/bin/bash

ROOTUSER_NAME=root
MOUNTPT=/var/lib/mysql
SIZE=102400
BLOCKSIZE=1024 
DEVICE=/dev/ram0 

	service mysql stop
	cp -r /var/lib/mysql/* /home/lazo/Desktop/sql/
	rm -Rf /var/lib/mysql/

	username=`id -nu` 
	[ "$username" != "$ROOTUSER_NAME" ] && echo "no autoritzat" && exit 1
	[ ! -d "$MOUNTPT" ] && mkdir $MOUNTPT
	dd if=/dev/zero of=$DEVICE count=$SIZE bs=$BLOCKSIZE
	mkfs -t ext4 $DEVICE 
	mount $DEVICE $MOUNTPT 
	chmod 777 $MOUNTPT
	echo $MOUNTPT " disponible"
	
	cp -r /home/lazo/Desktop/sql/* /var/lib/mysql/
	chown -R mysql:mysql /var/lib/mysql/
	service mysql start

exit 0