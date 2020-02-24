#include<stdio.h>
       
int multiplicar(int num1, int num2);

int main(){
    int x = multiplicar(13, 11);
    printf("The result of the multiplication is %d\n", x);
}

int multiplicar(int num1, int num2){

    int resultat = 0;

    while (num1 >= 1)
    {
        if (num1 % 2 != 0)
        {
            resultat = resultat + num2;
        }
        num1 = num1/2;
        num2 = num2*2;
    }
    return resultat;
}