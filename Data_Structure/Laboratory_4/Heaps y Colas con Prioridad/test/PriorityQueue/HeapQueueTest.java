package PriorityQueue;

import org.junit.jupiter.api.Test;
import testClass.Employe;
import testClass.Engineer;
import testClass.Secretary;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HeapQueueTest {

    @Test
    void emptyQueueElement(){
        HeapQueue<Integer,Integer> queue = new HeapQueue<Integer, Integer>();
        boolean throwExecption = false;
        try {
            queue.element();

        }catch (NoSuchElementException e) {
            throwExecption = true;
        }
        assertTrue(throwExecption);
    }

    @Test
    void emptyQueueRemove(){
        HeapQueue<Integer,Integer> queue = new HeapQueue<Integer, Integer>();
        boolean throwExecption = false;
        try {
            queue.remove();

        }catch (NoSuchElementException e) {
            throwExecption = true;
        }
        assertTrue(throwExecption);
    }

    @Test
    void addRemove(){
        HeapQueue<Integer,Integer> queue = new HeapQueue<Integer, Integer>();
        int[] numsTest = new int[]{10, 3, 40, 32, 56, 456, 89};
        int[] priorityTest = new int[]{0, 1, 3, 4, 4, 0, 2};
        int[] resultsTest = new int[]{32,56,40,89, 3,10, 456};

        //comprobaciones con prioridades nulas
        queue.add(32, null);
        queue.add(345, null);
        assertEquals(32, queue.remove());
        assertEquals(345, queue.remove());
        queue.add(2, null);
        queue.add(1,10);
        assertEquals(1, queue.remove());
        assertEquals(2, queue.remove());

        for(int i = 0; i < numsTest.length; i++){
            queue.add(numsTest[i], priorityTest[i]);
        }

        for(int i = 0; i < resultsTest.length; i++){
            assertEquals(resultsTest[i], queue.remove());
        }
    }

    @Test
    void addRemoveHierachy(){
        HeapQueue<String, Employe> employeQueue = new HeapQueue<String, Employe>();
        String[] name = new String[]{"Juan", "Maria", "Pepe", "Lucia"};
        int[] ids = new int[]{3,6,8,4};
        String[] testResult = new String[]{"Pepe", "Maria", "Lucia", "Juan", "Fernando", "Ana"};
        employeQueue.add("Ana", null);
        employeQueue.add("Fernando", new Secretary("Fernando", 10, 1));

        for(int i = 0; i < name.length; i++){
            employeQueue.add(name[i], new Engineer(name[i], i, "Informatico", ids[i]));
        }

        for(int i = 0; i < testResult.length; i++){
            assertEquals(testResult[i], employeQueue.remove());
        }
    }

}
