package lab5;

import java.util.Arrays;

public class Settlement implements Comparable<Settlement> {

    private String name;
    private int population;
    private double area;
    private SettlementType type;
    private int foundedYear;
    private double latitude;
    private double longitude;
    private final int id;

    private final Crossroad[] crossroads;
    private int crossroadCount;
    private static final int MAX_CROSSROADS = 100;

    private static int nextId = 1;
    private static int totalSettlements = 0;


    public Settlement() {
        this.name = "Невідомо";
        this.population = 0;
        this.area = 0.0;
        this.type = SettlementType.VILLAGE;
        this.foundedYear = 2000;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.crossroads = new Crossroad[MAX_CROSSROADS];
        this.crossroadCount = 0;
        this.id = nextId++;
        totalSettlements++;
    }

    public Settlement(String name, int population, double area, String type,
                      int foundedYear, double latitude, double longitude) {
        this.name = name;
        this.population = population;
        this.area = area;
        this.type = SettlementType.fromString(type);
        this.foundedYear = foundedYear;
        this.latitude = latitude;
        this.longitude = longitude;
        this.crossroads = new Crossroad[MAX_CROSSROADS];
        this.crossroadCount = 0;
        this.id = nextId++;
        totalSettlements++;
    }

    public Settlement(String name, int population) {
        this(name, population, 0.0, "Місто", 2000, 0.0, 0.0);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPopulation() { return population; }
    public void setPopulation(int population) {
        if (population >= 0) this.population = population;
        else throw new IllegalArgumentException("Населення не може бути від'ємним!");
    }

    public double getArea() { return area; }
    public void setArea(double area) {
        if (area >= 0) this.area = area;
        else throw new IllegalArgumentException("Площа не може бути від'ємною!");
    }

    public SettlementType getSettlementType() { return type; }
    public String getType() { return type.getDescription(); }
    public void setType(String type) { this.type = SettlementType.fromString(type); }
    public void setType(SettlementType type) { this.type = type; }

    public int getFoundedYear() { return foundedYear; }
    public void setFoundedYear(int year) {
        if (year > 0) this.foundedYear = year;
        else throw new IllegalArgumentException("Рік заснування має бути позитивним!");
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getId() { return id; }

    public static int getTotalSettlements() { return totalSettlements; }

    public void addCrossroad(Crossroad c) {
        if (crossroadCount < MAX_CROSSROADS) crossroads[crossroadCount++] = c;
        else throw new IllegalStateException("Досягнуто максимум перехресть!");
    }

    public Crossroad getCrossroad(int index) {
        if (index >= 0 && index < crossroadCount) return crossroads[index];
        return null;
    }

    public Crossroad[] getCrossroads() {
        return Arrays.copyOf(crossroads, crossroadCount);
    }

    public int getCrossroadCount() { return crossroadCount; }

    public Crossroad findMostDangerousCrossroad() {
        return Crossroad.findMostDangerous(Arrays.copyOf(crossroads, crossroadCount));
    }


    public double getPopulationDensity() {
        return (area > 0) ? population / area : 0;
    }

    public int getAge(int currentYear) {
        return currentYear - foundedYear;
    }

    public void increasePopulation(int amount) {
        if (amount > 0) population += amount;
    }

    public void increasePopulation(double percent) {
        population += (int)(population * percent / 100.0);
    }

    public void decreasePopulation(int amount) {
        if (amount > 0 && population - amount >= 0) population -= amount;
        else throw new IllegalArgumentException("Неможливо зменшити населення: некоректне значення!");
    }

    @Override
    public int compareTo(Settlement other) {
        return Integer.compare(this.population, other.population);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s, нас.: %,d)", id, name, type, population);
    }

    public String getFullInfo() {
        return String.format(
                "ID: %d | Назва: %s | Тип: %s\n" +
                        "Населення: %,d | Площа: %.2f км² | Густота: %.2f ос/км²\n" +
                        "Рік заснування: %d | Координати: %.4f°, %.4f°\n" +
                        "Кількість перехресть: %d",
                id, name, type, population, area,
                getPopulationDensity(), foundedYear, latitude, longitude, crossroadCount);
    }
}