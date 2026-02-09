package lab3;

import lab3.Settlement;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;


public class SettlementMap {

    private final Map<String, Settlement> settlements;

    public SettlementMap() {
        this.settlements = new LinkedHashMap<>();
    }

    public SettlementMap(Collection<Settlement> collection) {
        this.settlements = new LinkedHashMap<>();
        for (Settlement s : collection) {
            settlements.put(s.getName(), s);
        }
    }

    public void put(Settlement s) {
        settlements.put(s.getName(), s);
    }

    public Settlement get(String name) {
        return settlements.get(name);
    }

    public int size() {
        return settlements.size();
    }

    public Map<String, Settlement> getAll() {
        return Collections.unmodifiableMap(settlements);
    }


    public Map<String, Settlement> filter(Predicate<Settlement> condition) {
        return settlements.entrySet().stream()
                .filter(e -> condition.test(e.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public int removeIfNot(Predicate<Settlement> keepCondition) {
        int before = settlements.size();
        settlements.entrySet().removeIf(e -> !keepCondition.test(e.getValue()));
        return before - settlements.size();
    }

    public int removeIf(Predicate<Settlement> condition) {
        int before = settlements.size();
        settlements.entrySet().removeIf(e -> condition.test(e.getValue()));
        return before - settlements.size();
    }

    public int sumInt(ToIntFunction<Settlement> extractor) {
        return settlements.values().stream()
                .mapToInt(extractor)
                .sum();
    }

    public double sumDouble(ToDoubleFunction<Settlement> extractor) {
        return settlements.values().stream()
                .mapToDouble(extractor)
                .sum();
    }

    public OptionalInt maxInt(ToIntFunction<Settlement> extractor) {
        return settlements.values().stream()
                .mapToInt(extractor)
                .max();
    }

    public OptionalInt minInt(ToIntFunction<Settlement> extractor) {
        return settlements.values().stream()
                .mapToInt(extractor)
                .min();
    }

    public OptionalDouble averageInt(ToIntFunction<Settlement> extractor) {
        return settlements.values().stream()
                .mapToInt(extractor)
                .average();
    }


    private Map<String, Settlement> sortedMap(Comparator<Settlement> comparator) {
        List<Settlement> sorted = new ArrayList<>(settlements.values());
        sorted.sort(comparator);
        Map<String, Settlement> result = new LinkedHashMap<>();
        for (Settlement s : sorted) {
            result.put(s.getName(), s);
        }
        return result;
    }

    public Map<String, Settlement> sortMultiAnonymous() {
        return sortedMap(new Comparator<Settlement>() {
            @Override
            public int compare(Settlement a, Settlement b) {
                int cmp = a.getType().compareTo(b.getType());
                if (cmp != 0) return cmp;
                return Integer.compare(b.getPopulation(), a.getPopulation());
            }
        });
    }

    public Map<String, Settlement> sortMultiLambda() {
        return sortedMap((a, b) -> {
            int cmp = a.getType().compareTo(b.getType());
            if (cmp != 0) return cmp;
            return Integer.compare(b.getPopulation(), a.getPopulation());
        });
    }

    public Map<String, Settlement> sortMultiMethodRef() {
        return sortedMap(
                Comparator.comparing(Settlement::getType)
                        .thenComparing(
                                Comparator.comparingInt(Settlement::getPopulation).reversed()
                        )
        );
    }


    public void printAll(String header) {
        System.out.println("\n--- " + header + " (" + settlements.size() + " шт.) ---");
        for (Map.Entry<String, Settlement> e : settlements.entrySet()) {
            Settlement s = e.getValue();
            System.out.printf("  [%-15s] насел.: %,10d | площа: %8.2f км² | тип: %-6s | перехр.: %d%n",
                    e.getKey(), s.getPopulation(), s.getArea(),
                    s.getType(), s.getCrossroadCount());
        }
    }

    public static void printMap(Map<String, Settlement> map, String header) {
        System.out.println("\n--- " + header + " (" + map.size() + " шт.) ---");
        for (Map.Entry<String, Settlement> e : map.entrySet()) {
            Settlement s = e.getValue();
            System.out.printf("  [%-15s] насел.: %,10d | площа: %8.2f км²%n",
                    e.getKey(), s.getPopulation(), s.getArea());
        }
    }

    public static void printList(List<Settlement> list, String header) {
        System.out.println("\n--- " + header + " (" + list.size() + " шт.) ---");
        for (Settlement s : list) {
            System.out.printf("  %-20s | тип: %-6s | насел.: %,10d | площа: %8.2f км²%n",
                    s.getName(), s.getType(), s.getPopulation(), s.getArea());
        }
    }
}