package lab3;

import lab3.Settlement;
import lab3.Crossroad;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class SettlementCollection {

    private final Collection<Settlement> settlements;

    public SettlementCollection() {
        this.settlements = new ArrayList<>();
    }

    public SettlementCollection(Collection<Settlement> settlements) {
        this.settlements = new ArrayList<>(settlements);
    }

    public void add(Settlement s) {
        settlements.add(s);
    }

    public boolean remove(Settlement s) {
        return settlements.remove(s);
    }

    public int size() {
        return settlements.size();
    }

    public Collection<Settlement> getAll() {
        return Collections.unmodifiableCollection(settlements);
    }

    public Settlement find(Predicate<Settlement> condition) {
        for (Settlement s : settlements) {
            if (condition.test(s)) {
                return s;
            }
        }
        return null;
    }

    public List<Settlement> findAll(Predicate<Settlement> condition) {
        List<Settlement> result = new ArrayList<>();
        for (Settlement s : settlements) {
            if (condition.test(s)) {
                result.add(s);
            }
        }
        return result;
    }

    public Set<Settlement> getUnique() {
        Set<Settlement> unique = new TreeSet<>(
                Comparator.comparing(Settlement::getName)
        );
        unique.addAll(settlements);
        return unique;
    }

    public List<Settlement> compareBy(Comparator<Settlement> comparator) {
        List<Settlement> sorted = new ArrayList<>(settlements);
        sorted.sort(comparator);
        return sorted;
    }

    public List<Settlement> filter(Predicate<Settlement> condition) {
        return settlements.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public double averageInt(ToIntFunction<Settlement> extractor) {
        if (settlements.isEmpty()) return 0;
        int sum = 0;
        for (Settlement s : settlements) {
            sum += extractor.applyAsInt(s);
        }
        return (double) sum / settlements.size();
    }

    public double averageDouble(ToDoubleFunction<Settlement> extractor) {
        if (settlements.isEmpty()) return 0;
        return settlements.stream()
                .mapToDouble(extractor)
                .average()
                .orElse(0);
    }



    public List<Settlement> sortByPopulationAnonymous() {
        List<Settlement> sorted = new ArrayList<>(settlements);
        sorted.sort(new Comparator<Settlement>() {
            @Override
            public int compare(Settlement a, Settlement b) {
                return Integer.compare(a.getPopulation(), b.getPopulation());
            }
        });
        return sorted;
    }

    public List<Settlement> sortByPopulationLambda() {
        List<Settlement> sorted = new ArrayList<>(settlements);
        sorted.sort((a, b) -> Integer.compare(a.getPopulation(), b.getPopulation()));
        return sorted;
    }

    public List<Settlement> sortByPopulationMethodRef() {
        List<Settlement> sorted = new ArrayList<>(settlements);
        sorted.sort(Comparator.comparingInt(Settlement::getPopulation));
        return sorted;
    }

    public List<Settlement> sortByNameMethodRef() {
        List<Settlement> sorted = new ArrayList<>(settlements);
        sorted.sort(Comparator.comparing(Settlement::getName));
        return sorted;
    }


    public void printAll(String header) {
        System.out.println("\n--- " + header + " (" + settlements.size() + " шт.) ---");
        for (Settlement s : settlements) {
            System.out.printf("  %-20s | насел.: %,10d | площа: %8.2f км² | перехресть: %d | засн.: %d%n",
                    s.getName(), s.getPopulation(), s.getArea(),
                    s.getCrossroadCount(), s.getFoundedYear());
        }
    }

    public static void printList(List<Settlement> list, String header) {
        System.out.println("\n--- " + header + " (" + list.size() + " шт.) ---");
        for (Settlement s : list) {
            System.out.printf("  %-20s | насел.: %,10d | площа: %8.2f км² | перехресть: %d%n",
                    s.getName(), s.getPopulation(), s.getArea(), s.getCrossroadCount());
        }
    }
}