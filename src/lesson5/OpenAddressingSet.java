package lesson5;

import kotlin.NotImplementedError;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    static class Node {
        Object value;
        boolean state;
        Node(Object value){
            this.value = value;
            state = true;
        }
    }

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Node current = (Node) storage[index];
        while (current != null) {
            if (current.value.equals(o) && current.state) {
                return true;
            }
            index = (index + 1) %   capacity;
            current = (Node) storage[index];
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Node current = (Node) storage[index];
        while (current != null) {
            if (current.value.equals(t) && current.state) {
                return false;
            }
            if (!current.state) {
                current.value = t;
                current.state = true;
                size++;
                return true;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = (Node)storage[index];
        }
        storage[index] = new Node(t);
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */
    //O(n) - по времени
    //O(n) - по памяти
    @Override
    public boolean remove(Object o) {
        int thisSize = size;
        int startInd = startingIndex(o);
        Node current = (Node) storage[startInd];
        while (thisSize != 0){
            if (current != null && current.value.equals(o) && current.state) {
                current.state = false;
                size--;
                return true;
            }
            startInd = (startInd + 1) % capacity;
            current = (Node) storage[startInd];
            thisSize--;
        }
        return false;
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() { return new OpenAddressingSetIterator();}

    class OpenAddressingSetIterator implements Iterator<T>{
        int count = size;
        int start = 0;
        Node node = (Node) storage[start];
        boolean flag = false;

        //O(1) - по врпмени
        //O(1) - по памяти
        @Override
        public boolean hasNext() {
            count--;
            return count >= 0;
        }

        //O(n) - по времени
        //O(1) - по памяти
        @Override
        public T next() {
            if (size == 0 || count < 0) throw new IllegalStateException();
            flag = true;
            for (;start < capacity; start++) {
                node = (Node) storage[start];
                if (node != null && node.state) {
                    start++;
                    return (T) node.value;
                }
            }
            return (T) node.value;
        }

        //O(n) - по врпмени
        //O(1) - по памяти
        @Override
        public void remove() {
            if (!flag) throw new IllegalStateException();
           OpenAddressingSet.this.remove(node.value);
            flag = false;
        }
    }
}





