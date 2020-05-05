#define _POSIX_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <time.h>
#include <unistd.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <signal.h>
#include <pthread.h>

int REG_REQ = 0;
int REG_INFO = 1;
int REG_ACK = 2;
int INFO_ACK = 3;
int REG_NACK = 4;
int INFO_NACK = 5;
int REG_REJ = 6;
int ALIVE = 16;
int ALIVE_REJ = 17; 
int DISCONNECTED = 160;
int NOT_REGISTERED = 161;
int WAIT_ACK_REG = 162;
int WAIT_INFO = 163;
int WAIT_ACK_INFO = 164;
int REGISTERED = 165;
int SEND_ALIVE = 166;
int debug = 0;
char id_servidor[32]="";
int udp_port = 0;
int tcp_port = 0;

struct dades_client{
    int estatus_client;
    char id_client[12];
    char dispositius_clients[16*8];
    int aleatori;
    int alive_sense_resposta;
    int alive_rebut;
    struct sockaddr_in adreca_udp_client;
};
struct dades_client array_clients[50];

int random_number(){
    return (rand() % (99999999 - 10000000 + 1)) + 10000000;
}

int random_UDP_port(){
    return (rand() % (65000 - 1024 + 1)) + 1024;
}

volatile int esperar_thread_registre = 0;
pthread_t nom_thread_esperar_registre;

volatile int thread_gestionar_alive = 0;
pthread_t nom_thread_gestionar_alive;


struct paquets_clients_udp{
    unsigned char tipus;
    char id_client_paquet[13];
    char numero_aleatori_paquet[9];
    char dades_paquets[61];
};
int socket_udp;

