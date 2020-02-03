package Tarea2;
/**
 **Practica: Laboratorio4
 *
 **Autores: Jordi Lazo Florensa, Alejandro Clavera Poza
 */
import Tarea1.BinaryTree;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IterativeTraversals implements Traversals {

    @Override
    public <E> List<E> preOrder(BinaryTree<E> tree) {
        Stack<BinaryTree<E>> stack = new Stack<>();
        ArrayList<E> list = new ArrayList<>();
        BinaryTree<E> nextTree = null;
        if(!tree.isEmpty()) {
            stack.push(tree);
            list.add(tree.elem());
            nextTree = tree.left();
        }
        while(!stack.isEmpty() || nextTree != null) {
            if(!nextTree.isEmpty()){
                list.add(nextTree.elem()); //añadimos el elemento padre del arbol al recorrido
                stack.push(nextTree);
                nextTree = nextTree.left();
            }else if(!stack.isEmpty()){ //Cuando el arbol esta vacio lo eliminamos de la pila y continuamos por su arbol derecho
                nextTree = stack.pop().right();
            }else {
                nextTree = null;
            }
        }
        return list;
    }

    @Override
    public <E> List<E> inOrder(BinaryTree<E> tree) {
        Stack<BinaryTree<E>> stack = new Stack<>();
        ArrayList<E> list = new ArrayList<>();
        BinaryTree<E> nextTree = null;
        if(!tree.isEmpty()) {
            stack.push(tree);
            nextTree = tree.left();
        }
        while(!stack.isEmpty() || nextTree != null) {
            if(!nextTree.isEmpty()){
                stack.push(nextTree);
                nextTree = nextTree.left();
            }else if(!stack.isEmpty()){
                //añadimos el elemento raiz del arbol al recorrido antes de recorrer el arbol hijo derecho
                list.add(stack.peek().elem());
                nextTree = stack.pop().right();
            }else {
                nextTree = null;
            }
        }
        return list;
    }

    @Override
    public <E> List<E> postOrder(BinaryTree<E> tree) {
        Stack<BinaryTree<E>> stack = new Stack<>();
        boolean isRight = false;
        ArrayList<E> list = new ArrayList<>();
        BinaryTree<E> nextTree = tree.left();

        if(!tree.isEmpty()) stack.push(tree);
        while(!stack.isEmpty()){
            if(!nextTree.isEmpty()){
                stack.push(nextTree);
                nextTree = nextTree.left();
                if(!nextTree.isEmpty())isRight = false;
            }else {
                //Pasamos a recorrer el arbol derecho
                BinaryTree<E> aux = stack.peek().right();
                if(aux.isEmpty()){//Si hemos llegado al final del arbol eliminamos de la pila el arbol y lo añadimos al recorrido
                    BinaryTree<E> lastPop = stack.pop();
                    list.add(lastPop.elem());
                    //Mientras sigamos en el arbol derecho vamos eliminando los arboles y añadiendo su elemento al recorrido
                    while(isRight && !stack.isEmpty() && lastPop.equals(stack.peek().right())){
                        lastPop = stack.pop();
                        list.add(lastPop.elem());
                    }
                }else {
                    nextTree = aux;
                    isRight = true; //Marcamos que estamos en el arbol hijo derecho
                }
            }
        }
        return list;
    }
}