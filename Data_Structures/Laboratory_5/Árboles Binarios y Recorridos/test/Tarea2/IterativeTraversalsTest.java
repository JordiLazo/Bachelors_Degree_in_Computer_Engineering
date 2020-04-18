package Tarea2;

import Tarea1.LinkedBinaryTree;
import org.junit.jupiter.api.Test;
import test_class.RecursiveTraversals;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IterativeTraversalsTest {

    @Test
    void preOrder() {
        //construccion del arbol
        LinkedBinaryTree<Integer> left = new LinkedBinaryTree<Integer>(4);
        assertTrue(left.left().isEmpty());
        LinkedBinaryTree<Integer> right = new LinkedBinaryTree<Integer>(5, null, null);
        LinkedBinaryTree<Integer> leftR = new LinkedBinaryTree<Integer>(2, left, right);
        left = new LinkedBinaryTree<>(6);
        right = new LinkedBinaryTree<>(7);
        LinkedBinaryTree<Integer> rightR = new LinkedBinaryTree<>(3, left, right);
        LinkedBinaryTree<Integer> raiz =  new LinkedBinaryTree<>(1, leftR, rightR);
        RecursiveTraversals recursiveTraversals = new RecursiveTraversals();
        IterativeTraversals iterativeTraversals = new IterativeTraversals();
        List<Integer> listA = recursiveTraversals.preOrder(raiz);
        List<Integer> listB = iterativeTraversals.preOrder(raiz);
        assertEquals(listA.size(), listB.size());

        for(int i = 0; i < listA.size(); i++) {
            assertEquals(listA.get(i), listB.get(i));
        }

    }


    @Test
    void inOrder() {
        LinkedBinaryTree<Integer> left = new LinkedBinaryTree<Integer>(4, null, null);
        assertTrue(left.left().isEmpty());
        LinkedBinaryTree<Integer> right = new LinkedBinaryTree<Integer>(5);
        LinkedBinaryTree<Integer> leftR = new LinkedBinaryTree<Integer>(2, left, right);
        left = new LinkedBinaryTree<>(6);
        right = new LinkedBinaryTree<>(7);
        LinkedBinaryTree<Integer> rightR = new LinkedBinaryTree<>(3, left, right);
        LinkedBinaryTree<Integer> raiz =  new LinkedBinaryTree<>(1, leftR, rightR);


        RecursiveTraversals recursiveTraversals = new RecursiveTraversals();
        IterativeTraversals iterativeTraversals = new IterativeTraversals();
        List<Integer> listA = recursiveTraversals.inOrder(raiz);
        List<Integer> listB = iterativeTraversals.inOrder(raiz);
        assertEquals(listA.size(), listB.size());

        for(int i = 0; i < listA.size(); i++) {
            assertEquals(listA.get(i), listB.get(i));
        }

    }


    @Test
    void postOrder() {
        LinkedBinaryTree<Integer> left = new LinkedBinaryTree<>(4, null, null);
        LinkedBinaryTree<Integer> right = new LinkedBinaryTree<>(5, left, left);
        LinkedBinaryTree<Integer> leftR = new LinkedBinaryTree<>(2, left, right);
        left = new LinkedBinaryTree<>(6);
        right = new LinkedBinaryTree<>(7, left, right);
        LinkedBinaryTree<Integer> rightR = new LinkedBinaryTree<>(3, left, right);
        LinkedBinaryTree<Integer> raiz =  new LinkedBinaryTree<>(1, leftR, rightR);


        RecursiveTraversals recursiveTraversals = new RecursiveTraversals();
        IterativeTraversals iterativeTraversals = new IterativeTraversals();
        List<Integer> listA = recursiveTraversals.postOrder(raiz);
        List<Integer> listB = iterativeTraversals.postOrder(raiz);
        assertEquals(listA.size(), listB.size());
        for(int i = 0; i < listA.size(); i++) {
            assertEquals(listA.get(i), listB.get(i));
        }

    }
}