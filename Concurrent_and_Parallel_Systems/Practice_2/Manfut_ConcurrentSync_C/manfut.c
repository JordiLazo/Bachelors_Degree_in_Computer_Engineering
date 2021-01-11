/* ---------------------------------------------------------------
Práctica 2.
Código fuente: manfut.c
Grau Informàtica
49259953W i Sergi Puigpinós Palau.
47694432E i Jordi Lazo Florensa.
--------------------------------------------------------------- */
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <math.h>
#include <limits.h>
#include <pthread.h>
#include <semaphore.h>
#include <stdbool.h>
#include "manfut.h"

#define GetPorter(j) (Jugadors[j])
#define GetDefensor(j) (Jugadors[NPorters + j])
#define GetMitg(j) (Jugadors[NPorters + NDefensors + j])
#define GetDelanter(j) (Jugadors[NPorters + NDefensors + NMitjos + j])
#define MAX_BUFFER_LENGTH 2000
#define ARRAY_SIZE 100
#define COMB_ARR_SIZE 400

char *color_red = "\033[01;31m";
char *color_green = "\033[01;32m";
char *color_blue = "\033[01;34m";
char *end_color = "\033[00m";

// Definition functions prototype
void LlegirFitxerJugadors(char *pathJugadors);
void CalcularEquipOptim(long int PresupostFitxatges, PtrJugadorsEquip MillorEquipPtr);
TJugadorsEquip *evaluateThreadFunc(void *arguments);
TBoolean ObtenirJugadorsEquip(TEquip equip, PtrJugadorsEquip jugadors);
TEquip GetEquipInicial();
TBoolean JugadorsRepetits(TJugadorsEquip jugadors);
int CostEquip(TJugadorsEquip equip);
int PuntuacioEquip(TJugadorsEquip equip);
void error(char *str);
unsigned int Log2(unsigned long long int n);
void PrintJugadors();
void PrintEquipJugadors(TJugadorsEquip equip);

//Functions added
void checkTeam(TEquip equip, TJugadorsEquip jugadors, int PresupostFitxatges, int costEquip, int puntuacioEquip, char *buffer);
void calculateStatistics(TJugadorsEquip jugadors, int costEquip, int puntuacioEquip, struct Tstatistics *statistics, int PresupostFitxatges);
void calculateGlobalStatistics(struct Tstatistics statistics);
void printStatistics(struct Tstatistics statistics, char *buffer);
void printGlobalStatistics(char *buffer);
void printMessages();
void addMessageToQueue(char *message);
void forcePrint();
void messengerThreadFunc();
void toStringEquipJugadors(TJugadorsEquip equip, char *outpString, char *buffer);
void killMessenger();
void statisticsSummary(struct Tstatistics statistics, char *buffer);
void cleanStatisticsStructure(struct Tstatistics *statistics);

// Global variables definition
TJugador Jugadors[DMaxJugadors];
int NJugadors, NPorters, NDefensors, NMitjos, NDelanters;
char cad[256];

//Shared variables READ-ONLY
int threadsWaitingSummary = 0;
struct Tstatistics globalStatistics;
int M = 25000;
int numOfThreads;

//Shared variables
TJugadorsEquip MillorEquip;
int MaxPuntuacio = -1;
int threadsFinished;
bool finalPrint = false;

//Messenger variables
char messageArray[ARRAY_SIZE][MAX_BUFFER_LENGTH];
int messageArrayIndx = 0;
bool exitMess = false;

//Mutex & locks
pthread_mutex_t checkTeamLock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t evaluatorLock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t messengerArrayLock = PTHREAD_MUTEX_INITIALIZER;

//Conditions
pthread_cond_t itemAdded = PTHREAD_COND_INITIALIZER;
pthread_cond_t evaluatorsEnded = PTHREAD_COND_INITIALIZER;
pthread_cond_t messengerEnded = PTHREAD_COND_INITIALIZER;

//Semaphore
sem_t messengerSemaphore;

//Barrier
pthread_barrier_t evaluatorEntranceBarrier;
pthread_barrier_t evaluatorExitBarrier;

