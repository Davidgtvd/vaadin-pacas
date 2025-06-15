package org.unl.pacas.base.controller.data_struct.list;

import java.util.*;
import java.util.function.Function;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.util.Comparator;

public class LinkedList<E> implements Iterable<E> {
    private Node<E> head;
    private Node<E> last;
    private Integer length;

    public LinkedList() {
        head = null;
        last = null;
        length = 0;
    }

    public Integer getLength() {
        return this.length;
    }

    public Boolean isEmpty() {
        return head == null || length == 0;
    }

    private Node<E> getNode(Integer pos) {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        } else if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out range");
        } else if (pos == 0) {
            return head;
        } else if ((length.intValue() - 1) == pos.intValue()) {
            return last;
        } else {
            Node<E> search = head;
            Integer cont = 0;
            while (cont < pos) {
                cont++;
                search = search.getNext();
            }
            return search;
        }
    }

    public E get(Integer pos) {
        return getNode(pos).getData();
    }

    private void addFirst(E data) {
        if (isEmpty()) {
            Node<E> aux = new Node<>(data);
            head = aux;
            last = aux;
        } else {
            Node<E> head_old = head;
            Node<E> aux = new Node<>(data, head_old);
            head = aux;
        }
        length++;
    }

    private void addLast(E data) {
        if (isEmpty()) {
            addFirst(data);
        } else {
            Node<E> aux = new Node<>(data);
            last.setNext(aux);
            last = aux;
            length++;
        }
    }

    public void add(E data, Integer pos) throws Exception {
        if (pos == 0) {
            addFirst(data);
        } else if (length.intValue() == pos.intValue()) {
            addLast(data);
        } else {
            Node<E> search_preview = getNode(pos - 1);
            Node<E> search = getNode(pos);
            Node<E> aux = new Node<>(data, search);
            search_preview.setNext(aux);
            length++;
        }
    }

    public void add(E data) {
        addLast(data);
    }

    public String print() {
        if (isEmpty())
            return "Esta vacía";
        else {
            StringBuilder resp = new StringBuilder();
            Node<E> help = head;
            while (help != null) {
                resp.append(help.getData()).append(" - ");
                help = help.getNext();
            }
            resp.append("\n");
            return resp.toString();
        }
    }

    public void update(E data, Integer pos) {
        getNode(pos).setData(data);
    }

    public void clear() {
        head = null;
        last = null;
        length = 0;
    }

    @SuppressWarnings("unchecked")
    public E[] toArray() {
        Class<?> clazz = null;
        E[] matriz = null;
        if (this.length > 0) {
            clazz = head.getData().getClass();
            matriz = (E[]) Array.newInstance(clazz, this.length);
            Node<E> aux = head;
            for (int i = 0; i < length; i++) {
                matriz[i] = aux.getData();
                aux = aux.getNext();
            }
        }
        return matriz;
    }

    public LinkedList<E> toList(E[] matriz) {
        clear();
        for (int i = 0; i < matriz.length; i++) {
            this.add(matriz[i]);
        }
        return this;
    }

    protected E deleteFirst() throws Exception {
        if (isEmpty()) {
            throw new Exception("List empty");
        } else {
            E element = head.getData();
            Node<E> aux = head.getNext();
            head = aux;
            if (length.intValue() == 1)
                last = null;
            length--;
            return element;
        }
    }

    protected E deleteLast() throws Exception {
        if (isEmpty()) {
            throw new Exception("List empty");
        } else {
            E element = last.getData();
            Node<E> aux = getNode(length - 2);
            if (aux == null) {
                last = null;
                if (length == 2) {
                    last = head;
                } else {
                    head = null;
                }
            } else {
                last = null;
                last = aux;
                last.setNext(null);
            }
            length--;
            return element;
        }
    }

    public E delete(Integer pos) throws Exception {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        } else if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out range");
        } else if (pos == 0) {
            return deleteFirst();
        } else if ((length.intValue() - 1) == pos.intValue()) {
            return deleteLast();
        } else {
            Node<E> preview = getNode(pos - 1);
            Node<E> actualy = getNode(pos);
            E element = actualy.getData();
            Node<E> next = actualy.getNext();
            actualy = null;
            preview.setNext(next);
            length--;
            return element;
        }
    }

    // MÉTODOS DE BÚSQUEDA

    /**
     * Búsqueda genérica por atributo (por nombre de campo o getter)
     * @param atributo Nombre del atributo a buscar
     * @param valor Valor a buscar
     * @param tipo Tipo de búsqueda: 0=contiene, 1=inicia con, 2=termina con, 3=igual
     * @return Lista con los resultados encontrados
     */
    public LinkedList<E> buscar(String atributo, Object valor, int tipo) {
        LinkedList<E> resultado = new LinkedList<>();

        if (isEmpty() || atributo == null || valor == null) {
            return resultado;
        }

        Node<E> current = head;
        while (current != null) {
            try {
                Object dato = obtenerValorAtributo(current.getData(), atributo);

                if (dato != null) {
                    boolean coincide = false;

                    if (dato instanceof Number && valor instanceof Number) {
                        coincide = compararNumeros((Number)dato, (Number)valor, tipo);
                    }
                    else if (dato instanceof String && valor instanceof String) {
                        coincide = compararTexto((String)dato, (String)valor, tipo);
                    }
                    else {
                        coincide = dato.equals(valor);
                    }

                    if (coincide) {
                        resultado.add(current.getData());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al buscar por atributo '" + atributo + "': " + e.getMessage());
            }

            current = current.getNext();
        }

        return resultado;
    }

    /**
     * Búsqueda con estadísticas
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
                        coincide = compararNumeros((Number)dato, (Number)valor, tipo);
                    } else if (dato instanceof String && valor instanceof String) {
                        coincide = compararTexto((String)dato, (String)valor, tipo);
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
                System.err.println("Error en búsqueda: " + e.getMessage());
            }

            current = current.getNext();
            index++;
        }

        long endTime = System.nanoTime();
        return new SearchResult(primerIndice, endTime - startTime, comparaciones, "Búsqueda Genérica", resultados);
    }

    /**
     * Obtiene el valor de un atributo usando reflexión
     */
    private Object obtenerValorAtributo(E objeto, String atributo) throws Exception {
        if (objeto == null || atributo == null) {
            return null;
        }

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

    // Métodos de ordenamiento y búsqueda avanzada

    public long ordenarPorAtributo(String atributo, boolean ascendente) {
        if (isEmpty() || length <= 1 || atributo == null) {
            return 0;
        }

        long startTime = System.nanoTime();

        Comparator<E> comparador = (obj1, obj2) -> {
            try {
                Object valor1 = obtenerValorAtributo(obj1, atributo);
                Object valor2 = obtenerValorAtributo(obj2, atributo);

                if (valor1 == null && valor2 == null) return 0;
                if (valor1 == null) return ascendente ? -1 : 1;
                if (valor2 == null) return ascendente ? 1 : -1;

                int resultado = 0;

                if (valor1 instanceof Comparable && valor2 instanceof Comparable) {
                    resultado = ((Comparable)valor1).compareTo(valor2);
                } else if (valor1 instanceof Number && valor2 instanceof Number) {
                    double d1 = ((Number)valor1).doubleValue();
                    double d2 = ((Number)valor2).doubleValue();
                    resultado = Double.compare(d1, d2);
                } else {
                    resultado = valor1.toString().compareToIgnoreCase(valor2.toString());
                }

                return ascendente ? resultado : -resultado;

            } catch (Exception e) {
                System.err.println("Error en comparación: " + e.getMessage());
                return 0;
            }
        };

        quickSort(comparador);

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long shellSortPorAtributo(String atributo, boolean ascendente) {
        if (isEmpty() || length <= 1 || atributo == null) {
            return 0;
        }

        long startTime = System.nanoTime();

        Comparator<E> comparador = (obj1, obj2) -> {
            try {
                Object valor1 = obtenerValorAtributo(obj1, atributo);
                Object valor2 = obtenerValorAtributo(obj2, atributo);

                if (valor1 == null && valor2 == null) return 0;
                if (valor1 == null) return ascendente ? -1 : 1;
                if (valor2 == null) return ascendente ? 1 : -1;

                int resultado = 0;

                if (valor1 instanceof Comparable && valor2 instanceof Comparable) {
                    resultado = ((Comparable)valor1).compareTo(valor2);
                } else if (valor1 instanceof Number && valor2 instanceof Number) {
                    double d1 = ((Number)valor1).doubleValue();
                    double d2 = ((Number)valor2).doubleValue();
                    resultado = Double.compare(d1, d2);
                } else {
                    resultado = valor1.toString().compareToIgnoreCase(valor2.toString());
                }

                return ascendente ? resultado : -resultado;

            } catch (Exception e) {
                System.err.println("Error en comparación: " + e.getMessage());
                return 0;
            }
        };

        shellSort(comparador);

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long quickSort(Comparator<E> comparator) {
        if (isEmpty() || length <= 1) {
            return 0;
        }

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
        if (isEmpty() || length <= 1) {
            return 0;
        }

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
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public boolean isSorted(Comparator<E> comparator) {
        if (isEmpty() || length <= 1) {
            return true;
        }

        Node<E> current = head;
        while (current.getNext() != null) {
            if (comparator.compare(current.getData(), current.getNext().getData()) > 0) {
                return false;
            }
            current = current.getNext();
        }
        return true;
    }

    public void generateRandomData(int count, Function<Integer, E> generator) {
        clear();
        for (int i = 0; i < count; i++) {
            add(generator.apply(i));
        }
    }

    public LinkedList<E> copy() {
        LinkedList<E> newList = new LinkedList<>();
        Node<E> current = head;
        while (current != null) {
            newList.add(current.getData());
            current = current.getNext();
        }
        return newList;
    }

    public List<E> toJavaList() {
        List<E> list = new ArrayList<>();
        Node<E> current = head;
        while (current != null) {
            list.add(current.getData());
            current = current.getNext();
        }
        return list;
    }

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

    public <R> List<R> map(Function<E, R> mapper) {
        List<R> result = new ArrayList<>();
        for (E item : this) {
            result.add(mapper.apply(item));
        }
        return result;
    }

    public LinkedList<E> filter(Function<E, Boolean> predicate) {
        LinkedList<E> result = new LinkedList<>();
        for (E item : this) {
            if (predicate.apply(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public boolean contains(Function<E, Boolean> predicate) {
        return find(predicate) != null;
    }

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

    public int size() {
        return length;
    }

    public Collection<E> toCollection() {
        return toJavaList();
    }

    public E findById(Long id) {
        return find(item -> {
            try {
                return item.getClass().getMethod("getId").invoke(item).equals(id);
            } catch (Exception e) {
                return false;
            }
        });
    }

    // Clase interna Node
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
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }
}