package Tarea_2;

import java.util.*;

import Tarea_1.Queue;

public class BankersQueue<E> implements Queue<E>, Iterable<E>{
    private ArrayList<E> front, back;
    private int modCount = 0;


    public BankersQueue(){
        front = new ArrayList<>();
        back = new ArrayList<>();
    }

    @Override
    public void add(E e) {
        back.add(e);
        modCount += 1;
    }

    @Override
    public void remove(){
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        if(front.size() == 0){
            moveToFront();
            modCount += 1;
        }

        front.remove(front.size() - 1);
        modCount += 1;
    }

    @Override
    public E element() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        if(front.size() == 0){
            moveToFront();
            modCount += 1;

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

    @Override
    public Iterator<E> iterator() {
        return new IteratorQueue();
    }

    private class IteratorQueue implements Iterator<E> {
        int cursorFront, cursorBack;
        ListIterator<E> iteratorFront;
        ListIterator<E> iteratorBack;
        int expectedModCount;


        public IteratorQueue(){
            cursorFront = front.size();//Posicionamos el cursorFront al final de ArrayList front
            cursorBack = 0; //Posicionamos el cursorFront al principio de ArrayList back
            iteratorFront = front.listIterator(cursorFront);
            iteratorBack = back.listIterator(cursorBack);
            expectedModCount = modCount;
        }


        @Override
        public boolean hasNext() {
            //Si el cursorFront no esta al principio de la lista comprobamos si tiene elemento previo
            if(cursorFront > 0){
                return iteratorFront.hasPrevious();
            }

            //Si el cursorFront esta al principio de la lista comprobamos si el cursorBack tiene elemento siguiente
            return iteratorBack.hasNext();
        }

            @Override
            public E next(){
            if(expectedModCount != modCount){
                throw new ConcurrentModificationException();
            }else if(isEmpty()){
                throw new NoSuchElementException();
            }

            /*
                Si el cursorFront no esta en el principio del Front
                retornamos el elemento previo del iterador de la primera lista
             */
            if(cursorFront > 0){
                cursorFront -= 1;
                return iteratorFront.previous();
            }
           /*
                Si el cursorFront esta en el principio del Front
                retornamos el elemento siguiente del iterador de la segunda lista
             */
            cursorBack += 1;
            return iteratorBack.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
