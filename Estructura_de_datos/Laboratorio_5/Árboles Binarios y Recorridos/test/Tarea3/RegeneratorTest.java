package Tarea3;

import Tarea1.LinkedBinaryTree;
import Tarea2.IterativeTraversals;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegeneratorTest {

    @Test
    void postAndIn() {
        //construccion del arbol
        LinkedBinaryTree<Integer> left = new LinkedBinaryTree<Integer>(4,new LinkedBinaryTree<>(45), null);
        LinkedBinaryTree<Integer> right = new LinkedBinaryTree<Integer>(5);
        LinkedBinaryTree<Integer> leftR = new LinkedBinaryTree<Integer>(2, left, right);
        left = new LinkedBinaryTree<>(6);
        right = new LinkedBinaryTree<>(7);
        LinkedBinaryTree<Integer> rightR = new LinkedBinaryTree<>(3, left, right);
        LinkedBinaryTree<Integer> regenerate;
        LinkedBinaryTree<Integer> raiz =  new LinkedBinaryTree<>(1, leftR, rightR);

        IterativeTraversals iterativeTraversals = new IterativeTraversals();
        List<Integer> posOrder = iterativeTraversals.postOrder(raiz);
        List<Integer> inOrder = iterativeTraversals.inOrder(raiz);

        regenerate = Regenerator.postAndIn(posOrder, inOrder);
        assertTrue(raiz.equals(regenerate));
    }
}