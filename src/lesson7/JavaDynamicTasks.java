package lesson7;

import kotlin.NotImplementedError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     * <p>
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    //O(n^2) - по времени
    //O(n) - по памяти
    public static String longestCommonSubSequence(String first, String second) {
        int y = 0;
        char[] firstAr = new char[first.length() + 1];
        char[] secondAr = new char[second.length() + 1];
        for (char c : first.toCharArray()) {
            if (y < firstAr.length) firstAr[y] = c;
            y++;
        }
        y = 0;
        for (char c : second.toCharArray()) {
            if (y < secondAr.length) secondAr[y] = c;
            y++;
        }

        int[][] matrix = new int[firstAr.length][secondAr.length];
        for (int i = first.length() - 1; i >= 0; i--) {
            for (int j = second.length() - 1; j >= 0; j--) {
                if (firstAr[i] == secondAr[j]) matrix[i][j] = 1 + matrix[i + 1][j + 1];
                else matrix[i][j] = Math.max(matrix[i + 1][j], matrix[i][j + 1]);
            }
        }
        StringBuilder result = new StringBuilder();
        int t = 0;
        int v = 0;
        while (t < first.length() && v < second.length()) {
            if (firstAr[t] == secondAr[v]) {
                result.append(firstAr[t]);
                v++;
                t++;
            } else if (matrix[t + 1][v] > matrix[t][v + 1]) t++;
            else v++;
        }
        return result.toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     * <p>
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    //O(n^2) - по времени
    //O(n) - по памяти
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        if (list.size() == 0) return list;
        if (list.size() == 1) return list;
        Integer[] array = new Integer[list.size()];
        int u = 0;
        for (Integer p : list) {
            array[u] = p;
            u++;
        }
        Integer[] ancestorArray = new Integer[list.size()];
        Integer[] maxLengthForEach = new Integer[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            maxLengthForEach[i] = 1;
            ancestorArray[i] = -1;
            for (int j = 0; j < i; ++j) {
                if (array[j] < array[i]) {
                    if (1 + maxLengthForEach[j] > maxLengthForEach[i]) {
                        maxLengthForEach[i] = 1 + maxLengthForEach[j];
                        ancestorArray[i] = j;
                    }
                }
            }
        }

        Integer max = maxLengthForEach[0];
        int ind = 0;
        for (int i = 0; i < list.size(); i++) {
            if (maxLengthForEach[i] > max) {
                max = maxLengthForEach[i];
                ind = i;
            }
        }
        Integer[] res = new Integer[max];
        while (ind != -1) {
            res[max - 1] = array[ind];
            max--;
            ind = ancestorArray[ind];
        }
        return Arrays.asList(res.clone());
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     * <p>
     * В файле с именем inputName задано прямоугольное поле:
     * <p>
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     * <p>
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     * <p>
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}

