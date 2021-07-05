package Tarea_2;
/**
 ** Practica: Laboratorio1
 *
 ** Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
import java.util.*;

public class Partitioner {

    //Obligamos a que los elementos E implementen la interfaz Comparable para poderlos comparar
    public static <E extends Comparable<E>> PairOfLists<E> partition(List<E> scr, E pivot){
        PairOfLists<E> pairOfLists = new PairOfLists<>();
        Iterator<E> iterator = scr.iterator();
        while(iterator.hasNext()){
            E nextElement = iterator.next();
            if(nextElement.compareTo(pivot)<=0){
                pairOfLists.list1.add(nextElement);
            }else {
                pairOfLists.list2.add(nextElement);
            }
        }
        return pairOfLists;
    }

    public static <E extends Comparable> PairOfLists<E> partition(List<E> src, E pivot, Comparator<E> comp) {
        PairOfLists<E> pairOfLists = new PairOfLists<>();
        Iterator<E> iterator = src.iterator();
        while(iterator.hasNext()){
            E nextELement = iterator.next();
            if(comp.compare(nextELement, pivot)<=0){
                pairOfLists.list1.add(nextELement);
            }else {
                pairOfLists.list2.add(nextELement);
            }
        }
        return pairOfLists;
    }
}
