package lesson3;

import java.util.*;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;
        Node<T> parent = null;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     * <p>
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * <p>
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     * <p>
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
            newNode.parent = closest;
        } else {
            assert closest.right == null;
            closest.right = newNode;
            newNode.parent = closest;
        }
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     * <p>
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     * <p>
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     * <p>
     * Средняя
     */
    //O(n) - по времени
    //O(1) - по памяти
    //тесты полные
    @Override
    public boolean remove(Object o) {
        if (root == null) return false;
        T t = (T) o;
        return remove(root, t);
    }

    private boolean remove(Node<T> start, T value) {
        Node<T> removeEl = find(value);
        if (removeEl == null || removeEl.value != value) return false;
        int compare = value.compareTo(start.value);
        if (compare < 0) remove(start.left, value);
        else if (compare > 0) remove(start.right, value);
        else {
            Node<T> parent = removeEl.parent;
            if (removeEl.left != null && removeEl.right != null) { //если у remove 2 ребенка
                Node<T> theLeft = start.right; //находим самый левый у прового ребенка
                while (theLeft.left != null) {
                    theLeft = theLeft.left;
                }
                remove(start.right, theLeft.value); // потом удаляем самый левый
                theLeft.left = removeEl.left;
                if (start.left != null) removeEl.left.parent = theLeft;
                theLeft.right = start.right;
                if (start.right != null) removeEl.right.parent = theLeft;
                if (parent != null) { //вставляем на место remove самый левый
                    theLeft.parent = removeEl.parent;
                    if (parent.right != null && parent.right.value == removeEl.value) parent.right = theLeft;
                    else parent.left = theLeft;
                } else {
                    theLeft.parent = null;
                    root = theLeft;
                }
                return true;
            } else if (removeEl.left == null && removeEl.right != null) { //если у remove только правый ребенок
                if (parent != null) {
                    removeEl.right.parent = removeEl.parent;
                    if (parent.right != null && parent.right.value == removeEl.value) parent.right = removeEl.right;
                    else parent.left = removeEl.right;
                } else {
                    removeEl.right.parent = null;
                    root = removeEl.right;
                }
                size--;
                return true;
            } else if (removeEl.left != null) { //если у remove только левый ребенок
                if (parent != null) {
                    removeEl.left.parent = removeEl.parent;
                    if (parent.right != null && parent.right.value == removeEl.value) parent.right = removeEl.left;
                    else parent.left = removeEl.left;
                } else {
                    removeEl.left.parent = null;
                    root = removeEl.left;
                }
                size--;
                return true;
            } else { //если у remove нет детей
                if (parent != null) {
                    if (parent.right != null && parent.right.value == removeEl.value) parent.right = null;
                    else parent.left = null;
                    size--;
                    return true;
                }
            }
        }
        return true;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator(root);
    }

    public class BinarySearchTreeIterator implements Iterator<T> {
        ArrayDeque<Node<T>> deque;
        Node<T> previous;
        boolean flag = false;

        private BinarySearchTreeIterator(Node<T> root) {
            deque = new ArrayDeque<>();
            goLeft(root);
        }

        /**
         * Проверка наличия следующего элемента
         * <p>
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         * <p>
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         * <p>
         * Средняя
         */
        @Override
        public boolean hasNext() {
            return !deque.isEmpty();
        }

        /**
         * Получение следующего элемента
         * <p>
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         * <p>
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         * <p>
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         * <p>
         * Средняя
         */
        //тесты полные
        //O(n) - по времени
        //O(n) - по памяти
        @Override
        public T next() {
            flag = true;
            if (deque.isEmpty()) throw new IllegalStateException();
            Node<T> node = deque.pop();
            if (node.right != null) {
                if (node.right.left != null) {
                    goLeft(node.right);
                } else deque.push(node.right);
            }
            previous = node;
            return node.value;
        }

        private void goLeft(Node<T> root) {
            while (root != null) {
                deque.push(root);
                root = root.left;
            }
        }

        /**
         * Удаление предыдущего элемента
         * <p>
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         * <p>
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         * <p>
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         * <p>
         * Сложная
         */
        //тесты полные
        //O(n) - по времени
        //O(n) - по памяти
        @Override
        public void remove() {
            if (!flag) throw new IllegalStateException();
            flag = false;
            BinarySearchTree.this.remove(previous.value);
        }
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

}