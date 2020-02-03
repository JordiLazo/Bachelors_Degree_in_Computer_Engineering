#!/bin/bash
# ----------------------------------------------------------------------
# PRA2: Guions bash
# Codi font: prac2_2.sh
#
# Alejandro Clavera Poza
# Jordi Lazo Forensa
# ----------------------------------------------------------------------
if [ $# -ne 2 ]
then
       echo "$0 suma els dos nombres passats com a parametres"
       echo "Ús: <nombre1> <nombre2>"
       exit 1
fi
echo "$1 + $2 = `expr $1 + $2`"
