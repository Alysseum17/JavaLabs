package lab4;

import lab2.Crossroad;
import lab2.Settlement;
import lab2.UnregulatedCrossroad;

import java.util.*;

/**
 * Клас для демонстрації підстановочних типів (wildcards).
 *
 * <p>Завдання 4: методи з аргументом типу {@code List<?>}.
 * Завдання 5: приклади підстановочних типів у бібліотечних класах Java.
 */
public class WildcardUtils {

    // =====================================================================
    //  UNBOUNDED WILDCARD  List<?>
    // =====================================================================

    /**
     * Виводить будь-який список на екран (не залежить від типу елементів).
     * Аналог: у Lab 2 printInfo() виводив тільки Crossroad[] — тут будь-який List.
     *
     * @param list список будь-якого типу
     */
    public static void printList(List<?> list) {
        System.out.println("Список [" + list.size() + " елементів]:");
        for (Object elem : list) {
            System.out.println("  - " + elem);
        }
    }

    /**
     * Повертає кількість ненульових елементів.
     * Unbounded wildcard: читаємо елементи як Object — немає потреби знати точний тип.
     *
     * @param list список будь-якого типу
     * @return кількість ненульових елементів
     */
    public static int countNonNull(List<?> list) {
        int count = 0;
        for (Object elem : list) {
            if (elem != null) count++;
        }
        return count;
    }

    /**
     * Перевіряє, чи містить список задане значення (через equals).
     *
     * @param list  список будь-якого типу
     * @param value значення для пошуку
     * @return true, якщо знайдено
     */
    public static boolean contains(List<?> list, Object value) {
        for (Object elem : list) {
            if (Objects.equals(elem, value)) return true;
        }
        return false;
    }

    /**
     * Перевертає список «на місці», використовуючи wildcard capture helper.
     * Демонструє паттерн Wildcard Capture and Helper Methods (лекція).
     *
     * @param list список будь-якого типу
     */
    public static void reverse(List<?> list) {
        reverseHelper(list);
    }

    private static <T> void reverseHelper(List<T> list) {
        int n = list.size();
        for (int i = 0; i < n / 2; i++) {
            T temp = list.get(i);
            list.set(i, list.get(n - 1 - i));
            list.set(n - 1 - i, temp);
        }
    }

    // =====================================================================
    //  UPPER BOUNDED WILDCARD  List<? extends ...>
    // =====================================================================

    /**
     * Обчислює суму числових значень списку будь-якого підтипу Number.
     * Приймає List&lt;Integer&gt;, List&lt;Double&gt;, List&lt;Long&gt; тощо.
     *
     * @param list список числових значень (підтипи Number)
     * @return сума як double
     */
    public static double sumNumbers(List<? extends Number> list) {
        double sum = 0;
        for (Number n : list) sum += n.doubleValue();
        return sum;
    }

    /**
     * Обчислює середнє числових значень.
     *
     * @param list список числових значень
     * @return середнє як double або 0 для порожнього списку
     */
    public static double average(List<? extends Number> list) {
        if (list.isEmpty()) return 0;
        return sumNumbers(list) / list.size();
    }

