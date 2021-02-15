#!/usr/bin/env python3.6

import socket
import sys
from datetime import datetime
import threading
from time import sleep
import select
import struct
import signal
import os


def t():
    return datetime.now().strftime("%d/%m/%Y  %H:%M:%S")

def contar_temps():
    global thread_contar_REG_REQ, enviar_REG_REQ
    time = 1
    sleep(time)
    for i in range(3):
        if thread_contar_REG_REQ:
            enviar_REG_REQ = True
            sleep(time)
        else:
            break
    while time < 3:
        if thread_contar_REG_REQ:
            enviar_REG_REQ = True
            time += 1
            sleep(time)
        else:
            break
    while True:
        if thread_contar_REG_REQ:
            enviar_REG_REQ = True
            sleep(time)
        else:
            break


def registrarse():
    global thread_contar_REG_REQ
    if debug:
        print(t() + "s'ha creat un thread")
    thread_contar_REG_REQ = True
    thread_REG_REQ = threading.Thread(target=contar_temps, args=[], daemon=True)
    thread_REG_REQ.start()


def funcio_principal_registre():
    global numero_paquets, intents_de_registre, estat_client, client_socket, thread_contar_REG_REQ, enviar_REG_REQ

    while True:
        if enviar_REG_REQ:
            enviar_REG_REQ = False
            adreca = (socket.gethostbyname(diccionary["Server"]), int(diccionary["Server-UDP"]))
            empaquetar_REG_REQ = struct.pack("B13s9s61s", 0, diccionary["Id"].encode(), "00000000".encode(),
                                             "".encode())
            client_socket.sendto(empaquetar_REG_REQ, adreca)
            estat_client = "WAIT_ACK_REG"
            print(t() + "NOU ESTAT = " + str(estat_client))
            numero_paquets += 1
            if debug:
                print(t() + "Enviant un paquet REG_REQ")
        if numero_paquets == 7:
            intents_de_registre += 1
            numero_paquets = 0
            thread_contar_REG_REQ = False
            estat_client = "NOT_REGISTERED"
            print(t() + "NOU ESTAT = " + str(estat_client))
            if intents_de_registre < 3:
                sleep(2)
                registrarse()
            else:
                print(t() + "No se ha pogut realitzat el registre")
                exit(-1)

        llegir, escriure, excepcional = select.select([client_socket], [], [], 0)
        if client_socket in llegir:
            thread_contar_REG_REQ = False
            paquet = client_socket.recv(84)
            dades_paquet = struct.unpack("B13s9s61s", paquet)
            # hem rebut un paquet via UDP
            if dades_paquet[0] != 2:
                estat_client = "NOT REGISTERED"
                print(t() + "NOU ESTAT = " + str(estat_client))
                if dades_paquet[0] == 4:
                    registrarse()
                else:
                    intents_de_registre += 1
                    numero_paquets = 0
                    registrarse()
            else:
                aleatori = int(dades_paquet[2].decode()[:-1])
                port_udp_string = ""
                for c in dades_paquet[3].decode(errors="ignore"):
                    if c.isdigit() and c != '\0':
                        port_udp_string = port_udp_string.__add__(c)
                    if c == '\0':
                        break
                nou_port_udp = int(port_udp_string)
                adreca = (socket.gethostbyname(diccionary["Server"]), nou_port_udp)
                dades_REG_INFO = str(diccionary["Local-TCP"]) + "," + str(diccionary["Params"])
                empaquetar_REG_INFO = struct.pack("B13s9s61s", 1, diccionary["Id"].encode(), dades_paquet[2],
                                                  dades_REG_INFO.encode())
                client_socket.sendto(empaquetar_REG_INFO, adreca)
                estat_client = "WAIT_ACK_INFO"
                print(t() + "NOU ESTAT = " + str(estat_client))
                llegir, escriure, excepcional = select.select([client_socket], [], [], 2)
                if client_socket not in llegir:
                    estat_client = "NOT REGISTERED"
                    print(t() + "NOU ESTAT = " + str(estat_client))
                    print("No s'ha rebut respota al REG INFO")
                    intents_de_registre += 1
                    numero_paquets = 0
                    registrarse()
                else:
                    paquet = client_socket.recv(84)
                    dades_paquet = struct.unpack("B13s9s61s", paquet)
                    if dades_paquet[0] == 3:
                        estat_client = "REGISTERED"
                        print(t() + "NOU ESTAT = " + str(estat_client))
                        port_tcp_string = ""
                        for c in dades_paquet[3].decode(errors="ignore"):
                            if c.isdigit() and c != '\0':
                                port_tcp_string = port_tcp_string.__add__(c)
                            if c == '\0':
                                break
                        return (int(port_tcp_string), aleatori)
                    else:
                        estat_client = "NOT_REGISTERED"
                        print(t() + "NOU ESTAT = " + str(estat_client))
                        print("No s'ha rebut INFO ACK")
                        intents_de_registre += 1
                        numero_paquets = 0
                        registrarse()



