package lab4;

import java.util.*;

public class WildcardUtils {

    public static void printList(List<?> list) {
        System.out.println("Список [" + list.size() + " елементів]:");
        for (Object elem : list) {
            System.out.println("  - " + elem);
        }
    }

    public static int countNonNull(List<?> list) {
        int count = 0;
        for (Object elem : list) {
            if (elem != null) count++;
        }
        return count;
    }

    public static boolean contains(List<?> list, Object value) {
        for (Object elem : list) {
            if (Objects.equals(elem, value)) return true;
        }
        return false;
    }

    private static <T> void reverse(List<T> list) {
        int n = list.size();
        for (int i = 0; i < n / 2; i++) {
            T temp = list.get(i);
            list.set(i, list.get(n - 1 - i));
            list.set(n - 1 - i, temp);
        }
    }

    public static double sumNumbers(List<? extends Number> list) {
        double sum = 0;
        for (Number n : list) sum += n.doubleValue();
        return sum;
    }

    public static double average(List<? extends Number> list) {
        if (list.isEmpty()) return 0;
        return sumNumbers(list) / list.size();
    }

    public static <T extends Comparable<T>> T findMax(List<? extends T> list) {
        if (list.isEmpty()) throw new NoSuchElementException("List is empty");
        T max = list.getFirst();
        for (T item : list) {
            if (item.compareTo(max) > 0) max = item;
        }
        return max;
    }

    public static Crossroad findMostDangerous(List<? extends Crossroad> list) {
        if (list.isEmpty()) return null;
        Crossroad worst = list.getFirst();
        for (Crossroad c : list) {
            if (c.getStats().getDangerLevel() > worst.getStats().getDangerLevel()) {
                worst = c;
            }
        }
        return worst;
    }

    public static double avgDangerLevel(List<? extends Crossroad> list) {
        if (list.isEmpty()) return 0;
        double sum = 0;
        for (Crossroad c : list) sum += c.getStats().getDangerLevel();
        return sum / list.size();
    }


    public static void fillWithIntegers(List<? super Integer> list, int from, int to) {
        for (int i = from; i <= to; i++) {
            list.add(i);
        }
    }

    public static void copyCrossroads(List<? extends Crossroad> src,
                                      List<? super Crossroad> dst) {
        for (Crossroad c : src) {
            dst.add(c);
        }
    }

    public static void demonstrateStdlibWildcards() {

        System.out.println("1. Collections.sort(List<T extends Comparable<? super T>>)");
        List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9, 3));
        System.out.println("   До сортування: " + numbers);
        Collections.sort(numbers);
        System.out.println("   Після сортування: " + numbers);

        System.out.println("\n2. Collections.max(Collection<? extends T extends Object & Comparable<? super T>>)");
        List<Double> doubles = Arrays.asList(3.14, 2.71, 1.41, 1.73);
        System.out.println("   Список: " + doubles);
        System.out.println("   Максимум: " + Collections.max(doubles));
        System.out.println("   Мінімум: " + Collections.min(doubles));

        System.out.println("\n3. Collections.copy(List<? super T> dest, List<? extends T> src)");
        List<Number> dest = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
        List<Integer> src  = Arrays.asList(10, 20, 30);
        System.out.println("   src  (List<Integer>): " + src);
        System.out.println("   dest до копіювання (List<Number>): " + dest);
        Collections.copy(dest, src);
        System.out.println("   dest після копіювання: " + dest);

        System.out.println("\n4. Collections.fill(List<? super T>, T obj)");
        List<Object> objects = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        System.out.println("   До fill: " + objects);
        Collections.fill(objects, "X");
        System.out.println("   Після fill(\"X\"): " + objects);

        System.out.println("\n5. Collections.unmodifiableList(List<? extends T>)");
        List<String> mutable = new ArrayList<>(Arrays.asList("Kyiv", "Lviv", "Odessa"));
        List<String> immutable = Collections.unmodifiableList(mutable);
        System.out.println("   Незмінний список: " + immutable);
        System.out.println("   Спроба модифікації — кине UnsupportedOperationException");

        System.out.println("\n6. Arrays.asList(T... a) — повертає List<T> фіксованого розміру");
        String[] arr = {"Settlement", "Crossroad", "UnregulatedCrossroad"};
        List<String> fromArray = Arrays.asList(arr);
        System.out.println("   Список з масиву: " + fromArray);

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