void *funcio_tractar_paquet(void *args){
    fd_set esperar_nou_paquet;
    struct timeval temps_paquet;
    int resultat_select;
    int i = 0;
    int aleatori,nou_port_udp;
    int nou_socket_udp;
    int llargada = sizeof(struct sockaddr_in);
    char array_aleatori[9], array_nou_port_udp[5];
    struct sockaddr_in nova_adreca_servidor;
    struct sockaddr_in adreca_client;
    struct paquets_clients_udp enviar_paquets,rebre_paquets;
    fflush(stdout);
    recvfrom(socket_udp,&rebre_paquets,84,MSG_WAITALL,(struct sockaddr *) &adreca_client,(socklen_t *)&llargada);
    if(rebre_paquets.tipus == REG_REQ){
        while(i < 50){
            rebre_paquets.id_client_paquet[12] = '\0';
            if(strcmp(array_clients[i].id_client,rebre_paquets.id_client_paquet) == 0){
                
                if(array_clients[i].estatus_client == DISCONNECTED || array_clients[i].estatus_client == NOT_REGISTERED){
                    
                    if(strcmp(rebre_paquets.dades_paquets,"") == 0 && strcmp(rebre_paquets.numero_aleatori_paquet,"00000000\0") == 0 ){
                        array_clients[i].estatus_client = WAIT_ACK_REG;
                        aleatori = random_number();
                        array_clients[i].adreca_udp_client = adreca_client;
                        nou_port_udp = random_UDP_port();
                        nova_adreca_servidor.sin_family = AF_INET;
                        nova_adreca_servidor.sin_port = htons(nou_port_udp);
                        nova_adreca_servidor.sin_addr.s_addr = INADDR_ANY;
                        nou_socket_udp = socket(AF_INET,SOCK_DGRAM,0);
                        while (bind(nou_socket_udp,(const struct sockaddr*)&nova_adreca_servidor,sizeof(nova_adreca_servidor)) < 0){
                            nou_port_udp = random_UDP_port();
                            nova_adreca_servidor.sin_port = htons(nou_port_udp);
                        }
                        sprintf((char *) array_aleatori,"%i",aleatori);
                        array_aleatori[8] = '\0';
                        sprintf((char *) array_nou_port_udp,"%i",nou_port_udp);
                        array_nou_port_udp[4] = '\0';
                        array_clients[i].aleatori = aleatori;
                        enviar_paquets.tipus = REG_ACK;
                        strcpy(enviar_paquets.id_client_paquet,id_servidor);
                        strcpy(enviar_paquets.dades_paquets,array_nou_port_udp);
                        strcpy(enviar_paquets.numero_aleatori_paquet,array_aleatori);
                        sendto(socket_udp,(struct paquets_clients_udp *) &enviar_paquets,84,MSG_CONFIRM,(struct sockaddr *)&adreca_client,sizeof(adreca_client));
                        FD_ZERO(&esperar_nou_paquet);
                        FD_SET(nou_socket_udp,&esperar_nou_paquet);
                        temps_paquet.tv_sec = 2;
                        temps_paquet.tv_usec = 0;
                        resultat_select = select(nou_socket_udp+1,&esperar_nou_paquet,NULL,NULL,(struct timeval *)&temps_paquet);
                        if(resultat_select){
                            recvfrom(nou_socket_udp,&rebre_paquets,84,MSG_WAITALL,(struct sockaddr *)&adreca_client,(socklen_t *)&llargada);
                            rebre_paquets.id_client_paquet[12] = '\0';
                            if(rebre_paquets.tipus == REG_INFO && atoi(rebre_paquets.numero_aleatori_paquet) == array_clients[i].aleatori && strcmp(rebre_paquets.id_client_paquet,array_clients[i].id_client) == 0){
                                char port_tcp_servidor[5];
                                char *dividir_array;
                                dividir_array = strtok(rebre_paquets.dades_paquets,",");
                                dividir_array = strtok(NULL,",");
                                strcpy(array_clients[i].dispositius_clients,dividir_array);
                                array_clients[i].id_client[12] = '\0';
                                sprintf((char *) port_tcp_servidor,"%i",tcp_port);
                                port_tcp_servidor[4] = '\0';
                                array_clients[i].estatus_client = REGISTERED;
                                enviar_paquets.tipus = INFO_ACK;
                                strcpy(enviar_paquets.id_client_paquet,id_servidor);
                                strcpy(enviar_paquets.dades_paquets,port_tcp_servidor);
                                strcpy(enviar_paquets.numero_aleatori_paquet,rebre_paquets.numero_aleatori_paquet);
                                sendto(nou_socket_udp,(struct paquets_clients_udp *) &enviar_paquets,84,MSG_CONFIRM,(struct sockaddr *)&adreca_client,sizeof(adreca_client));
                                sleep(3);
                                if(array_clients[i].estatus_client == REGISTERED){
                                    array_clients[i].estatus_client = DISCONNECTED;
                                }
                            }else{
                                enviar_paquets.tipus = INFO_NACK;
                                strcpy(enviar_paquets.id_client_paquet,id_servidor);
                                strcpy(enviar_paquets.dades_paquets,"paquet incorrecte");
                                strcpy(enviar_paquets.numero_aleatori_paquet,"00000000");
                                sendto(nou_socket_udp,(struct paquets_clients_udp *) &enviar_paquets,84,MSG_CONFIRM,(struct sockaddr *)&adreca_client,sizeof(adreca_client));
                                array_clients[i].estatus_client = DISCONNECTED;
                            }
                        }else{
                            array_clients[i].estatus_client = DISCONNECTED;
                        }

                    }else{
                        enviar_paquets.tipus = REG_NACK;
                        strcpy(enviar_paquets.id_client_paquet,id_servidor);
                        strcpy(enviar_paquets.dades_paquets,"identificaio incorrecta");
                        strcpy(enviar_paquets.numero_aleatori_paquet,"00000000");
                        sendto(socket_udp,(struct paquets_clients_udp *) &enviar_paquets,84,MSG_CONFIRM,(struct sockaddr *)&adreca_client,sizeof(adreca_client));
                        array_clients[i].estatus_client = DISCONNECTED;
                    }

                }
            }
            
        }
    }else if (rebre_paquets.tipus == ALIVE){
        i = 0;
        while (i < 50){
            if(strcmp(array_clients[i].id_client,rebre_paquets.id_client_paquet) == 0){
                if(array_clients[i].estatus_client == REGISTERED || array_clients[i].estatus_client == SEND_ALIVE){
                    if(strcmp(rebre_paquets.dades_paquets,"") == 0 && atoi(rebre_paquets.numero_aleatori_paquet) == array_clients[i].aleatori){
                        enviar_paquets.tipus = ALIVE;
                        strcpy(enviar_paquets.id_client_paquet,id_servidor);
                        strcpy(enviar_paquets.dades_paquets,array_clients[i].id_client);
                        strcpy(enviar_paquets.numero_aleatori_paquet,rebre_paquets.numero_aleatori_paquet);
                        sendto(socket_udp,(struct paquets_clients_udp *) &enviar_paquets,84,MSG_CONFIRM,(struct sockaddr *)&adreca_client,sizeof(adreca_client));
                        array_clients[i].estatus_client = SEND_ALIVE;
                        array_clients[i].alive_rebut = 1;
                        array_clients[i].alive_sense_resposta = 0;
                        return NULL;    
                    }
                }
            }
        }
        enviar_paquets.tipus = ALIVE_REJ;
        strcpy(enviar_paquets.id_client_paquet,id_servidor);
        strcpy(enviar_paquets.dades_paquets,array_clients[i].id_client);
        strcpy(enviar_paquets.numero_aleatori_paquet,rebre_paquets.numero_aleatori_paquet);
        sendto(socket_udp,(struct paquets_clients_udp *) &enviar_paquets,84,MSG_CONFIRM,(struct sockaddr *)&adreca_client,sizeof(adreca_client));
        array_clients[i].estatus_client = DISCONNECTED;
    }
    return NULL;
}
void *funcio_thread_registre(void *args){
    struct sockaddr_in adreca_servidor;
    fd_set escoltar_socket;
    pthread_t thread_registre_rebut;
    int resultat_select;
    socket_udp = socket(AF_INET,SOCK_DGRAM,0);
    adreca_servidor.sin_family = AF_INET;
    adreca_servidor.sin_port = htons(udp_port);
    adreca_servidor.sin_addr.s_addr = INADDR_ANY;
    bind(socket_udp,(const struct sockaddr *)&adreca_servidor,sizeof(adreca_servidor));
    
    while (esperar_thread_registre == 1){
        FD_ZERO(&escoltar_socket);
        FD_SET(socket_udp,&escoltar_socket);
        resultat_select = select(socket_udp+1,&escoltar_socket,NULL,NULL,0);
        if(resultat_select){
            pthread_create(&thread_registre_rebut,NULL,funcio_tractar_paquet,NULL);
            sleep(0.1);
        }
    }
    return NULL;
}

