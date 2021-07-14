CXXFLAGS = -ansi -Wall 
CFLAGS = -ansi -Wall 

all: ex1 ex2

test: test-ex1 test-ex2

test-ex1: ex1
	for X in 0 1 2 3 4; do echo "**** Exercici 1.$$X ****"; ./ex1 < ex1-jp$$X.txt; done

test-ex2: ex2
	for X in 0 1 2 3; do echo "**** Exercici 2.$$X ****"; ./ex2 < ex2-jp$$X.txt; done

clean:
	$(RM) ex1 ex2 

zip:
	zip practica-dr.zip Makefile ex1-jp*.txt* ex2-jp*.txt*
