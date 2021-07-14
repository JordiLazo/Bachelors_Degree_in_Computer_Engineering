/* ---------------------------------------------------------------
Práctica 2.
Código fuente: threadMessenger.java
Grau Informàtica
49259953W i Sergi Puigpinós Palau.
47694432E i Jordi Lazo Florensa.
--------------------------------------------------------------- */
package eps.udl.cat;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class threadMessenger extends Thread{
    private static final int LIST_SIZE = 100;
    private static final LinkedList<String> messageList = new LinkedList<>();
    public static final ReentrantLock messengerLock = new ReentrantLock();
    private static final Condition itemAdded = messengerLock.newCondition();
    public static final Condition messengerEnded = messengerLock.newCondition();
    private static final Semaphore semaphore = new Semaphore(LIST_SIZE);
    private static boolean exitMess = false;


    private void printMessages(){
        for (String s : messageList) {
            System.out.print(s);
        }
        messageList.clear();
    }

    public static void addMessageToQueue(String message) {
        try {
            semaphore.acquire();
            messengerLock.lock();
            messageList.add(message);
            itemAdded.signal();
        } catch (java.lang.InterruptedException exception) {
            System.out.println("Program Interrupted");
        } finally {
            messengerLock.unlock();
        }
    }

    public static void killMessenger(){
        exitMess=true;
        itemAdded.signal();
    }

    @Override
    public void run(){
        while (!exitMess){
            try{
                messengerLock.lock();
                //Wait until list is full
                while (messageList.size() != LIST_SIZE && !exitMess)
                    itemAdded.await();
                if (messageList.size() > 0) {
                    printMessages();
                    semaphore.release(LIST_SIZE);
                }
            } catch (java.lang.InterruptedException exception) {
                System.out.println("Program Interrupted");
            } finally {
                messengerLock.unlock();
            }
        }

        //Send signal to parent
        try{
            messengerLock.lock();
            messengerEnded.signalAll();
        } finally {
            messengerLock.unlock();
        }
    }
}