int main(int argc, char *argv[])
{
	TJugadorsEquip AuxEquip, MillorEquipRtn;
	long int PresupostFitxatges;

	float IntervalBegin = -1, IntervalEnd = -1;

	if (argc < 3)
		error("Error in arguments: ManFut <presupost> <fitxer_jugadors> <num_threads>");

	if (argc > 1)
		PresupostFitxatges = atoi(argv[1]);

	if (argc > 2)
		LlegirFitxerJugadors(argv[2]);

	numOfThreads = atoi(argv[3]);
	if (numOfThreads <= 0)
		error("Invalid number of Threads");

	if (argc > 4)
	{
		M = atoi(argv[4]);
		if (M <= 0)
			error("Error in arguments: Invalid M");
	}

	// Calculate the best team.
	CalcularEquipOptim(PresupostFitxatges, &MillorEquipRtn);
	write(1, color_blue, strlen(color_blue));
	write(1, "-- Best Team -------------------------------------------------------------------------------------\n", strlen("-- Best Team -------------------------------------------------------------------------------------\n"));
	PrintEquipJugadors(MillorEquipRtn);
	sprintf(cad, "   Cost %d, Points: %d.\n", CostEquip(MillorEquipRtn), PuntuacioEquip(MillorEquipRtn));
	write(1, cad, strlen(cad));
	write(1, "-----------------------------------------------------------------------------------------------------\n", strlen("-----------------------------------------------------------------------------------------------------\n"));
	write(1, end_color, strlen(end_color));

	exit(0);
}

// Read file with the market players list (each line containts a plater: "Id.;Name;Position;Cost;Team;Points")
void LlegirFitxerJugadors(char *pathJugadors)
{
	char buffer[256], tipus[10];
	int fdin;
	int nofi;

	if ((fdin = open(pathJugadors, O_RDONLY)) < 0)
		error("Error opening input file.");

	// Read players.
	NJugadors = NPorters = NDefensors = NMitjos = NDelanters = 0;
	do
	{
		int x = 0, i, f;

		while ((nofi = read(fdin, &buffer[x], 1)) != 0 && buffer[x++] != '\n')
			;
		buffer[x] = '\0';

		if (buffer[0] == '#')
			continue;

		// Player's identificator
		i = 0;
		for (f = 0; buffer[f] != ';'; f++)
			;
		buffer[f] = 0;
		Jugadors[NJugadors].id = atoi(&(buffer[i]));

		// Player's name
		i = ++f;
		for (; buffer[f] != ';'; f++)
			;
		buffer[f] = 0;
		strcpy(Jugadors[NJugadors].nom, &(buffer[i]));

		// Player's position
		i = ++f;
		for (; buffer[f] != ';'; f++)
			;
		buffer[f] = 0;
		if (strcmp(&(buffer[i]), "Portero") == 0)
		{
			NPorters++;
			Jugadors[NJugadors].tipus = JPorter;
		}
		else if (strcmp(&(buffer[i]), "Defensa") == 0)
		{
			NDefensors++;
			Jugadors[NJugadors].tipus = JDefensor;
		}
		else if (strcmp(&(buffer[i]), "Medio") == 0)
		{
			NMitjos++;
			Jugadors[NJugadors].tipus = JMitg;
		}
		else if (strcmp(&(buffer[i]), "Delantero") == 0)
		{
			NDelanters++;
			Jugadors[NJugadors].tipus = JDelanter;
		}
		else
			error("Error player type.");

		// Player's cost
		i = ++f;
		for (f = 0; buffer[f] != ';'; f++)
			;
		buffer[f] = 0;
		Jugadors[NJugadors].cost = atoi(&(buffer[i]));

		// Player's team
		i = ++f;
		for (f = 0; buffer[f] != ';'; f++)
			;
		buffer[f] = 0;
		strcpy(Jugadors[NJugadors].equip, &(buffer[i]));

		// Player's points
		i = ++f;
		for (f = 0; buffer[f] != '\n'; f++)
			;
		buffer[f] = 0;
		Jugadors[NJugadors].punts = atoi(&(buffer[i]));

		NJugadors++;
	} while (nofi);

	sprintf(cad, "Number of players: %d, Port:%d, Def:%d, Med:%d, Del:%d.\n", NJugadors, NPorters, NDefensors, NMitjos, NDelanters);
	write(1, cad, strlen(cad));

	close(fdin);
}

