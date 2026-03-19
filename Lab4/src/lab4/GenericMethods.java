package lab4;

import java.util.Comparator;
import java.util.function.Predicate;

public class GenericMethods {

    public static <T> T findMax(T[] array, Comparator<T> comparator) {
        if (array == null || array.length == 0) return null;
        T max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] != null && comparator.compare(array[i], max) > 0) {
                max = array[i];
            }
        }
        return max;
    }

    public static <T> T findMin(T[] array, Comparator<T> comparator) {
        if (array == null || array.length == 0) return null;
        T min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] != null && comparator.compare(array[i], min) < 0) {
                min = array[i];
            }
        }
        return min;
    }

    public static <T> void bubbleSort(T[] array, Comparator<T> comparator) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparator.compare(array[j], array[j + 1]) > 0) {
                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    public static <T> T linearSearch(T[] array, Predicate<T> predicate) {
        for (T item : array) {
            if (item != null && predicate.test(item)) return item;
        }
        return null;
    }

    public static <T> int countIf(T[] array, Predicate<T> predicate) {
        int count = 0;
        for (T item : array) {
            if (item != null && predicate.test(item)) count++;
        }
        return count;
    }

    public static <T extends Number> double sum(T[] array) {
        double total = 0;
        for (T item : array) {
            if (item != null) total += item.doubleValue();
        }
        return total;
    }

    public static <T> String arrayToString(T[] array) {
        if (array == null) return "null";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}