package org.unl.pacas.base.controller.data_struct.list;

import java.util.Objects;

/**
 * Nodo genérico para la lista enlazada.
 * @param <E> Tipo de dato almacenado.
 */
public class Node<E> {
    private E data;
    private Node<E> next;

    /**
     * Constructor con datos y siguiente nodo.
     * @param data Dato a almacenar.
     * @param next Siguiente nodo.
     */
    public Node(E data, Node<E> next) {
        this.data = data;
        this.next = next;
    }

    /**
     * Constructor solo con dato.
     * @param data Dato a almacenar.
     */
    public Node(E data) {
        this(data, null);
    }

    /**
     * Constructor vacío.
     */
    public Node() {
        this(null, null);
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    /**
     * Representación en texto del nodo (útil para debug).
     */
    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }

    /**
     * Compara dos nodos por su dato y referencia al siguiente.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(data, node.data) &&
               Objects.equals(next, node.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, next);
    }
}