void CalcularEquipOptim(long int PresupostFitxatges, PtrJugadorsEquip MillorEquipRtn)
{
	unsigned int maxbits;
	TEquip equip, primerEquip, ultimEquip, first, end;
	struct threadsArg *args[numOfThreads];
	pthread_t TArray[numOfThreads];
	pthread_t messengerThread;

	// Calculated number of bits required for all teams codification.
	maxbits = Log2(NPorters) * DPosPorters + Log2(NDefensors) * DPosDefensors + Log2(NMitjos) * DPosMitjos + Log2(NDelanters) * DPosDelanters;
	if (maxbits > Log2(ULLONG_MAX))
		error("The number of player overflow the maximum width supported.");

	// Calculate first and end team that have to be evaluated.
	first = primerEquip = GetEquipInicial();
	end = ultimEquip = pow(2, maxbits);

	sprintf(cad, "MAIN; First: %lld ; End: %lld \n", first, end);
	write(1, cad, strlen(cad));

	// Evaluating different teams/combinations.
	sprintf(cad, "Evaluating form %llXH to %llXH (Maxbits: %d). Evaluating %lld teams...\n", first, end, maxbits, end - first);
	write(1, cad, strlen(cad));

	int remaining = ultimEquip - primerEquip;

	//Initialize barrier and semaphore
	if (pthread_barrier_init(&evaluatorEntranceBarrier, NULL, numOfThreads))
		error("Error while trying to create the barrier");

	if (pthread_barrier_init(&evaluatorExitBarrier, NULL, numOfThreads))
		error("Error while trying to create the barrier");

	if (sem_init(&messengerSemaphore, 0, ARRAY_SIZE) != 0)
		error("Error while trying to create the semaphore");

	//Start messenger thread
	if (pthread_create(&messengerThread, NULL, (void *)messengerThreadFunc, NULL) != 0)
		error("Error while trying to create the messenger thread");

	//Start threads
	for (int i = 0; i < numOfThreads; ++i)
	{
		args[i] = malloc(sizeof(struct threadsArg));
		int pivot = remaining / (numOfThreads - i);
		int prevThreadEnd = i == 0 ? first : args[i - 1]->end + 1;
		int threadEnd = i == numOfThreads - 1 ? end : prevThreadEnd + pivot;
		args[i]->first = prevThreadEnd;
		args[i]->end = threadEnd;
		args[i]->PresupostFitxatges = PresupostFitxatges;
		if (pthread_create(&TArray[i], NULL, (void *)evaluateThreadFunc, (void *)args[i]) != 0)
			error("Error while trying to create the evaluator's threads");
		remaining -= pivot;
	}

	//Wait for the ending signal
	pthread_mutex_lock(&evaluatorLock);
	pthread_cond_wait(&evaluatorsEnded, &evaluatorLock);
	pthread_mutex_unlock(&evaluatorLock);

	//wait for messenger to print all and exit
	pthread_mutex_lock(&messengerArrayLock);
	killMessenger();
	pthread_cond_wait(&messengerEnded, &messengerArrayLock);
	pthread_mutex_unlock(&messengerArrayLock);

	//Pass the final value
	memcpy(MillorEquipRtn, &MillorEquip, sizeof(TJugadorsEquip));

	//Destroy semaphore
	if (sem_destroy(&messengerSemaphore) != 0)
		error("Error while trying to destroy the messenger semaphore");

	//Destroy barrier
	if (pthread_barrier_destroy(&evaluatorEntranceBarrier) != 0)
		error("Error while trying to destroy the evaluator's barrier");

	//Destroy conditions
	if (pthread_cond_destroy(&itemAdded) != 0)
		error("Error while trying to destroy the itemAdded condition");
	if (pthread_cond_destroy(&evaluatorsEnded) != 0)
		error("Error while trying to destroy the evaluatorsEnded condition");
	if (pthread_cond_destroy(&messengerEnded) != 0)
		error("Error while trying to destroy the messengerEnded condition");

	//Destroy mutex
	if (pthread_mutex_destroy(&checkTeamLock) != 0)
		error("Error while trying to destroy checkTeamLock");
	if (pthread_mutex_destroy(&evaluatorLock) != 0)
		error("Error while trying to destroy evaluatorLock");
	if (pthread_mutex_destroy(&messengerArrayLock) != 0)
		error("Error while trying to destroy messengerArrayLock");
}

