#!/bin/bash

#in terminal: less /etc/passwd
sudo groupadd students -g 1500
for i in `seq 3`
	do
		sudo useradd "student$i" -g 1500
		echo "password"$i"\npassword"$i  | sudo passwd "student$i"
	done
