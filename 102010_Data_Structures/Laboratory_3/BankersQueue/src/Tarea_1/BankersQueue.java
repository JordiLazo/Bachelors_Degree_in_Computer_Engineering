package Tarea_1;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class BankersQueue<E> implements Queue<E>{
    private ArrayList<E> front, back;


    public  BankersQueue(){
        front = new ArrayList<>();
        back = new ArrayList<>();
    }

    @Override
    public void add(E e) {
        back.add(e);
    }

    @Override
    public void remove(){
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        if(front.size() == 0){
            moveToFront();
        }

        front.remove(front.size() - 1);
    }

    @Override
    public E element() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        if(front.size() == 0){
            moveToFront();
        }

        return front.get(front.size() - 1);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return front.size() + back.size();
    }

    private void moveToFront(){
        for(int i = back.size() - 1; i >= 0; i--){
            front.add(back.get(i));
            back.remove(i);
        }

    }
}
