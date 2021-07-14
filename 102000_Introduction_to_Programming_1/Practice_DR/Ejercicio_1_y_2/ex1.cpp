/*Programació I – Grau en Enginyeria Informàtica
Pràctica docència repetida: Exercici 1 - Missatges d'SPAM 
Jordi Rafael Lazo Florensa
Universitat de Lleida*/
#include<stdio.h>
#include<ctype.h>
#include<string.h>
#define M 20 //maxim paraules per llista
#define N 30 //maxim lletres per paraules
//Declaració de les funcions
void llegir_paraules_buscar(char paraules_buscar[M][N+1]);
void llegir_text(char paraules_buscar_primera[M][N+1], char paraules_buscar_segona[M][N+1], int repeticions_primera[M], int repeticions_segona[M]);
void buscar_coincidencia(char paraules_buscar_primera[M][N+1], char paraules_buscar_segona[M][N+1], int repeticions_primera[M], int repeticions_segona[M], char paraula[N+1]);
void mostrar_resultats(char paraules_buscar_primera[M][N+1], char paraules_buscar_segona[M][N+1], int repeticions_primera[M], int repeticions_segona[M]);

int main(){
	char paraules_buscar_primera[M][N + 1];
	char paraules_buscar_segona[M][N + 1];
	int repeticions_primera[M] = {0};
	int repeticions_segona[M] = {0};
	llegir_paraules_buscar(paraules_buscar_primera);//Utilitzem la mateixa funcio per les dues llistes de paraules que hi han
	llegir_paraules_buscar(paraules_buscar_segona);
	llegir_text(paraules_buscar_primera, paraules_buscar_segona, repeticions_primera, repeticions_segona);
	mostrar_resultats(paraules_buscar_primera, paraules_buscar_segona, repeticions_primera, repeticions_segona);
}
void llegir_paraules_buscar(char paraules_buscar[M][N+1]){//Guardem les paraules que buscarem en el text
	int i = 0, j;
	char c;
	scanf("%c", &c);
	while(!isalpha(c) && c != '.'){ //Bucle per saltar espais
		scanf("%c", &c);
	}
	while (i < M && c!='.'){//Llegueix la paraula en concret 
		j = 0;
		while (j <= N && isalpha(c)){//Guarda els caracters en la matriu paraules_buscar
			paraules_buscar[i][j] = tolower(c);//Transformar totes les paraules a minuscules
			scanf("%c", &c);//Tornem a guardar
			j++;
		}
		paraules_buscar[i][j] = '\0';//Despres de cada paraula coloquem el caracter de final de string
		while (!isalpha(c) && c != '.'){//Un altre bucle que saltara els espais en blancs
			scanf("%c", &c);
		}
		i++;
	}
	while (i< M){//Omple els espais de les paraules que no ha llegit amb el caracter de final de string
		paraules_buscar[i][0] = '\0';
		i++;
	}
}
void llegir_text(char paraules_buscar_primera[M][N+1], char paraules_buscar_segona[M][N+1], int repeticions_primera[M], int repeticions_segona[M]){
	char paraula[N+1];
	char lletra;
	int i;

	bool punt = false;//creo una bool per verificar si hi han 2 punts seguits
	scanf("%c", &lletra);
	while(!isalpha(lletra) && lletra != '.'){//Es el mateix bucle de la primera funcio (saltar espais)
		scanf("%c", &lletra);
	}
	while(lletra != '.' && punt == false){//Aquest bucle llegueix tot el text
		i = 0;
		while (i <= N && isalpha(lletra)){//Guarda la paraula i la transforma en minuscula
			paraula[i] = tolower(lletra);
			scanf("%c", &lletra);
			i++;
		}
		paraula[i] = '\0';//Tornem a posar el caracter de final de string
		buscar_coincidencia(paraules_buscar_primera, paraules_buscar_segona, repeticions_primera, repeticions_segona, paraula);//Es truca a la funcio buscar_coincidencia que compara la paurala amb les dues llistes
		while (!isalpha(lletra) && lletra != '.'){//Aquest bucle salta espais (com el primer bucle)
			scanf("%c", &lletra);
		}
		if (lletra == '.'){//Afegeixo un if per comprovar si es un punt
			scanf("%c", &lletra);
			while (!isalpha(lletra) && lletra != '.'){//Tornem a saltar espais
				scanf("%c", &lletra);
			}
			if (lletra == '.'){//Si despres de saltar els espais i es troba un punt 
				punt = true;//Sortira del bucle while principal (el que llegueix tot el text)
			}
		}
	}
}
void buscar_coincidencia(char paraules_buscar_primera[M][N+1], char paraules_buscar_segona[M][N+1], int repeticions_primera[M], int repeticions_segona[M], char paraula[N+1]){
	int i;
	for (i = 0; i < M; i++){
		if (strcmp(paraules_buscar_primera[i], paraula) == 0){//Utilitzem la funcio strcmp per comparar la paraula de la llista 1 amb la paraula tractada
			repeticions_primera[i]++;
		}
		if (strcmp(paraules_buscar_segona[i], paraula) == 0){//Utilitzem la funcio strcmp per comparar la paraula de la llista 2 amb la paraula tractada
			repeticions_segona[i]++;
		}
	}
}
void mostrar_resultats(char paraules_buscar_primera[M][N+1], char paraules_buscar_segona[M][N+1], int repeticions_primera[M], int repeticions_segona[M]){
	int i = 0;
	printf("Relacio de paraules buscades a la primera llista\n");
	while(i < M && paraules_buscar_primera[i][0] != '\0'){
		//Afegim la condicio paraules_buscar_primera[i][0] != '\0' per a que nomes imprimeixe les paraules de la llista 1 intriduida i no les paraules buides 
		printf("La paraula \'%s\' apareix %i vegades.\n", paraules_buscar_primera[i], repeticions_primera[i]);
		//Imprimim per pantalla les paraules que han siguit introduides en la llista 1 i les repetcions
		i++;
	}
	i = 0;
	printf("Relacio de paraules buscades a la segona llista\n");
	while(i < M && paraules_buscar_segona[i][0] != '\0'){
		//Afegim la condicio paraules_buscar_primera[i][0] != '\0' per a que nomes imprimeixe les paraules de la llista 2 intriduida i no les paraules buides 
		printf("La paraula \'%s\' apareix %i vegades.\n", paraules_buscar_segona[i], repeticions_segona[i]);
		i++;
	}
}