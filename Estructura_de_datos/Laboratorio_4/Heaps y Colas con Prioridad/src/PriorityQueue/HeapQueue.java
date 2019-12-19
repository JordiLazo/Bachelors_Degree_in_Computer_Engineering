package PriorityQueue;
/**
 **Practica: Laboratorio4
 *
 **Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
import java.util.*;

public class HeapQueue<V, P extends Comparable<? super P>> implements PriorityQueue<V, P> {

    private final ArrayList<TSPair<V, P>> pairs = new ArrayList<>();
    private long nextTimeStamp = 0L;

    private static class TSPair<V, P extends Comparable<? super P>> implements Comparable<TSPair<V, P>> {

        private final V value;
        private final P priority;
        private final long timeStamp;

        public TSPair(V value, P priority, long timeStamp) {
            this.value = value;
            this.priority = priority;
            this.timeStamp = timeStamp;
        }

        @Override
        public int compareTo(TSPair<V, P> o){

            if(priority == null && priority == o.priority){
                return (timeStamp < o.timeStamp)?1:-1;
            }
            if(priority == null){
                return - 1;
            }

            if(o.priority == null) {
                return 1;
            }

            if(priority.compareTo(o.priority) == 0){
                return (timeStamp < o.timeStamp)?1:-1;
            }else {
                return priority.compareTo(o.priority);
            }
        }
    }

    static int parent(int index){
        if(index % 2 == 0){
            return (index - 2)/2; // Si es el hijo derecho
        }else {
            return (index - 1)/2; // Si es el hijo izquierdo
        }
    }

    static int left(int index) {
        return index * 2 + 1;
    }
    static int right(int index) {
        return index * 2 + 2;
    }

    boolean isValid(int index) {
        return 0 <= index && index < size();
    }

    boolean hasParent(int index) {
        return index > 0;
    }

    boolean hasLeft(int index) {
        return isValid(left(index));
    }

    boolean hasRight(int index) {
        return isValid(right(index));
    }

    private void upInTree(int index){
        if(isValid(index) && hasParent(index)){
            TSPair<V,P> parent = pairs.get(parent(index));
            TSPair<V,P> child = pairs.get(index);
            if(child.compareTo(parent) > 0){
                pairs.set(parent(index), child);
                pairs.set(index, parent);
                upInTree(parent(index));
            }
        }
    }

    private void downInTree(int index){
        TSPair<V,P> parent = pairs.get(index);
        TSPair<V,P> bestChild = null;
        int nextIndex = 0;
        if(index < size() - 1){
            if(hasLeft(index)) {
                bestChild = pairs.get(left(index));
                nextIndex = left(index);
            }

            if(hasRight(index)){
                TSPair<V,P> rightChild = pairs.get(right(index));
                if(bestChild == null){
                    bestChild = rightChild;
                    nextIndex = right(index);
                }else if(bestChild.compareTo(rightChild) < 0){
                    bestChild = rightChild;
                    nextIndex = right(index);
                }
            }

            if(bestChild != null && parent.compareTo(bestChild) < 0){
                pairs.set(index, bestChild);
                pairs.set(nextIndex, parent);
                downInTree(nextIndex);
            }
        }

    }


    @Override
    public void add(V value, P priority) {
        TSPair<V, P> pair = new TSPair<>(value, priority, nextTimeStamp++);
        pairs.add(pair);
        upInTree(size()-1);
    }

    @Override
    public V remove(){
       V element;
       if(size()==0){
           throw new NoSuchElementException();
       }
       element = element();
       pairs.set(0, pairs.get(size() - 1));
       pairs.remove(size() - 1);
       if(size() > 1)downInTree(0); //Bajamos el elemento por el arbol si este tiene mas de un elemento
       return element;
    }

    @Override
    public V element() {
        if(size() == 0){
            throw new NoSuchElementException();
        }
        return pairs.get(0).value;
    }

    @Override
    public int size() {
        return pairs.size();
    }
}
