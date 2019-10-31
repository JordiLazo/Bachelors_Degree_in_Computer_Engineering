	#include<stdio.h>

	int main(){

		int n,roma=n+1,suma=0;	// roma es la variable que utilitzo per comparar les lletres introduides per l'usuari i la inicialitzare a n+1.
								//Perquè el primer valor que introduirà l'usuari actuará com si fós més petit que l'anterior.
		char c;
		bool trobat=false;

		printf("\nIntrodueixi una seqüencia de números representats en la numeració romana:\n\n");
		scanf("%c",&c);

		while(c!='\n' && !trobat){
			if(c=='M'){			//Assignem un valor a cada caracter introduït per l'usuari (sempre que perteneixi a la numeració romana).
				n=1000;
			}else if(c=='D'){
				n=500;
			}else if(c=='C'){
				n=100;
			}else if(c=='L'){
				n=50;
			}else if(c=='X'){
				n=10;
			}else if(c=='V'){
				n=5;
			}else if(c=='I'){
				n=1;
			}else{
				trobat=true; //Si algun caracter introduit per l'usuari no correspen a un número romà es sortirà del bucle i la variable booleana será true i es mostrará el missatge de error.
			}
			if(n>roma){		//Es compara el valor 'n' de cada caracter introduit per l'usuari amb el valor introduit anteriorment 'roma' i es realitzen les operacions correponents.
				suma=suma+n-roma*2;
					}else{
						suma=suma+n;
						}
			roma=n; //Per a comparar el valor actual amb el següent assignarem a 'roma' (que l'utilitzem per comparar) el valor de 'n'.
			scanf("%c",&c); //Guardem el següent caràcter que posteriorment prendrà el valor 'n' i el compararem amb l'ultim 'roma'.
		}
		if(trobat==true){
			printf("\nLa seqüència introduïda no representa números romans\n\n");
		}else{
			printf("\nEl número introduït és el: %d\n\n",suma);
		}
		return 0;
	}