//Evaluators Functions

TJugadorsEquip *evaluateThreadFunc(void *arguments)
{
	char buffer[MAX_BUFFER_LENGTH]; //Buffer for each thread so they don't overlap messages, to keep all the locks inside the same function and to keep it similar to java
	struct threadsArg *args = arguments;
	struct Tstatistics statistics;
	statistics.numComb = 0;
	TEquip equip;

	sprintf(buffer, "Thread: %lu ; First: %lld ; End: %lld \n", pthread_self(), args->first, args->end);
	addMessageToQueue(buffer);

	for (equip = args->first; equip <= args->end; equip++)
	{
		TJugadorsEquip jugadors;

		statistics.numComb++;

		if (statistics.numComb % M == 0)
			statisticsSummary(statistics, buffer);

		// Get playes from team number. Returns false if the team is not valid.
		if (!ObtenirJugadorsEquip(equip, &jugadors))
		{
			statistics.numInvComb++;
			continue;
		}

		// Reject teams with repeated players.
		if (JugadorsRepetits(jugadors))
		{
			statistics.numInvComb++;
			//sprintf(cad,"%s Invalid.\r%s", color_red, end_color);
			//addMessageToQueue(cad);
			continue; // Equip no valid.
		}

		//Store repeated operations into variables
		int costEquip = CostEquip(jugadors);
		int puntuacioEquip = PuntuacioEquip(jugadors);

		//Check Team
		pthread_mutex_lock(&checkTeamLock);
		checkTeam(equip, jugadors, args->PresupostFitxatges, costEquip, puntuacioEquip, buffer);
		pthread_mutex_unlock(&checkTeamLock);

		calculateStatistics(jugadors, costEquip, puntuacioEquip, &statistics, args->PresupostFitxatges);
	}

	threadsFinished++;
	while (threadsFinished <= numOfThreads)
	{
		if (threadsFinished == numOfThreads)
			finalPrint = true;
		statisticsSummary(statistics, buffer);
	}

	free(arguments);
}

void statisticsSummary(struct Tstatistics statistics, char *buffer)
{
	//Wait for threads using a barrier
	int rc = pthread_barrier_wait(&evaluatorEntranceBarrier);
	if (rc != 0 && rc != PTHREAD_BARRIER_SERIAL_THREAD)
		error("Can't wait for barrier");

	//Print global statistics; send signal to parent
	pthread_mutex_lock(&evaluatorLock);
	printStatistics(statistics, buffer);
	calculateGlobalStatistics(statistics);
	//Last thread prints global and sends signal to parent
	if (threadsWaitingSummary == numOfThreads - 1)
	{
		printGlobalStatistics(buffer);
		threadsWaitingSummary = 0;
		cleanStatisticsStructure(&globalStatistics);
		if (finalPrint)
		{
			threadsFinished++;
			pthread_cond_signal(&evaluatorsEnded);
		}
	}
	else
	{
		threadsWaitingSummary++;
	}
	pthread_mutex_unlock(&evaluatorLock);

	//Wait for threads using a barrier
	int rc2 = pthread_barrier_wait(&evaluatorExitBarrier);
	if (rc2 != 0 && rc2 != PTHREAD_BARRIER_SERIAL_THREAD)
		error("Can't wait for barrier");
}

void cleanStatisticsStructure(struct Tstatistics *statistics)
{
	statistics->numComb = 0;
	statistics->numInvComb = 0;
	statistics->numValidComb = 0;
	statistics->avgCostValidComb = 0;
	statistics->avgScoreValidComb = 0;
}

