/*Programació I – Grau en Enginyeria Informàtica
Pràctica docència repetida: Exercici 2 - Sudoku 
Jordi Rafael Lazo Florensa
Universitat de Lleida*/
#include <stdio.h>
#include <ctype.h>
#define N 16
//...............................................................//NOMBREM LES FUNCIONS QUE UTILITZAREM\\......................................................//
void llegir_sudoku(char sudoku[N][N]);
void dibuixar_sudoku(char sudoku[N][N]);
int comprovar_sudoku(int comprovar_fila[N], int comprovar_columna[N], char sudoku[N][N]);
bool verificar_fila(char sudoku[N][N], int fila, int columna, int posicio, int &coincidencia_fila);
bool verificar_columna(char sudoku[N][N], int fila, int columna, int posicio, int &coindidencia_columna);
bool verificar_regio(char sudoku[N][N], int fila, int columna, int posicio, int &coincidencia_regio);
int trobar_regio(int valor);
int transformar_digits(int hexadecimal);
//.............................................................................//FUNCIÓ MAIN\\................................................................//
int main() {

	int comprovar_fila[N];
	int comprovar_columna[N];
	char sudoku [N][N] = {};

	llegir_sudoku(sudoku);
	dibuixar_sudoku(sudoku);   
	comprovar_sudoku(comprovar_fila,comprovar_columna,sudoku);
}
//...........................................................//AQUÍ PROGRAMEM LES FUNCIONS QUE HEM NOMBRAT\\...................................................//
void llegir_sudoku(char sudoku[N][N]){
	int i, j;
	char celda;
	printf("Introdueix un sudoku 16x16 per a comprovar si és correcte o no:\n");
	for(i = 0; i < N; i++){
		for(j = 0; j < N; j++){
			scanf("%c", &celda);
			while(celda == ' ' || celda == '\n' || celda=='\t'){
				scanf("%c",&celda);
			}
			sudoku[i][j]=celda;
			
		} 
	}
}
//Dibuixem el sudoku que en estra per la terminal
void dibuixar_sudoku(char sudoku[N][N]){
	int i,j;
	for(i = 0; i < N; i++){
		printf("|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n");
		for(j = 0; j < N; j++){
			printf("| %c ", sudoku[i][j]);
		}
		printf("|\n");
	}
	printf("|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n");
}
//Verifiquem la fila
bool verificar_fila(char sudoku[N][N], int fila, int columna, int posicio){
	for(int i=columna;i<N;i++){

		if(i!=columna && sudoku[fila][i]== posicio){
			return false;
		}
	}
	return true;
}
//Verifiquem la columna
bool verificar_columna(char sudoku[N][N], int fila, int columna, int posicio){
	
	for(int i=0;i<N;i++){

		if(i!=fila && sudoku[i][columna]== posicio){
			return false;
		}
	}
	return true;
}
//Verifiquem la regio
bool verificar_regio(char sudoku[N][N], int fila, int columna, int posicio){
	for(int i = fila ; i%4 > 0 ; i++){ 
		for(int j = 0; j%4 > 0 ; j++){
			if(sudoku[i][j] == posicio && !(i == fila && columna == j)){
				return false;
			}
		}
	}
	return true;
}

int trobar_regio(int valor) {
if (valor > 0 && valor <= 3)
            return 1;
        else if( valor >3 && valor<=7)
            return 2;
        else if(valor>7 && valor <=11)
            return 3;
        else if(valor >11 && valor<=15)
         return 4;
      else
            return -1;
}


//Finalment utilitzem les funcions programades anteriorment per comprovar el sudoku
//En el meu cas nomes amb que es detecti una fila,columna o regio malament aquesta funcio retornara com fals el sudoku
int comprovar_sudoku(int comprovar_fila[N], int comprovar_columna[N],char sudoku[N][N]){
	int coincidencia_columna_fila=0;
	for(int i = 0;  i < N ; i++) {
		for (int j = 0; j < N ; j++) {
			int posicio = sudoku[i][j];
			//Aqui he afegit el que em vas demanar de comprobar les files/columes/regios que coincideixen amb el valor del sudoku
            if(isdigit(posicio) > 0) {

                if(posicio == j + '1'){//sumem +1 per comptar correctament files,columnes i regions
                    coincidencia_columna_fila+=1;
                }
                if(posicio == i + '1'){
                    coincidencia_columna_fila+=1;
                }
                if(sudoku[i][j]==i%4 +'1'|| sudoku[i][j]==j%4+'1'){
                    coincidencia_columna_fila+=1;
                }

            }

			if (!verificar_regio(sudoku, i, j, posicio)) {
				printf("El sudoku introduït té un error en la regió %i,%i.\nPer tant, el SUDOKU ÉS INCORRECTE.\n",trobar_regio(i),trobar_regio(j));
				printf("\n");//aqui truquem a la funcio de de trobar de regions

				return -1;
			}
            if (!verificar_columna(sudoku, i,j, posicio)) {
				printf("El sudoku introduït té un error en la columna %i.\nPer tant, el SUDOKU ÉS INCORRECTE.\n",j+1);
				printf("\n");

				return -1;
			}
            
			if (!verificar_fila(sudoku, i,j, posicio)) {
				printf("El sudoku introduït té un error en la fila %i.\nPer tant, el SUDOKU ÉS INCORRECTE.\n",i+1);
				printf("\n");

				return -1;
			}
		}
	}
	printf("El SUDOKU introduït és CORRECTE.\n");
	printf("En el sudoku es troben %i de coincidences\n",coincidencia_columna_fila);//missatge de les coincidencies
	printf("\n");
	return 0;
}