package Tarea1;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LinkedBinaryTreeTest {

    @Test
    void elem() {
        LinkedBinaryTree<Integer> tree = new LinkedBinaryTree<>(32, null, null);
        boolean throwException = false;
        try{
            tree.left().elem();

        }catch (NoSuchElementException e){
            throwException = true;
        }
        assertTrue(throwException);

        assertEquals(32, tree.elem());
    }

    @Test
    void left() {
        LinkedBinaryTree<Integer> emptyTree = new LinkedBinaryTree<>();
        LinkedBinaryTree<Integer> tree = new LinkedBinaryTree<>(32,new LinkedBinaryTree<>(2), null);
        boolean throwException = false;
        try{
            emptyTree.left();

        }catch (NoSuchElementException e){
            throwException = true;
        }
        assertTrue(throwException);

        assertEquals(2, tree.left().elem());
    }

    @Test
    void right() {
        LinkedBinaryTree<Integer> emptyTree = new LinkedBinaryTree<>();
        LinkedBinaryTree<Integer> tree = new LinkedBinaryTree<>(32,null, new LinkedBinaryTree<>(2));
        boolean throwException = false;
        try{
            emptyTree.right();

        }catch (NoSuchElementException e){
            throwException = true;
        }
        assertTrue(throwException);

        assertEquals(2, tree.right().elem());
    }

    @Test
    void isEmpty() {
        assertTrue(new LinkedBinaryTree<>().isEmpty());
        assertFalse(new LinkedBinaryTree<>(45).isEmpty());
    }

    @Test
    void equals() {
        LinkedBinaryTree<Integer> emptyTree = new LinkedBinaryTree<>();
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Hola", new LinkedBinaryTree<>("adios"), new LinkedBinaryTree<>("saludos"));
        LinkedBinaryTree<String> tree2 = new LinkedBinaryTree<>("Ho", new LinkedBinaryTree<>("adios"), new LinkedBinaryTree<>("saludos", new LinkedBinaryTree<>("hola"), null));
        assertTrue(tree.equals(tree));
        assertFalse(emptyTree.equals(tree));
        assertFalse(tree.equals(tree2));
        assertFalse(tree.equals(2));
    }
}