void checkTeam(TEquip equip, TJugadorsEquip jugadors, int PresupostFitxatges, int costEquip, int puntuacioEquip, char *buffer)
{
	// Chech if the team points is bigger than current optimal team, then evaluate if the cost is lower than the available budget
	if (puntuacioEquip > MaxPuntuacio && costEquip < PresupostFitxatges)
	{
		// We have a new partial optimal team.
		MaxPuntuacio = puntuacioEquip;
		MillorEquip = jugadors;
		sprintf(buffer, "%s Team %lld -> Cost: %d  Points: %d. %s\n", color_green, equip, costEquip, puntuacioEquip, end_color);
	}
	else
	{
		sprintf(buffer, "Team %lld -> Cost: %d  Points: %d. \r", equip, costEquip, puntuacioEquip);
	}
	addMessageToQueue(buffer);
}

void calculateStatistics(TJugadorsEquip jugadors, int costEquip, int puntuacioEquip, struct Tstatistics *statistics, int PresupostFitxatges)
{
	statistics->avgCostValidComb = ((statistics->avgCostValidComb * statistics->numValidComb) + costEquip) / (statistics->numValidComb + 1);
	statistics->avgScoreValidComb = ((statistics->avgScoreValidComb * statistics->numValidComb) + puntuacioEquip) / (statistics->numValidComb + 1);
	statistics->numValidComb++;
	if (puntuacioEquip > statistics->bestScore && costEquip < PresupostFitxatges)
	{ //Best combination
		statistics->bestScore = puntuacioEquip;
		statistics->bestCombination = jugadors;
	}
	else if (statistics->worseScore == 0 || puntuacioEquip < statistics->worseScore)
	{ //Worse combination
		statistics->worseScore = puntuacioEquip;
		statistics->worseCombination = jugadors;
	}
}

void calculateGlobalStatistics(struct Tstatistics statistics)
{
	globalStatistics.numComb += statistics.numComb;
	globalStatistics.numInvComb += statistics.numInvComb;
	if (statistics.numValidComb != 0)
	{
		globalStatistics.avgCostValidComb = ((globalStatistics.avgCostValidComb * globalStatistics.numValidComb) + (statistics.avgCostValidComb * statistics.numValidComb)) / (globalStatistics.numValidComb + statistics.numValidComb);
		globalStatistics.avgScoreValidComb = ((globalStatistics.avgScoreValidComb * globalStatistics.numValidComb) + (statistics.avgScoreValidComb * statistics.numValidComb)) / (globalStatistics.numValidComb + statistics.numValidComb);
		globalStatistics.numValidComb += statistics.numValidComb;
		if (globalStatistics.bestScore == 0 || statistics.bestScore > globalStatistics.bestScore)
		{ //Best combination regarding points
			globalStatistics.bestScore = statistics.bestScore;
			globalStatistics.bestCombination = statistics.bestCombination;
		}
		if (globalStatistics.worseScore == 0 || (statistics.worseScore < globalStatistics.worseScore && statistics.worseScore != 0))
		{ //Worse combination regarding points
			globalStatistics.worseScore = statistics.worseScore;
			globalStatistics.worseCombination = statistics.worseCombination;
		}
	}
}

void printStatistics(struct Tstatistics statistics, char *buffer)
{
	char bestComb[COMB_ARR_SIZE];
	char worseComb[COMB_ARR_SIZE];
	toStringEquipJugadors(statistics.bestCombination, bestComb, buffer);
	toStringEquipJugadors(statistics.worseCombination, worseComb, buffer);
	sprintf(buffer, "*******THREAD %lu STATISTICS******\
	 	\nNúmero de Combinaciones evaluadas: %d \
		\nNúmero de combinaciones no válidas: %d \
		\nCoste promedio de las combinaciones válidas: %f \
		\nPuntuación promedio de las combinaciones válidas: %f \
		\nMejor combinación (desde el punto de vista de la puntuación):\n%s   Cost %d, Points: %d. \
		\nPeor combinación (desde el punto de vista de la puntuación):\n%s   Cost %d, Points: %d. \
		\n********************************************************\n",
			pthread_self(), statistics.numComb, statistics.numInvComb, statistics.avgCostValidComb, statistics.avgScoreValidComb, bestComb, CostEquip(statistics.bestCombination), PuntuacioEquip(statistics.bestCombination), worseComb, CostEquip(statistics.worseCombination), PuntuacioEquip(statistics.worseCombination));
	addMessageToQueue(buffer);
}

