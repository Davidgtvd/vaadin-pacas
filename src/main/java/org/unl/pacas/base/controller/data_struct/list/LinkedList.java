package org.unl.pacas.base.controller.data_struct.list;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.Comparator;

/**
 * Implementación personalizada de lista enlazada simple con utilidades avanzadas.
 * @param <E> Tipo de elementos almacenados.
 */
public class LinkedList<E> implements Iterable<E> {
    private Node<E> head;
    private Node<E> last;
    private int length;

    public LinkedList() {
        head = null;
        last = null;
        length = 0;
    }

    /** Retorna el tamaño de la lista */
    public int size() {
        return length;
    }

    /** Alias para size() */
    public Integer getLength() {
        return length;
    }

    /** Indica si la lista está vacía */
    public boolean isEmpty() {
        return length == 0;
    }

    private Node<E> getNode(int pos) {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        }
        if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out of range: " + pos);
        }
        if (pos == 0) return head;
        if (pos == length - 1) return last;

        Node<E> current = head;
        for (int i = 0; i < pos; i++) {
            current = current.getNext();
        }
        return current;
    }

    /** Obtiene el elemento en la posición dada */
    public E get(int pos) {
        return getNode(pos).getData();
    }

    private void addFirst(E data) {
        Node<E> newNode = new Node<>(data, head);
        head = newNode;
        if (length == 0) {
            last = newNode;
        }
        length++;
    }

    private void addLast(E data) {
        Node<E> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            last = newNode;
        } else {
            last.setNext(newNode);
            last = newNode;
        }
        length++;
    }

    /**
     * Inserta un elemento en la posición indicada.
     * @param data Elemento a insertar.
     * @param pos Posición donde insertar.
     * @throws IndexOutOfBoundsException si pos inválido.
     */
    public void add(E data, int pos) {
        if (pos < 0 || pos > length) {
            throw new IndexOutOfBoundsException("Index out of range: " + pos);
        }
        if (pos == 0) {
            addFirst(data);
        } else if (pos == length) {
            addLast(data);
        } else {
            Node<E> prev = getNode(pos - 1);
            Node<E> newNode = new Node<>(data, prev.getNext());
            prev.setNext(newNode);
            length++;
        }
    }

    /** Añade al final */
    public void add(E data) {
        addLast(data);
    }

    /** Añade todos los elementos de otra LinkedList */
    public void addAll(LinkedList<E> other) {
        if (other == null || other.isEmpty()) return;
        for (E item : other) {
            this.add(item);
        }
    }

    /** Retorna representación en String */
    public String print() {
        if (isEmpty()) return "Está vacía";
        StringBuilder sb = new StringBuilder();
        Node<E> current = head;
        while (current != null) {
            sb.append(current.getData()).append(" - ");
            current = current.getNext();
        }
        sb.append("\n");
        return sb.toString();
    }

    /** Actualiza el elemento en la posición dada */
    public void update(E data, int pos) {
        getNode(pos).setData(data);
    }

    /** Limpia la lista */
    public void clear() {
        head = null;
        last = null;
        length = 0;
    }

    /** Convierte la lista a arreglo */
    @SuppressWarnings("unchecked")
    public E[] toArray() {
        if (length == 0) {
            return (E[]) new Object[0];
        }
        Class<?> clazz = head.getData().getClass();
        E[] array = (E[]) Array.newInstance(clazz, length);
        Node<E> current = head;
        for (int i = 0; i < length; i++) {
            array[i] = current.getData();
            current = current.getNext();
        }
        return array;
    }

    /** Llena la lista desde un arreglo */
    public LinkedList<E> toList(E[] array) {
        clear();
        for (E e : array) {
            add(e);
        }
        return this;
    }

    /** Elimina y retorna el primer elemento */
    protected E deleteFirst() throws Exception {
        if (isEmpty()) {
            throw new Exception("List empty");
        }
        E data = head.getData();
        head = head.getNext();
        length--;
        if (length == 0) {
            last = null;
        }
        return data;
    }

    /** Elimina y retorna el último elemento */
    protected E deleteLast() throws Exception {
        if (isEmpty()) {
            throw new Exception("List empty");
        }
        if (length == 1) {
            return deleteFirst();
        }
        Node<E> prev = getNode(length - 2);
        E data = last.getData();
        prev.setNext(null);
        last = prev;
        length--;
        return data;
    }

    /**
     * Elimina y retorna el elemento en la posición dada
     * @throws IndexOutOfBoundsException si pos inválido
     */
    public E delete(int pos) throws Exception {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        }
        if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out of range: " + pos);
        }
        if (pos == 0) {
            return deleteFirst();
        }
        if (pos == length - 1) {
            return deleteLast();
        }
        Node<E> prev = getNode(pos - 1);
        Node<E> current = prev.getNext();
        E data = current.getData();
        prev.setNext(current.getNext());
        length--;
        return data;
    }

    // Búsquedas genéricas y avanzadas

    /**
     * Busca elementos por atributo con tipo de comparación
     * @param atributo nombre del atributo o getter
     * @param valor valor a buscar
     * @param tipo 0=contiene,1=inicia con,2=termina con,3=igual
     * @return lista con resultados
     */
    public LinkedList<E> buscar(String atributo, Object valor, int tipo) {
        LinkedList<E> resultado = new LinkedList<>();
        if (isEmpty() || atributo == null || valor == null) return resultado;

        Node<E> current = head;
        while (current != null) {
            try {
                Object dato = obtenerValorAtributo(current.getData(), atributo);
                if (dato != null) {
                    boolean coincide = false;
                    if (dato instanceof Number && valor instanceof Number) {
                        coincide = compararNumeros((Number) dato, (Number) valor, tipo);
                    } else if (dato instanceof String && valor instanceof String) {
                        coincide = compararTexto((String) dato, (String) valor, tipo);
                    } else {
                        coincide = dato.equals(valor);
                    }
                    if (coincide) {
                        resultado.add(current.getData());
                    }
                }
            } catch (Exception e) {
                // Mejor usar logging en producción
            }
            current = current.getNext();
        }
        return resultado;
    }

    /**
     * Búsqueda con estadísticas de tiempo y comparaciones
     */
    public SearchResult buscarConEstadisticas(String atributo, Object valor, int tipo) {
        long startTime = System.nanoTime();
        int comparaciones = 0;
        LinkedList<E> resultados = new LinkedList<>();

        if (isEmpty() || atributo == null || valor == null) {
            long endTime = System.nanoTime();
            return new SearchResult(-1, endTime - startTime, comparaciones, "Búsqueda Genérica", resultados);
        }

        Node<E> current = head;
        int index = 0;
        int primerIndice = -1;

        while (current != null) {
            try {
                Object dato = obtenerValorAtributo(current.getData(), atributo);
                comparaciones++;

                if (dato != null) {
                    boolean coincide = false;

                    if (dato instanceof Number && valor instanceof Number) {
                        coincide = compararNumeros((Number) dato, (Number) valor, tipo);
                    } else if (dato instanceof String && valor instanceof String) {
                        coincide = compararTexto((String) dato, (String) valor, tipo);
                    } else {
                        coincide = dato.equals(valor);
                    }

                    if (coincide) {
                        if (primerIndice == -1) {
                            primerIndice = index;
                        }
                        resultados.add(current.getData());
                    }
                }
            } catch (Exception e) {
                // Mejor usar logging en producción
            }

            current = current.getNext();
            index++;
        }

        long endTime = System.nanoTime();
        return new SearchResult(primerIndice, endTime - startTime, comparaciones, "Búsqueda Genérica", resultados);
    }

    /**
     * Obtiene valor de atributo usando reflexión (getter o campo)
     */
    private Object obtenerValorAtributo(E objeto, String atributo) throws Exception {
        if (objeto == null || atributo == null) return null;

        try {
            String getterName = "get" + capitalize(atributo);
            Method getter = objeto.getClass().getMethod(getterName);
            return getter.invoke(objeto);
        } catch (NoSuchMethodException e1) {
            try {
                Field field = objeto.getClass().getDeclaredField(atributo);
                field.setAccessible(true);
                return field.get(objeto);
            } catch (NoSuchFieldException e2) {
                try {
                    String isGetterName = "is" + capitalize(atributo);
                    Method isGetter = objeto.getClass().getMethod(isGetterName);
                    return isGetter.invoke(objeto);
                } catch (NoSuchMethodException e3) {
                    throw new Exception("No se encontró el atributo: " + atributo);
                }
            }
        }
    }

    private boolean compararNumeros(Number dato, Number valor, int tipo) {
        double d1 = dato.doubleValue();
        double d2 = valor.doubleValue();

        switch (tipo) {
            case 0: return d1 == d2;
            case 1: return d1 > d2;
            case 2: return d1 < d2;
            case 3: return d1 >= d2;
            case 4: return d1 <= d2;
            default: return d1 == d2;
        }
    }

    private boolean compararTexto(String dato, String valor, int tipo) {
        String datoStr = dato.toLowerCase();
        String valorStr = valor.toLowerCase();

        switch (tipo) {
            case 0: return datoStr.contains(valorStr);    // contiene
            case 1: return datoStr.startsWith(valorStr);  // inicia con
            case 2: return datoStr.endsWith(valorStr);    // termina con
            case 3: return datoStr.equals(valorStr);      // igual
            default: return datoStr.contains(valorStr);
        }
    }

    // Ordenamientos

    public long ordenarPorAtributo(String atributo, boolean ascendente) {
        if (isEmpty() || length <= 1 || atributo == null) return 0;

        long startTime = System.nanoTime();

        Comparator<E> comparador = (obj1, obj2) -> {
            try {
                Object valor1 = obtenerValorAtributo(obj1, atributo);
                Object valor2 = obtenerValorAtributo(obj2, atributo);

                if (valor1 == null && valor2 == null) return 0;
                if (valor1 == null) return ascendente ? -1 : 1;
                if (valor2 == null) return ascendente ? 1 : -1;

                if (valor1 instanceof Comparable && valor2 instanceof Comparable) {
                    return ascendente ? ((Comparable) valor1).compareTo(valor2) : ((Comparable) valor2).compareTo(valor1);
                } else if (valor1 instanceof Number && valor2 instanceof Number) {
                    double d1 = ((Number) valor1).doubleValue();
                    double d2 = ((Number) valor2).doubleValue();
                    return ascendente ? Double.compare(d1, d2) : Double.compare(d2, d1);
                } else {
                    return ascendente ? valor1.toString().compareToIgnoreCase(valor2.toString())
                            : valor2.toString().compareToIgnoreCase(valor1.toString());
                }
            } catch (Exception e) {
                return 0;
            }
        };

        quickSort(comparador);

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long shellSortPorAtributo(String atributo, boolean ascendente) {
        if (isEmpty() || length <= 1 || atributo == null) return 0;

        long startTime = System.nanoTime();

        Comparator<E> comparador = (obj1, obj2) -> {
            try {
                Object valor1 = obtenerValorAtributo(obj1, atributo);
                Object valor2 = obtenerValorAtributo(obj2, atributo);

                if (valor1 == null && valor2 == null) return 0;
                if (valor1 == null) return ascendente ? -1 : 1;
                if (valor2 == null) return ascendente ? 1 : -1;

                if (valor1 instanceof Comparable && valor2 instanceof Comparable) {
                    return ascendente ? ((Comparable) valor1).compareTo(valor2) : ((Comparable) valor2).compareTo(valor1);
                } else if (valor1 instanceof Number && valor2 instanceof Number) {
                    double d1 = ((Number) valor1).doubleValue();
                    double d2 = ((Number) valor2).doubleValue();
                    return ascendente ? Double.compare(d1, d2) : Double.compare(d2, d1);
                } else {
                    return ascendente ? valor1.toString().compareToIgnoreCase(valor2.toString())
                            : valor2.toString().compareToIgnoreCase(valor1.toString());
                }
            } catch (Exception e) {
                return 0;
            }
        };

        shellSort(comparador);

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long quickSort(Comparator<E> comparator) {
        if (isEmpty() || length <= 1) return 0;

        long startTime = System.nanoTime();

        E[] array = toArray();
        quickSortRecursive(array, 0, array.length - 1, comparator);
        toList(array);

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private void quickSortRecursive(E[] array, int low, int high, Comparator<E> comparator) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, comparator);
            quickSortRecursive(array, low, pivotIndex - 1, comparator);
            quickSortRecursive(array, pivotIndex + 1, high, comparator);
        }
    }

    private int partition(E[] array, int low, int high, Comparator<E> comparator) {
        E pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    public long shellSort(Comparator<E> comparator) {
        if (isEmpty() || length <= 1) return 0;

        long startTime = System.nanoTime();

        E[] array = toArray();
        int n = array.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                E temp = array[i];
                int j;
                for (j = i; j >= gap && comparator.compare(array[j - gap], temp) > 0; j -= gap) {
                    array[j] = array[j - gap];
                }
                array[j] = temp;
            }
        }

        toList(array);

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private void swap(E[] array, int i, int j) {
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Búsquedas lineal y binaria

    public SearchResult linearSearch(E target, Comparator<E> comparator) {
        long startTime = System.nanoTime();
        int comparisons = 0;

        Node<E> current = head;
        int index = 0;

        while (current != null) {
            comparisons++;
            if (comparator.compare(current.getData(), target) == 0) {
                long endTime = System.nanoTime();
                LinkedList<E> resultado = new LinkedList<>();
                resultado.add(current.getData());
                return new SearchResult(index, endTime - startTime, comparisons, "Linear Search", resultado);
            }
            current = current.getNext();
            index++;
        }

        long endTime = System.nanoTime();
        return new SearchResult(-1, endTime - startTime, comparisons, "Linear Search", new LinkedList<>());
    }

    public SearchResult binarySearch(E target, Comparator<E> comparator) {
        long startTime = System.nanoTime();
        int comparisons = 0;

        if (isEmpty()) {
            long endTime = System.nanoTime();
            return new SearchResult(-1, endTime - startTime, comparisons, "Binary Search", new LinkedList<>());
        }

        E[] array = toArray();
        int left = 0;
        int right = array.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisons++;

            int comparison = comparator.compare(array[mid], target);

            if (comparison == 0) {
                long endTime = System.nanoTime();
                LinkedList<E> resultado = new LinkedList<>();
                resultado.add(array[mid]);
                return new SearchResult(mid, endTime - startTime, comparisons, "Binary Search", resultado);
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        long endTime = System.nanoTime();
        return new SearchResult(-1, endTime - startTime, comparisons, "Binary Search", new LinkedList<>());
    }

    public SearchResult smartSearch(E target, Comparator<E> comparator, boolean isOrdered) {
        if (isOrdered && length > 10) {
            return binarySearch(target, comparator);
        } else {
            return linearSearch(target, comparator);
        }
    }

    /** Resultado de búsqueda con estadísticas */
    public static class SearchResult {
        private final int index;
        private final long executionTime;
        private final int comparisons;
        private final String method;
        private final LinkedList<?> results;

        public SearchResult(int index, long executionTime, int comparisons, String method, LinkedList<?> results) {
            this.index = index;
            this.executionTime = executionTime;
            this.comparisons = comparisons;
            this.method = method;
            this.results = results;
        }

        public SearchResult(int index, long executionTime, int comparisons, String method) {
            this(index, executionTime, comparisons, method, new LinkedList<>());
        }

        public int getIndex() { return index; }
        public long getExecutionTime() { return executionTime; }
        public int getComparisons() { return comparisons; }
        public String getMethod() { return method; }
        public boolean isFound() { return index != -1; }
        public LinkedList<?> getResults() { return results; }
        public int getResultCount() { return results.getLength(); }

        @Override
        public String toString() {
            return String.format("SearchResult{method='%s', found=%s, index=%d, results=%d, time=%d ns, comparisons=%d}",
                    method, isFound(), index, getResultCount(), executionTime, comparisons);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /** Verifica si la lista está ordenada según el comparador */
    public boolean isSorted(Comparator<E> comparator) {
        if (isEmpty() || length <= 1) return true;

        Node<E> current = head;
        while (current.getNext() != null) {
            if (comparator.compare(current.getData(), current.getNext().getData()) > 0) {
                return false;
            }
            current = current.getNext();
        }
        return true;
    }

    /** Genera datos aleatorios usando función generadora */
    public void generateRandomData(int count, Function<Integer, E> generator) {
        clear();
        for (int i = 0; i < count; i++) {
            add(generator.apply(i));
        }
    }

    /** Copia la lista */
    public LinkedList<E> copy() {
        LinkedList<E> newList = new LinkedList<>();
        Node<E> current = head;
        while (current != null) {
            newList.add(current.getData());
            current = current.getNext();
        }
        return newList;
    }

    /** Convierte a java.util.List */
    public List<E> toJavaList() {
        List<E> list = new ArrayList<>();
        Node<E> current = head;
        while (current != null) {
            list.add(current.getData());
            current = current.getNext();
        }
        return list;
    }

    /** Busca primer elemento que cumple el predicado */
    public E find(Function<E, Boolean> predicate) {
        Node<E> current = head;
        while (current != null) {
            if (predicate.apply(current.getData())) {
                return current.getData();
            }
            current = current.getNext();
        }
        return null;
    }

    /** Mapea elementos con función */
    public <R> List<R> map(Function<E, R> mapper) {
        List<R> result = new ArrayList<>();
        for (E item : this) {
            result.add(mapper.apply(item));
        }
        return result;
    }

    /** Filtra elementos con predicado */
    public LinkedList<E> filter(Function<E, Boolean> predicate) {
        LinkedList<E> result = new LinkedList<>();
        for (E item : this) {
            if (predicate.apply(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /** Verifica si existe elemento que cumple predicado */
    public boolean contains(Function<E, Boolean> predicate) {
        return find(predicate) != null;
    }

    /** Actualiza primer elemento que cumple predicado */
    public boolean updateIf(Function<E, Boolean> predicate, E newData) {
        Node<E> current = head;
        int pos = 0;
        while (current != null) {
            if (predicate.apply(current.getData())) {
                update(newData, pos);
                return true;
            }
            current = current.getNext();
            pos++;
        }
        return false;
    }

    /** Remueve primer elemento que cumple predicado */
    public boolean removeIf(Function<E, Boolean> predicate) {
        Node<E> current = head;
        int pos = 0;
        while (current != null) {
            if (predicate.apply(current.getData())) {
                try {
                    delete(pos);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            current = current.getNext();
            pos++;
        }
        return false;
    }

    /** Busca elemento por id (asumiendo método getId) */
    public E findById(Long id) {
        return find(item -> {
            try {
                return item.getClass().getMethod("getId").invoke(item).equals(id);
            } catch (Exception e) {
                return false;
            }
        });
    }

    // Nodo interno
    private static class Node<E> {
        private E data;
        private Node<E> next;

        public Node(E data) {
            this.data = data;
            this.next = null;
        }

        public Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }

        public E getData() { return data; }
        public void setData(E data) { this.data = data; }
        public Node<E> getNext() { return next; }
        public void setNext(Node<E> next) { this.next = next; }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() { return current != null; }

            @Override
            public E next() {
                if (current == null) throw new NoSuchElementException();
                E data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }
}