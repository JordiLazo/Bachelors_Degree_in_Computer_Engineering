package Tarea2;

import java.util.*;
import Tarea1.*;

public interface Traversals {
    <E> List<E> preOrder(BinaryTree<E> tree);
    <E> List<E> inOrder(BinaryTree<E> tree);
    <E> List<E> postOrder(BinaryTree<E> tree);
}
