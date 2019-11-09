package Tarea_3;
/**
 ** Practica: Laboratorio1
 *
 ** Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Partitioner {
    public static <E extends Comparable<E>> void copyPartition(List<? extends E> src, E pivot, List<? super E> trg1, List<? super E>trg2){
        Iterator<? extends E> iterator = src.iterator();
        while(iterator.hasNext()){
            E nextElement = iterator.next();
            if(nextElement.compareTo(pivot)<=0){
                trg1.add(nextElement);
            }else {
                trg2.add(nextElement);
            }
        }
    }

    public static <E extends Comparable<E>> void copyPartition(List<? extends E> src, E pivot, List<? super E> trg1, List<? super E>trg2, Comparator<E> comparator){
        Iterator<? extends E> iterator = src.iterator();
        while(iterator.hasNext()){
            E nextElement = iterator.next();
            if(comparator.compare(nextElement, pivot)<=0){
                trg1.add(nextElement);
            }else {
                trg2.add(nextElement);
            }
        }
    }
}
