package org.unl.pacas.base.controller.data_struct.list;

/**
 * Clase genérica Node para la estructura de datos enlazada del sistema de pacas.
 * 
 * @param <E> Tipo de dato (Paca, Usuario, Prenda, etc.)
 */
public class Node<E> {
    private E data;           
    private Node<E> next;     

    public E getData() {
        return this.data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Node<E> getNext() {
        return this.next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public Node(E data, Node<E> next) {
        this.data = data;
        this.next = next;
    }

    public Node(E data) {
        this.data = data;
        this.next = null;
    }

    public Node() {
        this.data = null;
        this.next = null;
    }

    public boolean hasNext() {
        return this.next != null;
    }

    public boolean hasData() {
        return this.data != null;
    }

    public void disconnect() {
        this.next = null;
    }

    public Node<E> copy() {
        return new Node<>(this.data);
    }

    @Override
    public String toString() {
        if (data instanceof org.unl.pacas.base.models.Persona) {
            org.unl.pacas.base.models.Persona paca = (org.unl.pacas.base.models.Persona) data;
            return "Node{data=Paca[id=" + paca.getId() + ", nombre=" + paca.getNombre() + "], hasNext=" + hasNext() + "}";
        }
        return "Node{data=" + (data != null ? data.toString() : "null") + ", hasNext=" + hasNext() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node<?> node = (Node<?>) obj;
        return data != null ? data.equals(node.data) : node.data == null;
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }
}