package Tarea3;
/**
 **Practica: Laboratorio4
 *
 **Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
import java.util.List;
import Tarea1.*;

public class Regenerator {
    public static <E> LinkedBinaryTree<E> postAndIn(List<? extends E> postorder, List<? extends E> inorder) {
        if(postorder.size() > 1){
            E element = postorder.get(postorder.size()-1);
            int rootPosition = inorder.indexOf(element);
            int sizeRight = inorder.size() - 1 - rootPosition;
            int initPosRightChild =  postorder.size()- 1 - sizeRight;
            LinkedBinaryTree<E> left = postAndIn(postorder.subList(0, initPosRightChild), inorder.subList(0, rootPosition));
            LinkedBinaryTree<E> right = postAndIn(postorder.subList(initPosRightChild, postorder.size() - 1), inorder.subList(rootPosition + 1 , inorder.size()));
            return new LinkedBinaryTree<>(element, left, right);
        }else if(postorder.size() == 1) {
            E element = postorder.get(postorder.size() - 1);
            return new LinkedBinaryTree<>(element);
        }
        return new LinkedBinaryTree<>();
    }
}
