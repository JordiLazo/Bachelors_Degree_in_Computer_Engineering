;***Universitat de Lleida***
;***Arquitectura de Computadors***
;***Pràctica processament segmentat***
;***comparar_major_delay.s*** 
;***Alejandro Clavera Poza i Jordi Rafael Lazo Florensa***




		.data
A:		.word 10, 2, 9, 12, 43, 152, 2, 4, 18, 3
X:		.word 20
NUM:	.word 0
RES:	.word 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

	.text
main:
	ld r2, X(r0)
	ld r3, NUM(r0)
	and r1, r0, r0 
	daddi r6, r6, 80
	ld r4, A(r1)
bucle:
	slt r5, r2, r4
	dadd r3, r3, r5
	sd r5, RES(r1)
	daddi r1, r1, 8
	bne r1, r6, bucle
	ld r4, A(r1)
	sd r3, NUM(r0)
	halt       
