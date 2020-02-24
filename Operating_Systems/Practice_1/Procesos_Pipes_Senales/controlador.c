/* -----------------------------------------------------------------------
 PRA1: Processos, pipes i senyals: Primers.
 Codi font: controlador.c

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
#include "result.h"

//Función encargada de cerrar todos los procesos hijos(Se llamara cuando ocurra un error y haya que acabar con el programa entero)
void exitProgramError(int numProcesosCreated){
	kill(0, SIGTERM); //Mandamos la señal SIGTERM para que los procesos hijos acaben
	for(int i = 0; i < numProcesosCreated; i++){
		wait(NULL);
	}
	
	exit(-1);
}

int main(int argc, char* argv[]){
	int numProcess;
	int numProcessCreated = 0;
	int pidGenerator;
	int pipeNumbers[2];
	int pipeAnswers[2];
	char input[100];
	char msg[200];
	int numsPrimePipe = 0;
	int numCalculatorExit = 0;
	result result;

	//Control de sañales
	if(signal(SIGTERM, SIG_IGN) == SIG_ERR){ //Aplicamos el tratamiento de la señal SIGTERM que en nuestro caso el proceso la ignorara
		perror("Error: ");
		exit(-1);
	}


	if(argc == 1){ //Comprobamos si se ha pasado el número de procesos calculadores a utilizar
		sprintf(msg, "Error: No se ha introducido el numero de procesos calculadores a utilizar.\n");
		write(2, msg, strlen(msg));
		exit(-1);
	}else {
		sscanf(argv[1], "%d", &numProcess);
		if(numProcess <= 0) {
			sprintf(msg, "Error: El número de procesos que se quiere utilizar es < 1.\n");
			write(1, msg, strlen(msg));
			exit(-1);
		}
	}

	//Pedimos al usuario que ingrese un número
	write(1, "¿Calcular numeros primos de 2 a? ", strlen("¿Calcular numeros primos de 2 a? "));
	read(0, input, sizeof(input));

	//Creación de  los pipes
	if(pipe(pipeNumbers) < 0 || pipe(pipeAnswers) < 0){
		perror("Error no se ha podido crear los pipes: ");
	}
	
	//Creamos proceso generador
	if((pidGenerator = fork()) == 0){
		dup2(pipeNumbers[1], 10);//Asignamos el fichero de escritura del pipe numeros al descritor de ficheros de indice 10
		close(pipeNumbers[0]); //Cerramos todos los ficheros de los pipe que no se vayan a utilizar
		close(pipeNumbers[1]);
		close(pipeAnswers[1]);
		close(pipeAnswers[0]);
		execl("./generador", "generador", input, NULL);
		perror("Error: no se a podido ejecutar el proceso generador");
		exit(-1);
	}else if(pidGenerator < 0){ //Si no se puede crear el proceso el programa acabrara
		perror("Error no se ha podido crear el proceso generador. \n");
		exitProgramError(numProcessCreated);
	}
	numProcessCreated = 1;
	close(pipeNumbers[1]); //Cerramos los ficheros del pipe que no necesite y que no necesiten los procesos generadores

	//Creación de los procesos calculadores
	for(int i = 0; i < numProcess; i++){
		int pidCalculator;
		if((pidCalculator = fork()) == 0){
			dup2(pipeNumbers[0], 11);//Asignamos el fichero de lectura del pipe numeros al descritor de ficheros de indice 11
			close(pipeNumbers[0]);
			dup2(pipeAnswers[1], 20); //Asignamos el fichero de escritura del pipe respuestas al descritor de ficheros de indice 20
			close(pipeAnswers[1]);
			execl("./calculador", "calulador", NULL);
			perror("Error: ");
			exit(-1);
		}else if(pidCalculator < 0){//Si no se puede crear el proceso el programa acabrara				
			perror("Error no se ha podido crear un proceso calculador."); 
			exitProgramError(numProcessCreated);
		}
		numProcessCreated += 1;
	}

	dup2(pipeAnswers[0], 21);

	close(pipeNumbers[0]);
	close(pipeAnswers[1]);

	//Leyemos los resultados
	int code;
	while((code = read(21, &result, sizeof(result))) != 0){
		if(code < 0){
			perror("Error no se ha podido leer del pipe: ");
			exitProgramError(numProcessCreated);
		}
	
		if(result.isPrime == 'S')numsPrimePipe += 1;
		//Mostramos mensaje con la lectura
		sprintf(msg, "Controlador: recibio del Calculador PID %d : numero %d es primo? %c\n", result.pid, result.number, result.isPrime);
		write(1, msg, strlen(msg));
	}
	

	kill(0, SIGTERM);

	
	for(int i = 0; i < numProcessCreated; i++){
		
		if(wait(&code) != pidGenerator){
			code = WEXITSTATUS(code);
			numCalculatorExit += code;
		}else if(WEXITSTATUS(code) == 255){ 
			exitProgramError((numProcessCreated - i) - 1); //Acabamos en caso que el proceso generador haya sufrido algun error.
		}
	}
	
	sprintf(msg, "numeroPrimosPipe=%d numeroPrimosExit=%d\n", numsPrimePipe, numCalculatorExit);
	write(1, msg, strlen(msg));

	exit(0);
}
