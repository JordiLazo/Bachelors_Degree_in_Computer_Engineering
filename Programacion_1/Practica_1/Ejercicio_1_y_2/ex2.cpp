#include<stdio.h>
#include<time.h>
#include<stdlib.h>

int main(){

int n1=0,n2=0,suma=0,quantitat,apostes,capital=1000; //Introduim les varibles.
int guanyat=0,perdut=0;
bool trobat=false;

srand(time(NULL)); //Generació de números aleatoris.

printf("\nBenvingut al joc de imparell o parell!\n");

while(!trobat){
        if(capital<=0){ //Es el recompte final si l'usuari juga fins que perd tot el capital.
          trobat=true;
          printf("\nHas perdut tot el teu capital!\n");
          printf("\nRecompte final: has guanyat %i partida/es i has perdut %i partida/es.\n\n",guanyat,perdut);
        }else{
          printf("\nEl teu capital es de %i €, quina quantitat vol apostar?\n\n",capital);
          scanf("%i",&quantitat);
            if(quantitat>capital){//Si la quantitat es incorrecta.
              printf("\nEl teu capital es inferior a la quantitat introduïda, sisplau intrdueix una quantitat vàlida.\n");
            }else if(quantitat==0){//Es el missatge quan l'usuari introdueix un 0 quan té que apostar(retirada).
                trobat = true;                
                printf("\nEl teu capital final es de %i €\n",capital);
                printf("\nHas guanyat %i partida/es i has perdut %i partida/des.\n",guanyat,perdut);
            }else{//Aposta per jugar i inicialitzar la geeneració de números aleatoris.
                printf("\nIntrodueix un número per realitzar la vostra aposta: \n\n1 = imparell \n2 = parell\n\n");
                scanf("%i",&apostes);
                n1 = rand() % 6+1;//Generació de números aleatoris.
                n2 = rand() % 6+1;
                suma = n2+n1;
                  if(apostes==2){
                    printf("\nHas apostat %i € a 2 (parell).\n",quantitat);
                  }else{
                    printf("\nHas apostat %i € a 1 (imparell).\n",quantitat);
                      }
                    printf("\nHa sortit el %i (%i+%i)\n",suma,n1,n2);
                  if((suma%2==0 && apostes==2) || (suma%2!=0 && apostes==1)){ //Generació del resultat final (si has guanyat o has perdut).
                    capital += quantitat;
                    printf("\nHas guanyat %i €!\n",quantitat);
                    guanyat++;
                  }else{
                    capital -= quantitat;
                    printf("\nHas perdut %i €\n",quantitat);
                    perdut++;
                      }
                  }
              }
            }
return 0;
}