void *funcio_thread_alive(void*args){
    int i;
    int llargada = sizeof(struct sockaddr_in);
    struct paquets_clients_udp paquet_alive;
    while(thread_gestionar_alive == 1){
        i = 0;
        sleep(2);
        while(i<50){
            if(array_clients[i].estatus_client == SEND_ALIVE){
                if(array_clients[i].alive_sense_resposta == 3){
                    array_clients[i].estatus_client = DISCONNECTED;
                }
                if(array_clients[i].alive_rebut == 0){
                    char string_random[9];
                    sprintf(string_random,"%i",array_clients[i].aleatori);
                    string_random[8] = '\0';
                    paquet_alive.tipus = ALIVE;
                    strcpy(paquet_alive.id_client_paquet,id_servidor);
                    strcpy(paquet_alive.numero_aleatori_paquet,string_random);
                    strcpy(paquet_alive.dades_paquets,array_clients[i].id_client);
                    array_clients[i].alive_sense_resposta++;
                    sendto(socket_udp,(struct paquets_clients_udp *)&paquet_alive,84,MSG_CONFIRM,(struct sockaddr *)&array_clients[i].adreca_udp_client,llargada);
                }else{
                    array_clients[i].alive_rebut = 0;
                }
            }
            i++;
        }
    }
    return NULL;
}
void llista_clients(){
    int i = 0;
    printf("DADES DELS DISPOSITIUS\n");
    printf("ID\t\tESTATUS\t\tDISPOSITIUS\t\n");
    while(i < 50){
        if(strcmp(array_clients[i].id_client,"\0") == 0){
            break;
        }
        printf("%s\t",array_clients[i].id_client);
        if(array_clients[i].estatus_client == DISCONNECTED){
            printf("DISCONNECTED\t");
        }else if(array_clients[i].estatus_client == NOT_REGISTERED){
            printf("NOT_REGISTERED\t");
        }else if(array_clients[i].estatus_client == WAIT_ACK_REG){
            printf("WAIT_ACK_REG\t");
        }else if(array_clients[i].estatus_client == WAIT_INFO){
            printf("WAIT_INFO\t");
        }else if(array_clients[i].estatus_client == WAIT_ACK_INFO){
            printf("WAIT_ACK_INFO\t");
        }else if(array_clients[i].estatus_client == REGISTERED){
            printf("REGISTERED\t");
        }else if(array_clients[i].estatus_client == SEND_ALIVE){
            printf("SEND_ALIVE\t");
        }
        printf("%s\n",array_clients[i].dispositius_clients);
        i++;
    }
}