    /**
     * Знаходить максимальний елемент у списку Comparable-типів.
     * Приймає List&lt;Integer&gt;, List&lt;String&gt;, List&lt;Double&gt; тощо.
     *
     * @param <T>  тип елементів (підтип Comparable&lt;T&gt;)
     * @param list непорожній список
     * @return максимальний елемент
     */
    public static <T extends Comparable<T>> T findMax(List<? extends T> list) {
        if (list.isEmpty()) throw new NoSuchElementException("List is empty");
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) max = item;
        }
        return max;
    }

    /**
     * Знаходить найнебезпечніше перехрестя з будь-якого списку Crossroad
     * (або його підтипів, зокрема UnregulatedCrossroad).
     * Приймає List&lt;Crossroad&gt;, List&lt;UnregulatedCrossroad&gt; тощо.
     *
     * @param list список перехресть
     * @return найнебезпечніше перехрестя або null
     */
    public static Crossroad findMostDangerous(List<? extends Crossroad> list) {
        if (list.isEmpty()) return null;
        Crossroad worst = list.get(0);
        for (Crossroad c : list) {
            if (c.getStats().getDangerLevel() > worst.getStats().getDangerLevel()) {
                worst = c;
            }
        }
        return worst;
    }

    /**
     * Обчислює середній рівень небезпеки перехресть.
     *
     * @param list список перехресть
     * @return середній рівень небезпеки
     */
    public static double avgDangerLevel(List<? extends Crossroad> list) {
        if (list.isEmpty()) return 0;
        double sum = 0;
        for (Crossroad c : list) sum += c.getStats().getDangerLevel();
        return sum / list.size();
    }

    // =====================================================================
    //  LOWER BOUNDED WILDCARD  List<? super ...>
    // =====================================================================

    /**
     * Заповнює список цілими числами від from до to.
     * Приймає List&lt;Integer&gt;, List&lt;Number&gt;, List&lt;Object&gt;.
     *
     * @param list список (супертип Integer)
     * @param from початкове значення
     * @param to   кінцеве значення
     */
    public static void fillWithIntegers(List<? super Integer> list, int from, int to) {
        for (int i = from; i <= to; i++) {
            list.add(i);
        }
    }

    /**
     * Копіює всі перехрестя src до dst.
     * dst може бути List&lt;Crossroad&gt; або List&lt;Object&gt; тощо.
     *
     * @param src вихідний список (підтип Crossroad)
     * @param dst цільовий список (супертип Crossroad)
     */
    public static void copyCrossroads(List<? extends Crossroad> src,
                                      List<? super Crossroad> dst) {
        for (Crossroad c : src) {
            dst.add(c);
        }
    }

    // =====================================================================
    //  ПРИКЛАДИ ПІДСТАНОВОЧНИХ ТИПІВ У БІБЛІОТЕЧНИХ КЛАСАХ JAVA
    //  Завдання 5
    // =====================================================================

    /**
     * Демонструє використання wildcard-типів у стандартній бібліотеці Java.
     */
    public static void demonstrateStdlibWildcards() {
        System.out.println("\n======================================================");
        System.out.println("ЗАВДАННЯ 5: Підстановочні типи у бібліотечних класах");
        System.out.println("======================================================\n");

        // --- 1. Collections.sort(List<T>) використовує Comparable<? super T> ---
        System.out.println("1. Collections.sort(List<T extends Comparable<? super T>>)");
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9, 3));
        System.out.println("   До сортування: " + numbers);
        Collections.sort(numbers);
        System.out.println("   Після сортування: " + numbers);

        // --- 2. Collections.max(Collection<? extends T>) ---
        System.out.println("\n2. Collections.max(Collection<? extends T extends Object & Comparable<? super T>>)");
        List<Double> doubles = Arrays.asList(3.14, 2.71, 1.41, 1.73);
        System.out.println("   Список: " + doubles);
        System.out.println("   Максимум: " + Collections.max(doubles));
        System.out.println("   Мінімум: " + Collections.min(doubles));

        // --- 3. Collections.copy(List<? super T> dest, List<? extends T> src) ---
        System.out.println("\n3. Collections.copy(List<? super T> dest, List<? extends T> src)");
        List<Number> dest = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
        List<Integer> src  = Arrays.asList(10, 20, 30);
        System.out.println("   src  (List<Integer>): " + src);
        System.out.println("   dest до копіювання (List<Number>): " + dest);
        Collections.copy(dest, src);  // List<Integer> → List<Number>: OK завдяки wildcards
        System.out.println("   dest після копіювання: " + dest);

        // --- 4. Collections.fill(List<? super T>, T obj) ---
        System.out.println("\n4. Collections.fill(List<? super T>, T obj)");
        List<Object> objects = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        System.out.println("   До fill: " + objects);
        Collections.fill(objects, "X");
        System.out.println("   Після fill(\"X\"): " + objects);

        // --- 5. Collections.unmodifiableList повертає List<T> від List<? extends T> ---
        System.out.println("\n5. Collections.unmodifiableList(List<? extends T>)");
        List<String> mutable = new ArrayList<>(Arrays.asList("Kyiv", "Lviv", "Odessa"));
        List<String> immutable = Collections.unmodifiableList(mutable);
        System.out.println("   Незмінний список: " + immutable);
        System.out.println("   Спроба модифікації — кине UnsupportedOperationException");

        // --- 6. Arrays.asList повертає List<T> ---
        System.out.println("\n6. Arrays.asList(T... a) — повертає List<T> фіксованого розміру");
        String[] arr = {"Settlement", "Crossroad", "UnregulatedCrossroad"};
        List<String> fromArray = Arrays.asList(arr);
        System.out.println("   Список з масиву: " + fromArray);

        // --- 7. Iterator<? extends T> (через entrySet wildcard) ---
        System.out.println("\n7. Map<K,V>.entrySet() → Set<Map.Entry<K,V>> (параметризовані типи)");
        Map<String, Integer> populationMap = new LinkedHashMap<>();
        populationMap.put("Київ", 2_967_000);
        populationMap.put("Харків", 1_430_000);
        populationMap.put("Одеса", 1_011_000);
        for (Map.Entry<String, Integer> entry : populationMap.entrySet()) {
            System.out.printf("   %-10s → %,d осіб%n", entry.getKey(), entry.getValue());
        }
    }
}