void printGlobalStatistics(char *buffer)
{
	char bestComb[COMB_ARR_SIZE];
	char worseComb[COMB_ARR_SIZE];
	toStringEquipJugadors(globalStatistics.bestCombination, bestComb, buffer);
	toStringEquipJugadors(globalStatistics.worseCombination, worseComb, buffer);
	sprintf(buffer, "\033[01;34m*******GLOBAL  STATISTICS******\
	 	\nNúmero de Combinaciones evaluadas: %d \
		\nNúmero de combinaciones no válidas: %d \
		\nCoste promedio de las combinaciones válidas: %f \
		\nPuntuación promedio de las combinaciones válidas: %f \
		\nMejor combinación (desde el punto de vista de la puntuación):\n%s   Cost %d, Points: %d. \
		\nPeor combinación (desde el punto de vista de la puntuación):\n%s   Cost %d, Points: %d. \
		\n********************************************************\n \033[00m",
			globalStatistics.numComb, globalStatistics.numInvComb, globalStatistics.avgCostValidComb, globalStatistics.avgScoreValidComb, bestComb, CostEquip(globalStatistics.bestCombination), PuntuacioEquip(globalStatistics.bestCombination), worseComb, CostEquip(globalStatistics.worseCombination), PuntuacioEquip(globalStatistics.worseCombination));
	addMessageToQueue(buffer);
}

//Messenger Functions

void printMessages()
{
	for (int i = 0; i < ARRAY_SIZE; i++)
	{
		write(1, messageArray[i], strlen(messageArray[i]));
	}
	memset(messageArray, 0, sizeof messageArray);
	messageArrayIndx = 0;
}

void addMessageToQueue(char *message)
{
	sem_wait(&messengerSemaphore);
	pthread_mutex_lock(&messengerArrayLock);
	strcpy(messageArray[messageArrayIndx], message);
	messageArrayIndx++;
	pthread_cond_signal(&itemAdded);
	pthread_mutex_unlock(&messengerArrayLock);
}

void killMessenger()
{
	exitMess = true;
	pthread_cond_signal(&itemAdded);
}

void messengerThreadFunc()
{
	while (!exitMess)
	{
		pthread_mutex_lock(&messengerArrayLock);
		//Wait until list is full
		while (messageArrayIndx != ARRAY_SIZE && !exitMess)
			pthread_cond_wait(&itemAdded, &messengerArrayLock);
		if (messageArrayIndx > 0)
		{ //In case of exit
			printMessages();
			//Release
			for (int i = 0; i < ARRAY_SIZE; i++)
			{
				sem_post(&messengerSemaphore);
			}
		}
		pthread_mutex_unlock(&messengerArrayLock);
	}
	pthread_cond_signal(&messengerEnded);
}

// Calculate the initial team combination.
TEquip
GetEquipInicial()
{
	int p;
	TEquip equip = 0, equip2 = 0;
	unsigned bitsPorters, bitsDefensors, bitsMitjos, bitsDelanters;

	bitsPorters = Log2(NPorters);
	bitsDefensors = Log2(NDefensors);
	bitsMitjos = Log2(NMitjos);
	bitsDelanters = Log2(NDelanters);

	for (p = DPosDelanters - 1; p >= 0; p--)
	{
		equip += p;
		equip = equip << bitsDelanters;
	}

	for (p = DPosMitjos - 1; p >= 0; p--)
	{
		equip += p;
		equip = equip << bitsMitjos;
	}

	for (p = DPosDefensors - 1; p >= 0; p--)
	{
		equip += p;
		equip = equip << bitsDefensors;
	}

	for (p = DPosPorters - 1; p > 0; p--)
	{
		equip += p;
		equip = equip << bitsPorters;
	}

	return (equip);
}

// Convert team combinatio to an struct with all the player by position.
// Returns false if the team is not valid.

