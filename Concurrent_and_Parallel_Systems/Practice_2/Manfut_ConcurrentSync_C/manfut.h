/* ---------------------------------------------------------------
Práctica 2.
Código fuente: manfut.h
Grau Informàtica
49259953W i Sergi Puigpinós Palau.
47694432E i Jordi Lazo Florensa.
--------------------------------------------------------------- */
// Players by position (7-footbal)
#define DPosPorters 1
#define DPosDefensors 3
#define DPosMitjos 2
#define DPosDelanters 1

/*
// Players by position (11-footbal)
#define  DPosPorters 1
#define  DPosDefensors 4
#define  DPosMitjos 4
#define  DPosDelanters 2
*/

typedef enum
{
	JPorter,
	JDefensor,
	JMitg,
	JDelanter
} TTipusJug;
typedef unsigned long long int TEquip;
typedef enum
{
	False,
	True
} TBoolean;

struct TJugador
{
	int id;
	char nom[100];
	TTipusJug tipus;
	int cost;
	char equip[4];
	int punts;
};
typedef struct TJugador *PtrJugador, TJugador;

struct TJugadorsEquip
{
	int Porter[DPosPorters];
	int Defensors[DPosDefensors];
	int Mitjos[DPosMitjos];
	int Delanters[DPosDelanters];
};
typedef struct TJugadorsEquip *PtrJugadorsEquip, TJugadorsEquip;

struct TBestEquip
{
	TEquip Equip;
	int Puntuacio;
	int IdEmisor;
};
typedef struct TBestEquip *PtrBestEquip, TBestEquip;

struct threadsArg
{
	TEquip first;
	TEquip end;
	long int PresupostFitxatges;
};

struct Tstatistics
{
	int numComb;
	int numInvComb;
	int numValidComb;
	float avgCostValidComb;
	float avgScoreValidComb;
	TJugadorsEquip bestCombination;
	int bestScore;
	TJugadorsEquip worseCombination;
	int worseScore;
};

#define DMaxJugadors 1000
