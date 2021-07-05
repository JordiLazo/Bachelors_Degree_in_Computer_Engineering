package Tarea_2;

import Tarea_1.Client;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BankersQueueTest {

    @Test
    void hasNextEmptyQueue(){
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        Iterator<Client> iterator = bankersQueue.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void hasNext(){
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        Iterator<Client> iterator;
        bankersQueue.add(new Client("Pepe", 12));
        bankersQueue.add(new Client("Juan", 14));
        iterator = bankersQueue.iterator();
        assertTrue(iterator.hasNext());
    }
    @Test
    void nextEmptyQueue(){
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        Iterator<Client> iterator = bankersQueue.iterator();
        //Comprobamos si lanza un excepción si esta la cola vacia
        boolean throwsException = false;
        try {
            iterator.next();
        }catch (NoSuchElementException e){
            throwsException = true;
        }
        assertTrue(throwsException);
    }

    @Test
    void next(){
        Client[] clients = new Client[]{new Client("pepe", 123), new Client("pee", 123)};
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        Iterator<Client> iterator;
        bankersQueue.add(clients[0]);
        bankersQueue.add(clients[1]);
        iterator = bankersQueue.iterator();
        assertEquals(clients[0], iterator.next());
        bankersQueue.remove();
        boolean throwsException = false;
        /*
            Comprobamos si lanza un excepción si se ha realizado una modificación
            a la cola  mientras se estaba reccorriendo con el iterador
         */
        try {
            iterator.next();
        }catch (ConcurrentModificationException e){
            throwsException = true;
        }
        assertTrue(throwsException);
    }


}