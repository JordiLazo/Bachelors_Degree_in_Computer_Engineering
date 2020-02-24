/* -----------------------------------------------------------------------
 PRA1: Processos, pipes i senyals: Primers.
 Codi font: calculador.c

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

int numbersPrimeCalculated = 0;
char msg[200];

//Funcion que trata la señal SIGTERM
void sigTermProcessator(int sig){
	if(sig == SIGTERM){
		exit(numbersPrimeCalculated);
	}
}

int isPrime(int number){
	for(int i = 2; i < number; i++){
		if(number % i == 0){
			return 0;
		}
	}

	return 1;
}

int main(){
	int code;
	result result;

	if(signal(SIGTERM, sigTermProcessator)==SIG_ERR){
		perror("Error: ");
		exit(-1);
	}
	
	while((code = read(11, &result.number, sizeof(result.number)))!=0){ //Leemos del pipe la secuencia generada por el proceso Generador
		if(code < 0){
			perror("Error: ");
			exit(-1);
		}

		result.pid = getpid();

		if(isPrime(result.number)){
			result.isPrime = 'S';
			numbersPrimeCalculated += 1;
		}else {
			result.isPrime = 'N';
		}

		write(20, &result, sizeof(result)); //Enviamos el reslutado al controlador por el pipe resultados
	}
	close(20);
	pause();

}
