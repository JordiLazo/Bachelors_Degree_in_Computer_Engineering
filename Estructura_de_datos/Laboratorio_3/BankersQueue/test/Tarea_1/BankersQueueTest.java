package Tarea_1;

import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BankersQueueTest {


    @Test
    void isEmpty(){
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        assertTrue(bankersQueue.isEmpty());
        bankersQueue.add(new Client("Pepe", 1234));
        assertFalse(bankersQueue.isEmpty());

    }

    @Test
    void size(){
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        assertEquals(0, bankersQueue.size());
        bankersQueue.add(new Client("Pepe", 1234));
        assertEquals(1, bankersQueue.size());
    }

    @Test
    void add(){
        Client[] client = new Client[]{new Client("Pepe",1), new Client("Juan", 2)};
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        for(int i = 0; i < client.length; i++) {
            bankersQueue.add(client[i]);
        }

        for(int i = 0; i < client.length; i++){

            assertEquals(client[i], bankersQueue.element());
            bankersQueue.remove();
        }
    }

    @Test
    void elementEmptyQueue(){
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        boolean throwException = false;
        //Comprobamos si lanza un excepción si esta la cola vacia
        try {
            bankersQueue.element();
        }catch (NoSuchElementException e){
            throwException = true;
        }

        assertTrue(throwException);
    }

    @Test
    void element(){
        Client[] Client = new Client[]{new Client("Pepe",1), new Client("Juan", 2), new Client("Carlos", 3)};
        BankersQueue<Client> bankersQueue = new BankersQueue<>();

        for(int i = 0; i < Client.length; i++) {
            bankersQueue.add(Client[i]);
        }
        assertEquals(Client[0], bankersQueue.element());

    }

    @Test
    void removeEmptyQueue(){
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        boolean throwException = false;
        //Comprobamos si lanza un excepción si esta la cola vacia
        try {
            bankersQueue.remove();
        }catch (NoSuchElementException e){
            throwException = true;
        }
        assertTrue(throwException);
    }

    @Test
    void remove(){
        Client[] Client = new Client[]{new Client("Pepe",1), new Client("Juan", 2)};
        BankersQueue<Client> bankersQueue = new BankersQueue<>();
        for(int i = 0; i < Client.length; i++){
            bankersQueue.add(Client[i]);
        }

        bankersQueue.remove();
        assertEquals(Client[1], bankersQueue.element());
    }


}