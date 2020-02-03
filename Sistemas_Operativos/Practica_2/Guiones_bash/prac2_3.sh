#!/bin/bash
# ----------------------------------------------------------------------
# PRA2: Guions bash
# Codi font: prac2_3.sh
#
# Alejandro Clavera Poza
# Jordi Lazo Forensa
# ----------------------------------------------------------------------
if [ $# -lt 2 ] #Miramos si se han pasado 2 argumentos en caso contrario mostraremos un mensaje explicativo
then
	echo "Uso $0 <ruta home usuarios> <MB umbrall>"
	exit 1
fi

for user_name in $(ls $1 2>/dev/null) #Recorremos la lista de usuarios
do 
	dir="$1/$user_name"
	if [ -d $dir ] #Miramos que la ruta sea un directorio 
    then
        echo "Analizando home de $user_name..."
        if [ -r $dir ] #Miramos si tenemos permisos de lectura, si no los tenemos mostramos un mensaje indicando que no se ha podido leer
        then
       		result=$(du -ms -x $dir)
       		size_user=${result/$dir/''}
        	if [ $size_user -gt $2 ]
        	then
				echo "EL usuario $user_name excedio la quota de $2 MB, tiene $(($size_user)) MB ocupados"
			else
				echo "...Ok!"
        	fi
        else
        	echo "No se ha podido leer $dir"
        fi
    fi
done