def contar_variable():
    global enviar_alive
    while thread_alive_contar:
        enviar_alive = True
        sleep(2)

def enviar_variable():
    global enviar_alive,numeros_alive,estat_client
    alive = struct.pack("B13s9s61s", int("0x10", base=16), diccionary["Id"].encode(), str(aleatori).encode(),
                "".encode())
    while thread_alive_enviar:
        if enviar_alive:
            numeros_alive+=1
            if numeros_alive == 3:
                estat_client = "NOT_REGISTERED"
                print(t() + "NOU ESTAT = " + str(estat_client))
                thread_alive_contar = False
                enviar_alive = False
                os.kill(os.getpid(), signal.SIGUSR1)
            client_socket.sendto(alive,(socket.gethostbyname(diccionary["Server"]), int(diccionary["Server-UDP"])))
            enviar_alive = False
            estat_client = "SEND_ALIVE"
        llegir, escriure, excepcional = select.select([client_socket], [], [], 0)
        if llegir != []:
            paquet = client_socket.recv(84)
            numeros_alive = 0
            dades_paquet = struct.unpack("B13s9s61s",paquet)
            alive_string = ""
            for c in dades_paquet[3].decode(errors="ignore"):
                if c != '\0':
                    alive_string = alive_string.__add__(c)
                else:
                    break
            if dades_paquet[0] != 16 or int(dades_paquet[2].decode()[:-1]) != aleatori or alive_string != diccionary['Id']:
                estat_client = "NOT_REGISTERED"
                print(t() + "NOU ESTAT = " + str(estat_client))
                thread_alive_contar = False
                enviar_alive = False
                os.kill(os.getpid(), signal.SIGUSR1)

def engegar_alive():
    global thread_alive_enviar, thread_alive_contar
    thread_alive_enviar = True
    thread_alive_contar = True
    alive_thread = threading.Thread(target=enviar_variable,args=[],daemon=True)
    alive_thread.start()
    alive_thread = threading.Thread(target=contar_variable, args=[], daemon=True)
    alive_thread.start()

def reiniciar_registre(signum,handler):
    global TCP_servidor,aleatori
    registrarse()
    TCP_servidor, aleatori = funcio_principal_registre()
    engegar_alive()



def estat():
    print("DADES DELS CLIENTS")
    print("ID: " + diccionary['Id'])
    print("ESTATUS: "+ estat_client)
    print("PARMETRE\tVALOR")
    for i in diccionary['Params'].split(';'):
        print(i +  '\t\tNONE')


if __name__ == '__main__':
    tipo_paquet = ["REG_REQ", "REG_INFO", "REG_ACK", "INFO_ACK", "REG_NACK", "INFO_NACK", "REG_REJ"]
    estat_client = ["DISCONNECTED", "NOT_REGISTERED", "WAIT_ACK_REG", "WAIT_INFO", "WAIT_ACK_INFO", "REGISTERED",
                    "SEND_ALIVE"]
    debug = False
    cfg = "client.cfg"

    if len(sys.argv) > 1:
        for counter, argument in enumerate(sys.argv):
            if argument == '-d':
                debug = True
                print(t() + " Debug activat")
            if argument == '-c':
                cfg = sys.argv[counter + 1]

    diccionary = {}
    with open(cfg, "r") as f:
        for line in f:
            (key, value) = line.split("=")
            key = key.replace(" ", "")
            value = value.replace("\n", "").replace(" ", "")
            diccionary[key] = value

    thread_contar_REG_REQ = False
    enviar_REG_REQ = False

    numero_paquets = 0
    intents_de_registre = 0
    estat_client = "DISCONNECTED"
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    registrarse()
    TCP_servidor, aleatori = funcio_principal_registre()


    thread_alive_enviar = False
    thread_alive_contar = False
    enviar_alive = False
    numeros_alive = 0
    signal.signal(signal.SIGUSR1, reiniciar_registre)
    engegar_alive()
    while True:
        rebre_comanda = input("Introdueix una comanda ")
        rebre_comanda = rebre_comanda.split(' ')
        if rebre_comanda[0] == 'stat':
            estat()
        elif rebre_comanda[0] == 'quit':
            break
        elif rebre_comanda[0] == 'set':
            diccionary[rebre_comanda[1]] = rebre_comanda[2]
            pass
        else:
            print("Comanda desconeguda")
