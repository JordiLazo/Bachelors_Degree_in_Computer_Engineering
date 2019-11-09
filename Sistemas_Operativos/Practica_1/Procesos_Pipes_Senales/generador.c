/* -----------------------------------------------------------------------
 PRA1: Processos, pipes i senyals: Primers.
 Codi font: generador.c

 Alejandro Clavera Poza.
 Jordi Lazo Florensa.
 ---------------------------------------------------------------------- */
#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<signal.h>
#include<sys/types.h>
#include<sys/wait.h>

int main(int argc, char* argv[]){
	int number;
	char msg[200];
	
	if(signal(SIGTERM, SIG_DFL)==SIG_ERR){
		perror("Error: ");
		exit(-1);
	}
	
	if(argc == 1){ //Comprueba si se le ha pasado algun argumento en caso contrario acaba el proceso mostrando un error
		sprintf(msg,"Error no se ha recivido ningun argumento\n");
		write(2, msg, strlen(msg));
		exit(-1);
	}else {
		sscanf(argv[1], "%d", &number);	
	}

	if(number < 2){ //Comprueba si el número introducido es >=2 en caso contrario acaba el proceso mostrando un error
		sprintf(msg, "Error el argumento introducido debe de ser un número >= 2\n");
		write(2, msg, strlen(msg));
		exit(-1);
	}

	for(int i = 2; i <= number; i++){
		write(10, &i, sizeof(i)); //Escribe en el pipe numeros la secuencia
	}

	close(10);
	pause();
}
