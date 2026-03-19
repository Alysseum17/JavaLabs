package lab4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class GenericSettlement<T extends Crossroad> {

    private final String name;
    private final int population;
    private final double area;
    private final SettlementType type;
    private final int foundedYear;
    private final double latitude;
    private final double longitude;
    private final int id;

    private final List<T> crossroads = new ArrayList<>();

    private static int nextId = 1;
    private static int totalSettlements = 0;

    public GenericSettlement(String name, int population, double area,
                             String type, int foundedYear, double latitude, double longitude) {
        this.name = name;
        this.population = population;
        this.area = area;
        this.type = SettlementType.fromString(type);
        this.foundedYear = foundedYear;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = nextId++;
        totalSettlements++;
    }

    public GenericSettlement(String name, int population) {
        this(name, population, 0, "місто", 2000, 0, 0);
    }

    public void addCrossroad(T c) {
        crossroads.add(c);
    }

    public T getCrossroad(int index) {
        return crossroads.get(index);
    }

    public List<T> getCrossroads() {
        return Collections.unmodifiableList(crossroads);
    }

    public List<T> findBy(Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T c : crossroads)
            if (predicate.test(c)) result.add(c);
        return result;
    }

    public List<T> findByStreet(String streetName) {
        return findBy(c -> c.containsStreet(streetName));
    }

    public T findMostDangerous() {
        if (crossroads.isEmpty()) return null;
        T worst = crossroads.get(0);
        for (T c : crossroads)
            if (c.getStats().getDangerLevel() > worst.getStats().getDangerLevel())
                worst = c;
        return worst;
    }

    public T findMax(Comparator<T> comparator) {
        if (crossroads.isEmpty()) return null;
        T max = crossroads.get(0);
        for (T c : crossroads)
            if (comparator.compare(c, max) > 0) max = c;
        return max;
    }

    public String getName()        { return name; }
    public int getPopulation()     { return population; }
    public double getArea()        { return area; }
    public int getFoundedYear()    { return foundedYear; }
    public double getLatitude()    { return latitude; }
    public double getLongitude()   { return longitude; }
    public int getId()             { return id; }
    public int getCrossroadCount() { return crossroads.size(); }

    public double getPopulationDensity() { return area > 0 ? population / area : 0; }

    public void printInfo() {
        System.out.println("=== GenericSettlement<T> [ID=" + id + "]: " + name + " ===");
        System.out.println("Тип: " + type + ", населення: " + String.format("%,d", population));
        System.out.printf("Площа: %.2f км², густота: %.2f осіб/км²%n", area, getPopulationDensity());
        System.out.println("Рік заснування: " + foundedYear);
        System.out.println("Перехресть: " + crossroads.size());
    }

    public static int getTotalSettlements() { return totalSettlements; }
}