TBoolean
ObtenirJugadorsEquip(TEquip equip, PtrJugadorsEquip jugadors)
{
	int p;
	unsigned bitsPorters, bitsDefensors, bitsMitjos, bitsDelanters;

	bitsPorters = Log2(NPorters);
	bitsDefensors = Log2(NDefensors);
	bitsMitjos = Log2(NMitjos);
	bitsDelanters = Log2(NDelanters);

	for (p = 0; p < DPosPorters; p++)
	{
		jugadors->Porter[p] = (equip >> (bitsPorters * p)) & ((int)pow(2, bitsPorters) - 1);
		if (jugadors->Porter[p] >= NPorters)
			return False;
	}

	for (p = 0; p < DPosDefensors; p++)
	{
		jugadors->Defensors[p] = (equip >> ((bitsPorters * DPosPorters) + (bitsDefensors * p))) & ((int)pow(2, bitsDefensors) - 1);
		if (jugadors->Defensors[p] >= NDefensors)
			return False;
	}

	for (p = 0; p < DPosMitjos; p++)
	{
		jugadors->Mitjos[p] = (equip >> ((bitsPorters * DPosPorters) + (bitsDefensors * DPosDefensors) + (bitsMitjos * p))) & ((int)pow(2, bitsMitjos) - 1);
		if (jugadors->Mitjos[p] >= NMitjos)
			return False;
	}

	for (p = 0; p < DPosDelanters; p++)
	{
		jugadors->Delanters[p] = (equip >> ((bitsPorters * DPosPorters) + (bitsDefensors * DPosDefensors) + (bitsMitjos * DPosMitjos) + (bitsDelanters * p))) & ((int)pow(2, bitsDelanters) - 1);
		if (jugadors->Delanters[p] >= NDelanters)
			return False;
	}

	return True;
}

// Check if the team have any repeated player.
// Returns true if the team have repeated players.

TBoolean
JugadorsRepetits(TJugadorsEquip jugadors)
{
	// Returns True if the equip have some repeated players (is not valid).
	int i, j;

	// Porters.
	for (i = 0; i < DPosPorters - 1; i++)
		for (j = i + 1; j <= DPosPorters - 1; j++)
			if (jugadors.Porter[i] == jugadors.Porter[j])
				return True;

	// Defensors.
	for (i = 0; i < DPosDefensors - 1; i++)
		for (j = i + 1; j <= DPosDefensors - 1; j++)
			if (jugadors.Defensors[i] == jugadors.Defensors[j])
				return True;

	// Mitjos.
	for (i = 0; i < DPosMitjos - 1; i++)
		for (j = i + 1; j <= DPosMitjos - 1; j++)
			if (jugadors.Mitjos[i] == jugadors.Mitjos[j])
				return True;

	// Delanters
	for (i = 0; i < DPosDelanters - 1; i++)
		for (j = i + 1; j <= DPosDelanters - 1; j++)
			if (jugadors.Delanters[i] == jugadors.Delanters[j])
				return True;

	return False;
}

// Calculates the team cost adding the individual cost of all team players.
// Returns the cost.

int CostEquip(TJugadorsEquip equip)
{
	int x;
	int cost = 0;

	for (x = 0; x < DPosPorters; x++)
		cost += GetPorter(equip.Porter[x]).cost;

	for (x = 0; x < DPosDefensors; x++)
		cost += GetDefensor(equip.Defensors[x]).cost;

	for (x = 0; x < DPosMitjos; x++)
		cost += GetMitg(equip.Mitjos[x]).cost;

	for (x = 0; x < DPosDelanters; x++)
		cost += GetDelanter(equip.Delanters[x]).cost;

	return (cost);
}

// Calculates the team points adding the individual points of all team players.
// Returns the points.

int PuntuacioEquip(TJugadorsEquip equip)
{
	int x;
	int punts = 0;

	for (x = 0; x < DPosPorters; x++)
		punts += GetPorter(equip.Porter[x]).punts;

	for (x = 0; x < DPosDefensors; x++)
		punts += GetDefensor(equip.Defensors[x]).punts;

	for (x = 0; x < DPosMitjos; x++)
		punts += GetMitg(equip.Mitjos[x]).punts;

	for (x = 0; x < DPosDelanters; x++)
		punts += GetDelanter(equip.Delanters[x]).punts;

	return (punts);
}

// Prints an error message.ç

void error(char *str)
{
	char s[255];

	sprintf(s, "[%d] ManFut: %s (%s))\n", getpid(), str, strerror(errno));
	write(2, s, strlen(s));
	exit(1);
}

// Rounded log2

unsigned int Log2(unsigned long long int n)
{
	return (ceil(log2((double)n)));
}