int main(int argc, char *argv[]){
    char udp_string[32] = "";
    char tcp_string[32] = "";
    char numeros_udp[4] = "";
    char numeros_tcp[4] = "";
    FILE *llegir_fitxer,*llegir_llista;
    int j = 0;
    int i =1;
    char nom_fitxer[64] = "", nom_llista[64] = "";
    char rebre_comanda[64];

    for (i = 1 ; i< argc; i++){
        if (strcmp(argv[i],"-c") == 0){
            if((i+1) < argc && strlen(argv[i+1])<=64){
                strcpy(nom_fitxer,argv[i+1]);
                i++;
            }

        }else if (strcmp(argv[i],"-d") == 0 ){
            debug = 1;/*true*/
        }else if (strcmp(argv[i],"-u") == 0){   
            if((i+1) < argc && strlen(argv[i+1])<=64){
                strcpy(nom_llista,argv[i+1]);
                i++;
            } 
        
        }else{
            printf("Parametre no acceptat");
        }
    }
    if(strcmp(nom_fitxer,"") == 0){
        strcpy(nom_fitxer,"server.cfg");
    }
    if(strcmp(nom_llista,"") == 0){
        strcpy(nom_llista,"bbdd_dev.dat");
    }

    
    llegir_fitxer = fopen(nom_fitxer,"r");
    fgets(id_servidor,32,llegir_fitxer);
    id_servidor[strlen(id_servidor)-1] = '\0';
    /*intro*/
    
    for(j = 0; j < 5;j++){
        for(i = 1; i < strlen(id_servidor);i++){
            id_servidor[i-1] = id_servidor[i];
            id_servidor[i] = '&';
        }
    }
    for(i = 0; i < strlen(id_servidor);i++){
        if(id_servidor[i] == '&'){
            id_servidor[i] = '\0';
        }
    }

    

    fgets(udp_string,32,llegir_fitxer);
    j = 0;
    for(i = 0; i < strlen(udp_string);i++){
        if(isdigit(udp_string[i])){
            numeros_udp[j] = udp_string[i];
            j++;
        }
    }
    udp_port = atoi(numeros_udp);

    fgets(tcp_string,32,llegir_fitxer);
    
    j = 0;
    for(i = 0; i < strlen(tcp_string);i++){
        if(isdigit(tcp_string[i])){
            numeros_tcp[j] = tcp_string[i];
            j++;
        }
    }
    tcp_port = atoi(numeros_tcp);

    fclose(llegir_fitxer);

    llegir_llista = fopen(nom_llista,"r");
    i = 0;
    while (i<50){
        fgets(array_clients[i].id_client,32,llegir_llista);
        array_clients[i].id_client[12] = '\0';
        array_clients[i].estatus_client = DISCONNECTED;
        i++;
    }
    fclose(llegir_llista);

    esperar_thread_registre = 1;
    pthread_create(&nom_thread_esperar_registre,NULL,funcio_thread_registre,NULL);
    thread_gestionar_alive = 1;
    pthread_create(&nom_thread_gestionar_alive,NULL,funcio_thread_alive,NULL);
    while (1){
        fgets(rebre_comanda,64,stdin);
        rebre_comanda[strlen(rebre_comanda)-1] = '\0';
        if(strcmp(rebre_comanda,"list") == 0){
            llista_clients();
        }else if(strcmp(rebre_comanda,"quit") == 0){
            esperar_thread_registre = 0;
            thread_gestionar_alive = 0;
            break;
        }else{
            printf("Comanda desconeguda\n");
        }
    }
    return 0;
}
