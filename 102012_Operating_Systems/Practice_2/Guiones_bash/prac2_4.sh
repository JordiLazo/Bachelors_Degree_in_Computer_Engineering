#!/bin/bash
# ----------------------------------------------------------------------
# PRA2: Guions bash
# Codi font: prac2_2.sh
#
# Alejandro Clavera Poza
# Jordi Lazo Forensa
# ----------------------------------------------------------------------
tipFitxer()
{
	file_type=$(file $1 2>/dev/null) #Cargamos la salida del comando file en caso de error redireccionamos la salida de errores a /dev/null
	exit_status=$? #Guardamos el codigo de retorno del comando file
	return $exit_status
}

if [ $# -eq 0 ] #Miramos si se ha pasado algun argumento al script 
#Caso 1 no se ha pasado ningun argumento
then 
	dir=$(pwd) #Guardamos la direccion del directorio actual
	echo "Listado de ficheros del directorio [$dir]:"
	echo "--------------------------------------------"
		for file in $(ls $dir)
		do
			if [ -d $file ] 
			then
				echo "$file es un directorio" 

			elif [ -f $file ] #Miramos si el fichero es un directorio
			then
				file_type=''
				tipFitxer $file
				exit_status=$?
				if [ $exit_status -ne 0 ] #Miramos si el codigo de retorno de la función no sea 0 en casos afirmativo muestra mensaje de error
				then
					echo "ERROR en la ejecución de la función tipFitxer. Codigo retorno: $exit_status"
				else
					echo "$file_type"
				fi
			else 
				echo "$file no es ni directorio ni fichero" 

			fi	
		done
else
#Caso 2: Se ha pasado por argumento el nombre de los ficheros o directorios que se quiere analizar
	echo "Listado de ficheros del directorio [$*]:"
	echo "--------------------------------------------"
	for file in $*
	do
		if [ -d $file ] 
			then
				echo "$file es un directorio"

			elif [ -f $file ] 
			then
				file_type=''
				tipFitxer $file
				exit_status=$?
				if [ $exit_status -ne 0 ]
				then
					echo "ERROR en la ejecución de la función tipFitxer. Codigo retorno: $exit_status"

				else
					echo "$file_type"
				fi

			else 
				echo "$file no es ni directorio ni fichero"

			fi	
	done
fi


