package Tarea1;
/**
 **Practica: Laboratorio4
 *
 **Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
public interface BinaryTree<E>{
    E elem();
    BinaryTree<E> left();
    BinaryTree<E> right();
    boolean isEmpty();
}
