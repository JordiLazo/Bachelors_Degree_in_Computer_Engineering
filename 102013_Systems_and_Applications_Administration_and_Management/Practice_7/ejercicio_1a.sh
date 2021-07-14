#!/bin/bash
while [ 1 ]
do
	sobrepassat=$(repquota -a | awk '$1 == "pep"{used = $3;soft =$4;}
	END{if(used > soft){print "true"} else{print "false"}}')

	if [ $sobrepassat = "true" ]
	then
		echo "Soft Limit sobrepassat"
		echo "Excedida  la  quota  de  disc." | mail -s "Limit sobrepassat" pep
	fi
	sleep 1
done
exit 0