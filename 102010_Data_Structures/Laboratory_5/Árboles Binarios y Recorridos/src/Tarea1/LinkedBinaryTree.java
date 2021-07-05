package Tarea1;
/**
 **Practica: Laboratorio4
 *
 **Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
import java.util.NoSuchElementException;

public class LinkedBinaryTree<E> implements BinaryTree<E> {

    private Node<E> root;

    private static class Node<E> {
        private final E elem;
        private final Node<E> left;
        private final Node<E> right;

        private Node(E elem) {
            this.elem = elem;
            left = null;
            right = null;
        }

        private Node(E elem, Node<E> left, Node<E> right) {
            this.elem = elem;
            this.left = left;
            this.right = right;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if(obj == null || !(obj instanceof Node ))return false;
            Node<E> node = (Node<E>)obj;
            if(left == null || right == null){
                boolean isEquals = true;
                if(left == null)isEquals = left == node.left;
                if(right == null) isEquals &= right == node.right;
                return isEquals && elem.equals(node.elem);
            }
            return elem.equals(node.elem) && left.equals(node.left) && right.equals(node.right);
        }
    }

    public LinkedBinaryTree() {
        root = null;
    }

    public LinkedBinaryTree(E elem) {
        root = new Node<>(elem);
    }

    public LinkedBinaryTree(E e, LinkedBinaryTree<E> left, LinkedBinaryTree<E> right) {
        //En caso que nos pasen null para evitar excepciones generamos arboles vacios
        if(left==null)left = new LinkedBinaryTree<>();

        if(right==null)right = new LinkedBinaryTree<>();

        root = new Node<>(e, left.root, right.root);
    }

    private LinkedBinaryTree(Node<E> root) {
        this.root = root;
    }

    @Override
    public E elem() {
        if(isEmpty()) throw new NoSuchElementException();
        return root.elem;
    }

    @Override
    public BinaryTree<E> left() {
        if(isEmpty()) throw new NoSuchElementException();
        return new LinkedBinaryTree<E>(root.left);
    }

    @Override
    public BinaryTree<E> right() {
        if(isEmpty()) throw new NoSuchElementException();
        return new LinkedBinaryTree<E>(root.right);
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof LinkedBinaryTree)) {
            return false;
        }

        LinkedBinaryTree<E> tree = (LinkedBinaryTree<E>)obj;
        if(root == null || tree.root == null ) {
            return root == tree.root;
        }

        return root.equals(tree.root);
    }

}