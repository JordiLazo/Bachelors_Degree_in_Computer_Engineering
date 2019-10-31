package Tarea_2;
/**
 ** Practica: Laboratorio1
 *
 ** Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
import java.util.*;
//Clase encargada de comparar dos elementos
public class Comparate <E extends Comparable<E>> implements Comparator<E> {

    @Override
    public int compare(E o1, E o2) {
        return o1.compareTo(o2);
    }
}
