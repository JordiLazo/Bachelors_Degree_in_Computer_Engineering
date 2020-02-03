package test_class;
import Tarea1.BinaryTree;
import Tarea2.Traversals;
import java.util.*;

public class RecursiveTraversals implements Traversals {

    @Override
    public <E> List<E> preOrder(BinaryTree<E> tree) {
        List<E> result = new ArrayList<>();
        preOrder(tree, result);
        return result;
    }

    private <E> void preOrder(BinaryTree<E> tree, List<E> result) {
        if (!tree.isEmpty()) {
            result.add(tree.elem());
            preOrder(tree.left(), result);
            preOrder(tree.right(), result);
        }
    }

    @Override
    public <E> List<E> inOrder(BinaryTree<E> tree) {
        List<E> result = new ArrayList<>();
        inOrder(tree, result);
        return result;
    }
    private <E> void inOrder(BinaryTree<E> tree, List<E> result) {
        if (!tree.isEmpty()) {
            inOrder(tree.left(), result);
            result.add(tree.elem());
            inOrder(tree.right(), result);
        }
    }

    @Override
    public <E> List<E> postOrder(BinaryTree<E> tree) {
        List<E> result = new ArrayList<>();
        postOrder(tree, result);
        return result;
    }
    private <E> void postOrder(BinaryTree<E> tree, List<E> result) {
        if (!tree.isEmpty()) {
            postOrder(tree.left(), result);
            postOrder(tree.right(), result);
            result.add(tree.elem());
        }
    }
}
