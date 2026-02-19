package lab1;

public class Settlement {

    private String name;
    private int population;
    private double area;
    private SettlementType type;
    private int foundedYear;
    private double latitude;
    private double longitude;
    private final int id;

    private static int nextId = 1;
    private static int totalSettlements = 0;
    private static final double EARTH_RADIUS_KM = 6371.0;


    public Settlement() {
        this.name = "Невідомо";
        this.population = 0;
        this.area = 0.0;
        this.type = SettlementType.VILLAGE;
        this.foundedYear = 2000;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.id = nextId++;
        totalSettlements++;
    }

    public Settlement(String name, int population, double area, String type, int foundedYear, double latitude, double longitude) {
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

    public Settlement(String name, int population) {
        this.name = name;
        this.population = population;
        this.area = 0.0;
        this.type = SettlementType.TOWN;
        this.foundedYear = 2000;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.id = nextId++;
        totalSettlements++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        if (population >= 0) this.population = population;
        else System.out.println("Помилка: населення не може бути від'ємним!");
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        if (area >= 0) this.area = area;
        else System.out.println("Помилка: площа не може бути від'ємною!");

    }

    public String getType() {
        return type.getDescription();
    }

    public void setType(String type) {
        this.type = SettlementType.fromString(type);
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(int foundedYear) {
        if (foundedYear > 0) {
             this.foundedYear = foundedYear;
        } else {
            System.out.println("Помилка: рік заснування має бути позитивним!");
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public double getPopulationDensity() {
        if (area > 0) {
            return population / area;
        }
        return 0;
    }

    public void increasePopulation(int amount) {
        if (amount > 0) {
            population += amount;
            System.out.println(name + ": населення збільшено на " + amount
                    + ". Нове населення: " + population);
        }
    }

    public void increasePopulation(double percent) {
        int increase = (int) (population * percent / 100.0);
        population += increase;
        System.out.println(name + ": населення збільшено на " + percent + "% (+"
                + increase + "). Нове населення: " + population);
    }

    public void decreasePopulation(int amount) {
        if (amount > 0 && population - amount >= 0) {
            population -= amount;
            System.out.println(name + ": населення зменшено на " + amount
                    + ". Нове населення: " + population);
        } else {
            System.out.println("Помилка: неможливо зменшити населення " + name);
        }
    }

    public int getAge(int currentYear) {
        return currentYear - foundedYear;
    }

    public void printInfo() {
        System.out.println("--- Населений пункт [ID=" + id + "] ---");
        System.out.println("Назва: " + name);
        System.out.println("Тип: " + type);
        System.out.println("Населення: " + population + " осіб");
        System.out.printf("Площа: %.2f км²%n", area);
        System.out.println("Рік заснування: " + foundedYear);
        System.out.printf("Координати: %.4f° пн.ш., %.4f° сх.д.%n", latitude, longitude);
        System.out.printf("Густота населення: %.2f осіб/км²%n", getPopulationDensity());
        System.out.println();
    }

    public static int getTotalSettlements() {
        return totalSettlements;
    }

    public static Settlement getLargerByPopulation(Settlement a, Settlement b) {
        if (a.getPopulation() > b.getPopulation()) {
            return a;
        } else if (b.getPopulation() > a.getPopulation()) {
            return b;
        }
        return null;
    }

    public static double calculateDistance(Settlement a, Settlement b) {
        double dLat = Math.toRadians(b.getLatitude() - a.getLatitude());
        double dLon = Math.toRadians(b.getLongitude() - a.getLongitude());
        double x = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(a.getLatitude()))
                * Math.cos(Math.toRadians(b.getLatitude()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
        return EARTH_RADIUS_KM * c;
    }
    // Note: This doesn't have my formula, I just copy it from https://en.wikipedia.org/wiki/Haversine_formula
}
