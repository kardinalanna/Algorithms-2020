package lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

//@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    //O(n log(n)) - по времени
    //O(n) - по времени
    static public void sortTimes(String inputName, String outputName) throws IOException {
        List<String[]> list = new ArrayList<String[]>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) {
            String st;
            while ((st = reader.readLine()) != null) {
                if (!Pattern.matches("\\d{2}:\\d{2}:\\d{2}\\s(AM|PM)", st)) throw new IllegalArgumentException();
                list.add(st.split(":| "));
            }
            int[] newFormat = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String[] p = list.get(i);
                int hour = Integer.parseInt(p[0]);
                int min = Integer.parseInt(p[1]);
                int sec = Integer.parseInt(p[2]);
                int time = hour * 3600 + min * 60 + sec;
                switch (p[3]) {
                    case "AM":
                        if (hour != 12) newFormat[i] = time;
                        else newFormat[i] = time - 12 * 3600;
                        break;
                    case "PM":
                        if (hour != 12) newFormat[i] = time + 12 * 3600;
                        else newFormat[i] = time;
                        break;
                }
            }
            Sorts.mergeSort(newFormat);
            for (int time : newFormat) {
                int hourSt = time / 3600;
                int minSt = (time % 3600) / 60;
                int secSt = time % 3600 % 60;
                writer.write(writeString(hourSt, minSt, secSt) + "\n");
            }
        }
    }


    static String writeString(int hour, int min, int sec) {
        String hourRes;
        String minRes;
        String secRes;
        String spHour = null;
        if ((hour - 12 > 0 && hour - 12 < 10)) spHour = "0" + (hour - 12);
        else if (hour - 12 > 0) spHour = Integer.toString(hour - 12);
        if (hour < 10) hourRes = "0" + hour;
        else hourRes = Integer.toString(hour);
        if (min < 10) minRes = "0" + min;
        else minRes = Integer.toString(min);
        if (sec < 10) secRes = "0" + sec;
        else secRes = Integer.toString(sec);
        if (hour < 12) {
            if (hour == 0) return "12:" + minRes + ":" + secRes + " AM";
            else return hourRes + ":" + minRes + ":" + secRes + " AM";
        } else {
            if (hour == 12) return "12" + ":" + minRes + ":" + secRes + " PM";
            else return spHour + ":" + minRes + ":" + secRes + " PM";
        }
    }


    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    //O(n) - временные затраты
    //O(n) - затраты по памяти
    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        List<Integer> posList = new ArrayList<>();
        List<Integer> negList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) {
            String st;
            while ((st = reader.readLine()) != null) {
                int elem = (int) Math.round(Double.parseDouble(st) * 10);
                if (elem >= 0) posList.add(elem);
                else negList.add(Math.abs(elem));
            }
            int[] posAr = new int[posList.size()];
            int[] negAr = new int[negList.size()];

            for (int i = 0; i < Math.max(negAr.length, posAr.length); i++) {
                if (i < negAr.length) negAr[i] = negList.get(i);
                if (i < posAr.length) posAr[i] = posList.get(i);
            }
            posAr = Sorts.countingSort(posAr, 5000);
            negAr = Sorts.countingSort(negAr, 2730);
            for (int d = negAr.length - 1; d >= 0; d--) writer.write("-" + (negAr[d] / 10) + "." + (negAr[d] % 10) + "\n");
            for (int d : posAr) writer.write(d / 10 + "." + (d % 10) + "\n");
        }
    }
    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    //O(n) - временные затраты
    //O(n) - затраты по памяти
    static public void sortSequence(String inputName, String outputName) throws IOException {
        ArrayList<Integer> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) {
            String st;
            while ((st = reader.readLine()) != null) {
                list.add(Integer.parseInt(st));
            }
            HashMap<Integer, Integer> map = new HashMap<>();
            for (Integer element : list) {
                if (map.containsKey(element)) map.put(element, map.get(element) + 1);
                else map.put(element, 0);
            }
            int max = -1;
            int maxIn = 0;
            for (Integer element : map.keySet()) {
                int count = map.get(element);
                if (count > max || (count == max && element < maxIn)) {
                    max = count;
                    maxIn = element;
                }
            }
            for (int f : list) {
                if (f != maxIn) writer.write(f + "\n");
            }
            for (int j = 0; j <= max; j++) writer.write(maxIn + "\n");
        }
    }
    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
