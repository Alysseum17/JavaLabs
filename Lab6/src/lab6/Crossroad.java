package lab6;

public class Crossroad {

    private String crossroadName;
    private String[] streets;
    private int numberOfStreets;
    private double latitude;
    private double longitude;
    private boolean regulated;
    private final int id;

    private static int nextId = 1;
    private static int totalCrossroads = 0;

    public static class TrafficStats {
        private int vehiclesPerHour;
        private int accidentsPerYear;
        private int pedestriansPerHour;

        public TrafficStats(int vehiclesPerHour, int accidentsPerYear, int pedestriansPerHour) {
            this.vehiclesPerHour = vehiclesPerHour;
            this.accidentsPerYear = accidentsPerYear;
            this.pedestriansPerHour = pedestriansPerHour;
        }

        public TrafficStats() { this(0, 0, 0); }

        public int getVehiclesPerHour() { return vehiclesPerHour; }
        public void setVehiclesPerHour(int v) { this.vehiclesPerHour = v; }
        public int getAccidentsPerYear() { return accidentsPerYear; }
        public void setAccidentsPerYear(int a) { this.accidentsPerYear = a; }
        public int getPedestriansPerHour() { return pedestriansPerHour; }
        public void setPedestriansPerHour(int p) { this.pedestriansPerHour = p; }

        public double getDangerIndex() {
            if (vehiclesPerHour == 0) return 0;
            return (double) accidentsPerYear / (vehiclesPerHour * 365.0) * 100000;
        }

        @Override
        public String toString() {
            return String.format("Авто/год: %d, Пішоходи/год: %d, ДТП/рік: %d (небезпека: %.2f)",
                    vehiclesPerHour, pedestriansPerHour, accidentsPerYear, getDangerIndex());
        }
    }

    private TrafficStats stats;

    public Crossroad() {
        this.crossroadName = "Невідомо";
        this.streets = new String[0];
        this.numberOfStreets = 0;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.regulated = false;
        this.stats = new TrafficStats();
        this.id = nextId++;
        totalCrossroads++;
    }

    public Crossroad(String crossroadName, String[] streets, double latitude,
                     double longitude, boolean regulated) {
        this.crossroadName = crossroadName;
        this.streets = streets != null ? streets : new String[0];
        this.numberOfStreets = this.streets.length;
        this.latitude = latitude;
        this.longitude = longitude;
        this.regulated = regulated;
        this.stats = new TrafficStats();
        this.id = nextId++;
        totalCrossroads++;
    }

    public Crossroad(String name, String[] streets, double lat, double lon,
                     boolean regulated, int vehiclesPerHour, int accidents, int pedestrians) {
        this(name, streets, lat, lon, regulated);
        this.stats = new TrafficStats(vehiclesPerHour, accidents, pedestrians);
    }

    public String getCrossroadName() { return crossroadName; }
    public void setCrossroadName(String name) { this.crossroadName = name; }

    public String[] getStreets() { return streets; }
    public void setStreets(String[] streets) {
        this.streets = streets;
        this.numberOfStreets = streets.length;
    }

    public int getNumberOfStreets() { return numberOfStreets; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public boolean isRegulated() { return regulated; }
    public void setRegulated(boolean regulated) { this.regulated = regulated; }

    public int getId() { return id; }
    public TrafficStats getStats() { return stats; }
    public void setStats(TrafficStats stats) { this.stats = stats; }

    public static int getTotalCrossroads() { return totalCrossroads; }

    public boolean containsStreet(String streetName) {
        for (String s : streets) {
            if (s != null && s.equalsIgnoreCase(streetName)) return true;
        }
        return false;
    }

    public double getDangerIndex() { return stats.getDangerIndex(); }

    public static Crossroad findMostDangerous(Crossroad[] arr) {
        if (arr == null || arr.length == 0) return null;
        Crossroad most = arr[0];
        for (Crossroad c : arr) {
            if (c != null && c.getDangerIndex() > most.getDangerIndex()) most = c;
        }
        return most;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s)", id, crossroadName, regulated ? "регульоване" : "нерегульоване");
    }

    public String getFullInfo() {
        return String.format(
                "ID: %d | Назва: %s | Вулиці: %s | Регульоване: %s\n" +
                        "Координати: %.4f°, %.4f° | Статистика: %s",
                id, crossroadName, String.join(", ", streets),
                regulated ? "Так" : "Ні",
                latitude, longitude, stats.toString());
    }
}