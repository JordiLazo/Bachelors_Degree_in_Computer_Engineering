FLAGS = -ansi -Wall

.SILENT:

all: ex1.cpp ex2.cpp
	g++ ex1.cpp -o ex1 $(FLAGS)
	g++ ex2.cpp -o ex2 $(FLAGS)

test: ex1 ex2
	echo "**** Exercici 1.0 ****"
	./ex1 < ex1-jp0.txt
	echo "**** Exercici 1.1 ****"
	./ex1 < ex1-jp1.txt
	echo "**** Exercici 2.0 ****"
	./ex2 < ex2-jp0.txt
	echo "**** Exercici 2.1 ****"
	./ex2 < ex2-jp1.txt

clean:
	rm -f ex1 ex2
	ls
