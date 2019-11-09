package Tarea_3;

import Tarea_2.BankersQueue;
import java.util.Iterator;

public class BankSimulator {
    private static final int TIMEA  = 120;
    private static int timeMidle = 0;

    private static BankersQueue<BankClient>[] queues;
    private static Iterator<BankClient>[] iterators;

    public static void main(String[] args){
        for(int i = 1; i <= 10; i++){
            simulation(i, 100);
        }

    }

    private static void simulation(int numOfQueues, int numOfClients){
        boolean isEndOfSimulation = false;
        int time = 0;
        int nextQueue = 0;
        int numClientsArrive = 0;
        int numClientsExit = 0;
        BankClient[] clientsAttend = new BankClient[numOfQueues];
        int[] timeOfClient = new int[numOfQueues]; //Tiempo que lleva atendidos los clientes

        initSimulation(numOfQueues);
        timeMidle = 0;
        while(!isEndOfSimulation){
            //Llegada de un cliente cada 15 segundos hasta que se llege al maximo de la simulación
            if(time % 15 == 0 && numClientsArrive < numOfClients){
                BankClient client = new BankClient();
                client.setArrivalTime(time);
                queues[nextQueue].add(client);
                iterators[nextQueue] = queues[nextQueue].iterator();
                nextQueue = (nextQueue + 1) % numOfQueues;
                numClientsArrive += 1;
            }

            numClientsExit += attendClient(clientsAttend,timeOfClient, time);
            isEndOfSimulation = numClientsExit >= numOfClients;

            time += 1;
        }

        timeMidle /= numOfClients;
        System.out.println("Simulación con: " + numOfQueues + " "+ timeMidle);
    }

    /*
        Metodo encargado de atender a los clientes, retorna el nuemero de clientes que han salido en ese
        instante de tiempo
     */
    private static int attendClient(BankClient[] clientsAttend, int[] timeOfClient, int time){
        int numClientsExit = 0;
        for(int i = 0; i < queues.length; i++){
            if(clientsAttend[i] != null){
                //Si el cajero esta atendiendo a un cliente comprobamos si ya ha acabado de atenderlo
                if(timeOfClient[i] == TIMEA){
                    clientsAttend[i].setExitTime(time);
                    timeMidle += (clientsAttend[i].getExitTime() - clientsAttend[i].getArrivalTime());
                    numClientsExit += 1;
                    //Atiende al siguiente de la cola si lo hay
                    if(iterators[i].hasNext()){
                        clientsAttend[i] = iterators[i].next();
                        queues[i].remove();
                        iterators[i] = queues[i].iterator();
                        timeOfClient[i] = 1;
                    }else {
                        clientsAttend[i] = null;
                    }

                }else {
                    timeOfClient[i] += 1;
                }

            }else {
                //Atienede a un cliente si no estaba atendiendo a nadie, siempre que la cola no este vacia
                if(iterators[i].hasNext()){
                    clientsAttend[i] = iterators[i].next();
                    queues[i].remove();
                    iterators[i] = queues[i].iterator();
                    timeOfClient[i] = 1;
                }

            }

        }
        return numClientsExit;
    }

    @SuppressWarnings("unchecked")
    private static void initSimulation(int numQueues){
        queues = new BankersQueue[numQueues];
        iterators = new Iterator[numQueues];
        for(int i = 0; i < numQueues; i++){
            queues[i] = new BankersQueue();
            iterators[i] = queues[i].iterator();
        }
    }

}
