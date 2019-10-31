package Tarea_1;
/**
 ** Practica: Laboratorio1
 *
 ** Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
import java.util.*;

public class Partitioner {

    public static PairOfLists partition(List<Integer> src, int pivot){
        PairOfLists pairOfLists = new PairOfLists();
        Iterator<Integer> iterator = src.iterator();
        while(iterator.hasNext()){
            int nextElement = iterator.next();
            if(nextElement <= pivot){
                pairOfLists.list1.add(nextElement);
            }else {
                pairOfLists.list2.add(nextElement);
            }
        }
        return pairOfLists;
    }
}
