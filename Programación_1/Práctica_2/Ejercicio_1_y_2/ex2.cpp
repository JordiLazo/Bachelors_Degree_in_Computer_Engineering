/*Jordi Rafael Lazo Florensa i Roger Valencia Cabasés
Programació 1 – Grau en Enginyeria Informàtica
Curs 2018-2019
2ª pràctica: Exercici 2 – Comprovació de Sudokus*/
#include <stdio.h>
#define N 9
//...............................................................//NOMBREM LES FUNCIONS QUE UTILITZAREM\\......................................................//
void llegir_sudoku(int sudoku[N][N]);
void dibuixar_sudoku(int sudoku[N][N]);
int comprovar_sudoku(int comprovar_fila[N], int comprovar_columna[N], int sudoku[N][N]);
bool verificar_fila(int sudoku[N][N], int fila, int columna, int posicio);
bool verificar_columna(int sudoku[N][N], int fila, int columna, int posicio);
bool verificar_regio(int sudoku[N][N], int fila, int columna, int posicio);
int trobar_regio(int valor);
//.............................................................................//FUNCIÓ MAIN\\................................................................//
int main() {

	int comprovar_fila[N];
	int comprovar_columna[N];
	int sudoku [N][N] = {};

	llegir_sudoku(sudoku);
	dibuixar_sudoku(sudoku);
	comprovar_sudoku(comprovar_fila,comprovar_columna,sudoku);
}
//...........................................................//AQUÍ PROGRAMEM LES FUNCIONS QUE HEM NOMBRAT\\...................................................//
void llegir_sudoku(int sudoku[N][N]){
	int c, f;
	printf("Introdueix un sudoku 9x9 per a comprovar si és correcte o no:\n");
	for(f = 0; f < N; f++){
		for(c = 0; c < N; c++){
			scanf("%i", &sudoku[c][f]);
		}
	}
}

void dibuixar_sudoku(int sudoku[N][N]){
	int c, f;
	for(f = 0; f < N; f++){
		printf("|---|---|---|---|---|---|---|---|---|\n");
		for(c = 0; c < N; c++){
			printf("| %i ", sudoku[c][f]);
		}
		printf("|\n");
	}
	printf("|---|---|---|---|---|---|---|---|---|\n");
}

int comprovar_sudoku(int comprovar_fila[N], int comprovar_columna[N],int sudoku[N][N]){
	for(int i = 0; i < N ; i++) {
		for (int j = 0; j < N ; j++) {
			int posicio = sudoku[i][j];
			if (!verificar_fila(sudoku, i,j, posicio)) {
				printf("El sudoku introduït té un error en la fila %i.\nPer tant, el SUDOKU ÉS INCORRECTE.\n",j +1);
				printf("\n");
				return -1;
			}
			if (!verificar_columna(sudoku, i,j, posicio)) {
				printf("El sudoku introduït té un error en la columna %i.\nPer tant, el SUDOKU ÉS INCORRECTE.\n",i+1);
				printf("\n");
				return -1;
			}
			if (!verificar_regio(sudoku, i, j, posicio)) {
				printf("El sudoku introduït té un error en la regió %i,%i.\nPer tant, el SUDOKU ÉS INCORRECTE.\n",trobar_regio(i),trobar_regio(j));
				printf("\n");
				return -1;
			}
		}
	}
	printf("El SUDOKU introduït és CORRECTE.\n");
	printf("\n");
	return 0;	
}

bool verificar_fila(int sudoku[N][N], int fila, int columna, int posicio){
	fila++;
	while(fila<N) {
		if (sudoku[fila][columna] == posicio)
			return false;
		fila++;
	}
	return true;
}

bool verificar_columna(int sudoku[N][N], int fila, int columna, int posicio){
	columna++;
	while(columna<N) {
		if (sudoku[fila][columna] == posicio)
			return false;
		columna++;
	}
	return true;
}
bool verificar_regio(int sudoku[N][N], int fila, int columna, int posicio){
	for(int i = fila ; i%3 > 0 ; i++) 
		for(int j = columna; j%3 > 0 ; j++) 
			if(sudoku[i][j] == posicio && !(i == fila && columna == j))
				return false;
	return true;
}

int trobar_regio(int valor) {
		if (valor > 0 && valor <= 2)
			return 1;
		else if( valor >2 && valor<=5)
			return 2;
		else if(valor>5 && valor <=8)
			return 3;
		else
			return -1;
}
