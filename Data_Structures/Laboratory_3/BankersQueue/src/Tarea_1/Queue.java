package Tarea_1;

public interface Queue<E> {
    void add(E e);
    void remove();
    E element();
    boolean isEmpty();
    int size();
}
