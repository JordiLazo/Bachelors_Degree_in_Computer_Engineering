package PriorityQueue;

public interface PriorityQueue<V, P extends Comparable<? super P>> {
    void add(V value, P priority);
    V remove();
    V element();
    int size();
}