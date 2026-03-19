package lab4;

import lab2.Crossroad;
import lab2.Settlement;

import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Клас демонструє дженерик-методи, що узагальнюють методи з Lab 2.
 *
 * <p>Завдання 2: узагальнення методів класу Crossroad/Settlement.
 * Кожен дженерик-метод порівнюється з не-дженерик аналогом.
 */
public class GenericMethods {

    // =====================================================================
    //  ДЖЕНЕРИК-МЕТОДИ
    // =====================================================================

    /**
     * Знаходить максимальний елемент масиву за вказаним компаратором.
     * <p>Узагальнення: {@code Crossroad.findMostDangerous(Crossroad[])} з Lab 2,
     * де «максимум» визначався як найвищий рівень небезпеки.
     *
     * @param <T>        тип елементів
     * @param array      масив елементів (не null, не порожній)
     * @param comparator критерій порівняння
     * @return максимальний елемент або null, якщо масив порожній
     */
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

    /**
     * Знаходить мінімальний елемент масиву за вказаним компаратором.
     *
     * @param <T>        тип елементів
     * @param array      масив елементів
     * @param comparator критерій порівняння
     * @return мінімальний елемент або null
     */
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

    /**
     * Бульбашкове сортування масиву (за зростанням за компаратором).
     * <p>Узагальнення ручного сортування перехресть за ДТП/рік з Main.java Lab 2.
     *
     * @param <T>        тип елементів
     * @param array      масив, що сортується (змінюється «на місці»)
     * @param comparator критерій порівняння
     */
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

    /**
     * Лінійний пошук першого елемента, що задовольняє предикат.
     * <p>Узагальнення пошуку перехрестя за назвою (цикл по масиву) з Lab 2.
     *
     * @param <T>       тип елементів
     * @param array     масив для пошуку
     * @param predicate умова відбору
     * @return перший знайдений елемент або null
     */
    public static <T> T linearSearch(T[] array, Predicate<T> predicate) {
        for (T item : array) {
            if (item != null && predicate.test(item)) return item;
        }
        return null;
    }

    /**
     * Підраховує кількість елементів масиву, що задовольняють предикат.
     *
     * @param <T>       тип елементів
     * @param array     масив для обходу
     * @param predicate умова відбору
     * @return кількість елементів, що задовольняють умову
     */
    public static <T> int countIf(T[] array, Predicate<T> predicate) {
        int count = 0;
        for (T item : array) {
            if (item != null && predicate.test(item)) count++;
        }
        return count;
    }

    /**
     * Обчислює суму елементів масиву числових типів.
     * Тип T обмежений зверху — має бути підтипом Number.
     *
     * @param <T>   числовий тип
     * @param array масив чисел
     * @return сума як double
     */
    public static <T extends Number> double sum(T[] array) {
        double total = 0;
        for (T item : array) {
            if (item != null) total += item.doubleValue();
        }
        return total;
    }

    /**
     * Повертає рядкове представлення масиву будь-якого типу.
     *
     * @param <T>   тип елементів
     * @param array масив для виведення
     * @return рядок вигляду [e1, e2, ...]
     */
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

    // =====================================================================
    //  НЕ-ДЖЕНЕРИК АНАЛОГИ (для порівняння)
    // =====================================================================

    /**
     * Не-дженерик: знайти найнебезпечніше перехрестя.
     * Аналог {@code Crossroad.findMostDangerous()} з Lab 2.
     * Працює ТІЛЬКИ з Crossroad[].
     */
    public static Crossroad findMostDangerousCrossroad(Crossroad[] array) {
        if (array == null || array.length == 0) return null;
        Crossroad max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] != null
                    && array[i].getStats().getDangerLevel()
                    > max.getStats().getDangerLevel()) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * Не-дженерик: знайти найбільше місто за населенням.
     * Аналог {@code Settlement.getLargerByPopulation()} з Lab 2.
     * Працює ТІЛЬКИ з Settlement[].
     */
    public static Settlement findLargestSettlement(Settlement[] array) {
        if (array == null || array.length == 0) return null;
        Settlement max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] != null
                    && array[i].getPopulation() > max.getPopulation()) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * Не-дженерик: сортування перехресть за ДТП (спадання).
     * Аналог сортування у Main.java Lab 2.
     * Працює ТІЛЬКИ з Crossroad[].
     */
    public static void sortCrossroadsByAccidentsDesc(Crossroad[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j].getStats().getAccidentsPerYear()
                        < array[j + 1].getStats().getAccidentsPerYear()) {
                    Crossroad temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}