// Prints all market players information,

void PrintJugadors()
{
	int j;

	for (j = 0; j < NJugadors; j++)
	{
		sprintf(cad, "Jugador: %s (%d), Posició: %d, Cost: %d, Puntuació: %d.\n", Jugadors[j].nom, Jugadors[j].id, Jugadors[j].tipus, Jugadors[j].cost, Jugadors[j].punts);
		write(1, cad, strlen(cad));
	}
}

// Prints team players.
void PrintEquipJugadors(TJugadorsEquip equip)
{
	int x;

	write(1, "   Porters: ", strlen("   Porters: "));
	for (x = 0; x < DPosPorters; x++)
	{
		sprintf(cad, "%s (%d/%d), ", GetPorter(equip.Porter[x]).nom, GetPorter(equip.Porter[x]).cost, GetPorter(equip.Porter[x]).punts);
		write(1, cad, strlen(cad));
	}
	write(1, "\n", strlen("\n"));

	write(1, "   Defenses: ", strlen("   Defenses: "));
	for (x = 0; x < DPosDefensors; x++)
	{
		sprintf(cad, "%s (%d/%d), ", GetDefensor(equip.Defensors[x]).nom, GetDefensor(equip.Defensors[x]).cost, GetDefensor(equip.Defensors[x]).punts);
		write(1, cad, strlen(cad));
	}
	write(1, "\n", strlen("\n"));

	write(1, "   Mitjos: ", strlen("   Mitjos: "));
	for (x = 0; x < DPosMitjos; x++)
	{
		sprintf(cad, "%s (%d/%d), ", GetMitg(equip.Mitjos[x]).nom, GetMitg(equip.Mitjos[x]).cost, GetMitg(equip.Mitjos[x]).punts);
		write(1, cad, strlen(cad));
	}
	write(1, "\n", strlen("\n"));

	write(1, "   Delanters: ", strlen("   Delanters: "));
	for (x = 0; x < DPosDelanters; x++)
	{
		sprintf(cad, "%s (%d/%d), ", GetDelanter(equip.Delanters[x]).nom, GetDelanter(equip.Delanters[x]).cost, GetDelanter(equip.Delanters[x]).punts);
		write(1, cad, strlen(cad));
	}
	write(1, "\n", strlen("\n"));
}

void toStringEquipJugadors(TJugadorsEquip equip, char *outpString, char *buffer)
{
	int x;
	char rtnString[COMB_ARR_SIZE];

	strcpy(rtnString, "   Porters: ");
	for (x = 0; x < DPosPorters; x++)
	{
		sprintf(buffer, "%s (%d/%d), ", GetPorter(equip.Porter[x]).nom, GetPorter(equip.Porter[x]).cost, GetPorter(equip.Porter[x]).punts);
		strcat(rtnString, buffer);
	}
	strcat(rtnString, "\n");

	strcat(rtnString, "   Defenses: ");
	for (x = 0; x < DPosDefensors; x++)
	{
		sprintf(buffer, "%s (%d/%d), ", GetDefensor(equip.Defensors[x]).nom, GetDefensor(equip.Defensors[x]).cost, GetDefensor(equip.Defensors[x]).punts);
		strcat(rtnString, buffer);
	}
	strcat(rtnString, "\n");

	strcat(rtnString, "   Mitjos: ");
	for (x = 0; x < DPosMitjos; x++)
	{
		sprintf(buffer, "%s (%d/%d), ", GetMitg(equip.Mitjos[x]).nom, GetMitg(equip.Mitjos[x]).cost, GetMitg(equip.Mitjos[x]).punts);
		strcat(rtnString, buffer);
	}
	strcat(rtnString, "\n");

	strcat(rtnString, "   Delanters: ");
	for (x = 0; x < DPosDelanters; x++)
	{
		sprintf(buffer, "%s (%d/%d), ", GetDelanter(equip.Delanters[x]).nom, GetDelanter(equip.Delanters[x]).cost, GetDelanter(equip.Delanters[x]).punts);
		strcat(rtnString, buffer);
	}
	strcat(rtnString, "\n");

	for (int i = 0; i < COMB_ARR_SIZE; i++)
	{
		outpString[i] = rtnString[i];
	}
}