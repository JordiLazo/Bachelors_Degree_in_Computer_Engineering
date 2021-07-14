#include <stdio.h>
#include <stdlib.h>

#define N 100000

int main(int argc, char ** argv){
	int i;

	FILE *f = fopen(argv[1], "w");
	for(i = 0; i < N; i++) {
		fprintf(f, "%i", i);
		printf("Number: %i\n",i);
	}
	fclose(f);
}
