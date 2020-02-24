/*Jordi Rafael Lazo Florensa i Roger Valencia Cabasés
Programació 1 – Grau en Enginyeria Informàtica
Curs 2018-2019
2ª pràctica: Exercici 1 - Missatges d'SPAM */
#include<stdio.h>
#include<ctype.h>
#include<string.h>
#define M 20
#define L 30

//................................................................//NOMBREM LES FUNCIONS QUE UTILITZAREM\\..................................................//
void llegir_text(char paraules_spam[M+1][L+1], int cont[M+1]);    
void saltar_espais(char &se);                                                     
void buscar_spam(char paraules_spam[M+1][L+1], int cont[M+1], char paraules[M+1]);				
//..........................................................................//FUNCIÓ MAIN\\................................................................//      
int main(){

	char paraules_spam[M+1][L+1];    
	int cont[M+1];           
	float SPAM = 0;  
	int n;
	float contador_paraules_repetides=0; 
	int k=0,u;
	char c = '\0';	

	printf("Introdueix les paraules que seran buscades en el text per a detectar si es SPAM o NO:\n");	
	
	while(c!= '.' && k<M) {
		u=0;		
		scanf("%c",&c);
		saltar_espais(c);
	
		while(c != '.' && u<L && c != ' ' && c != '\n') {
			c = tolower(c);
			paraules_spam[k][u] = c;
			scanf("%c",&c);
			u++;
			
			if(c == '.' || c == ' ' || c == '\n') {
				SPAM = SPAM + 1;
			}
		}
		paraules_spam[k][u] = '\0';
		cont[k] = 0;
		k++;
	}
	paraules_spam[k][0] = '\0';

	llegir_text(paraules_spam, cont);

	printf("Anàlisi de les paraules buscades:\n");

	for(n=0; paraules_spam[n][0] != '\0'; n++){
		printf("La paraula \"%s\" apareix \"%i\" vegada/es en el text introduït.\n", paraules_spam[n], cont[n]);

		if(cont[n] >= 2){
			contador_paraules_repetides++;
		}
	}
	printf("\n");
	if((contador_paraules_repetides/SPAM) >= 0.5){
		printf("El resultat de l'anàlisi és el següent:\nS'ha/n trobat \"%g\" paraula/es amb més de 2 repeticiones.\nEl rati es de (%g/%g) = %2.g.\nPer tant, el text introduit ÉS SPAM.\n", contador_paraules_repetides,contador_paraules_repetides,SPAM,contador_paraules_repetides/SPAM);
	}else{
		printf("El resultat de l'anàlisi és el següent:\nS'ha/n trobat \"%g\" paraula/es amb més de 2 repeticiones.\nEl rati es de (%g/%g) = %2.g.\nPer tant, el text introduit NO ÉS SPAM.\n", contador_paraules_repetides,contador_paraules_repetides,SPAM,contador_paraules_repetides/SPAM);
	}
	printf("\n");
}

//............................................................//AQUÍ PROGRAMEM LES FUNCIONS QUE HEM NOMBRAT\\...................................................//
void llegir_text(char paraules_spam[M+1][L+1], int cont[M+1]){

	int l;
	char paraules[L+1];  
	char c='\0';

	printf("Introdueix un text acabat en dos punts per a detectar si es SPAM o NO:\n");
	printf("\n");

	while(c != '.') {
		while(c != '.') {
			l=0;
			saltar_espais(c);

			while(c != ' ' && l<L && c != '.' && c != '\n' && c != ',') {
				c = tolower(c);
				paraules[l] = c;
				l++;
				scanf("%c",&c);
			}
			paraules[l] = '\0';
			buscar_spam(paraules_spam, cont, paraules);
		}
		scanf("%c",&c);
		saltar_espais(c);
	}
}

void saltar_espais(char &c) {

	while(c == ' ' || c == '\n' || c == ',')	{
		scanf("%c",&c);
	}
}

void buscar_spam(char paraules_spam[M+1][L+1], int cont[M+1], char paraules[M+1]) {
	
	int n=0,l=0;
	
	while(n<M && paraules[l] != '\0') {
		l=0;
		
		if(strlen(paraules_spam[n]) == strlen(paraules)) {
			while(paraules_spam[n][l] == paraules[l] && paraules[l] != '\0') {
				l++;
			}

			if(paraules[l] == '\0') {
				cont[n]++;
			}
		}
		n++;
	}
}