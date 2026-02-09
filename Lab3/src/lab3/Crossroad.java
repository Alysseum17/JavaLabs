package lab3;

public class Crossroad {

    private String crossroadName;
    private String[] streets;
    private int numberOfStreets;
    private double latitude;
    private double longitude;
    private boolean regulated;
    private int id;

    private static int nextId = 1;
    private static int totalCrossroads = 0;

    public static class TrafficStats {
        private int vehiclesPerHour;
        private int accidentsPerYear;
        private int pedestriansPerHour;

        private static final double DAYS_IN_YEAR = 365.0;
        private static final int ACTIVE_HOURS_PER_DAY = 12;

        private static final int STATISTICAL_SCALE_FACTOR = 100000;

        public TrafficStats(int vehiclesPerHour, int accidentsPerYear, int pedestriansPerHour) {
            this.vehiclesPerHour = vehiclesPerHour;
            this.accidentsPerYear = accidentsPerYear;
            this.pedestriansPerHour = pedestriansPerHour;
        }

        public TrafficStats() {
            this(0, 0, 0);
        }

        public int getVehiclesPerHour() {
            return vehiclesPerHour;
        }
        public void setVehiclesPerHour(int v) {
            this.vehiclesPerHour = v;
        }

        public int getAccidentsPerYear() {
            return accidentsPerYear;
        }
        public void setAccidentsPerYear(int a) {
            this.accidentsPerYear = a;
        }

        public int getPedestriansPerHour() {
            return pedestriansPerHour;
        }
        public void setPedestriansPerHour(int p) {
            this.pedestriansPerHour = p;
        }

        public double getDangerLevel() {
            double totalPerYear = (vehiclesPerHour + pedestriansPerHour) * DAYS_IN_YEAR * ACTIVE_HOURS_PER_DAY;
            if (totalPerYear == 0) return 0;
            return accidentsPerYear / totalPerYear * STATISTICAL_SCALE_FACTOR;
        }

        public void printStats() {
            System.out.println("  [Статистика руху]");
            System.out.println("  Авто/год: " + vehiclesPerHour);
            System.out.println("  Пішоходів/год: " + pedestriansPerHour);
            System.out.println("  ДТП/рік: " + accidentsPerYear);
            System.out.printf("  Рівень небезпеки: %.4f%n", getDangerLevel());
        }
    }

    private TrafficStats stats;

    public Crossroad() {
        this.crossroadName = "Невідоме перехрестя";
        this.streets = new String[]{"вул. A", "вул. B"};
        this.numberOfStreets = 2;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.regulated = true;
        this.stats = new TrafficStats();
        this.id = nextId++;
        totalCrossroads++;
    }

    public Crossroad(String crossroadName, String[] streets,
                     double latitude, double longitude, boolean regulated) {
        this.crossroadName = crossroadName;
        this.streets = streets;
        this.numberOfStreets = streets.length;
        this.latitude = latitude;
        this.longitude = longitude;
        this.regulated = regulated;
        this.stats = new TrafficStats();
        this.id = nextId++;
        totalCrossroads++;
    }

    public Crossroad(String street1, String street2) {
        this.crossroadName = street1 + " / " + street2;
        this.streets = new String[]{street1, street2};
        this.numberOfStreets = 2;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.regulated = true;
        this.stats = new TrafficStats();
        this.id = nextId++;
        totalCrossroads++;
    }


    public String getCrossroadName() {
        return crossroadName;
    }
    public void setCrossroadName(String name) {
        this.crossroadName = name;
    }

    public String[] getStreets() {
        return streets;
    }
    public void setStreets(String[] streets) {
        this.streets = streets;
        this.numberOfStreets = streets.length;
    }

    public int getNumberOfStreets() {
        return numberOfStreets;
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

    public boolean isRegulated() {
        return regulated;
    }

    public void setRegulated(boolean regulated) {
        this.regulated = regulated;
    }

    public TrafficStats getStats() {
        return stats;
    }

    public void setStats(TrafficStats stats) {
        this.stats = stats;
    }

    public int getId() {
        return id;
    }

    public boolean containsStreet(String streetName) {
        for (String s : streets) {
            if (s.equalsIgnoreCase(streetName)) {
                return true;
            }
        }
        return false;
    }

    public void addStreet(String streetName) {
        String[] newStreets = new String[numberOfStreets + 1];
        System.arraycopy(streets, 0, newStreets, 0, numberOfStreets);
        newStreets[numberOfStreets] = streetName;
        streets = newStreets;
        numberOfStreets++;
    }

    public String getSafetyLevel() {
        if (regulated) {
            return "Регульоване (світлофор) — середній рівень безпеки";
        }
        return "Нерегульоване — потребує оцінки";
    }

    public void printInfo() {

        class InfoFormatter {
            private final String separator;

            InfoFormatter(String separator) {
                this.separator = separator;
            }

            String formatStreets() {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < streets.length; i++) {
                    sb.append(streets[i]);
                    if (i < streets.length - 1) sb.append(separator);
                }
                return sb.toString();
            }

            String formatLocation() {
                return String.format("%.4f° пн.ш., %.4f° сх.д.", latitude, longitude);
            }
        }

        InfoFormatter fmt = new InfoFormatter(" ∩ ");

        System.out.println("  === Перехрестя [ID=" + id + "] ===");
        System.out.println("  Назва: " + crossroadName);
        System.out.println("  Вулиці: " + fmt.formatStreets());
        System.out.println("  Кількість вулиць: " + numberOfStreets);
        System.out.println("  Координати: " + fmt.formatLocation());
        System.out.println("  Регулювання: " + (regulated ? "так (світлофор)" : "ні"));
        System.out.println("  Безпека: " + getSafetyLevel());
        if (stats != null) {
            stats.printStats();
        }
    }

    public static int getTotalCrossroads() {
        return totalCrossroads;
    }

    public static Crossroad findMostDangerous(Crossroad[] crossroads) {
        if (crossroads == null || crossroads.length == 0) return null;
        Crossroad worst = crossroads[0];
        for (int i = 1; i < crossroads.length; i++) {
            if (crossroads[i] != null
                    && crossroads[i].getStats().getDangerLevel()
                    > worst.getStats().getDangerLevel()) {
                worst = crossroads[i];
            }
        }
        return worst;
    }
}