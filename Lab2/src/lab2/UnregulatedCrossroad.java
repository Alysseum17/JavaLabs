package lab2;

public class UnregulatedCrossroad extends Crossroad {

    private PriorityType priorityType;
    private String mainStreet;
    private boolean hasSpeedBump;
    private int speedLimit;


    public UnregulatedCrossroad() {
        super();
        super.setRegulated(false);
        this.priorityType = PriorityType.EQUAL_PRIORITY;
        this.mainStreet = "";
        this.hasSpeedBump = false;
        this.speedLimit = 50;
    }

    public UnregulatedCrossroad(String crossroadName, String[] streets,
                                double latitude, double longitude,
                                String priorityType, String mainStreet,
                                boolean hasSpeedBump, int speedLimit) {
        super(crossroadName, streets, latitude, longitude, false);
        this.priorityType = PriorityType.fromString(priorityType);
        this.mainStreet = mainStreet;
        this.hasSpeedBump = hasSpeedBump;
        this.speedLimit = speedLimit;
    }

    public UnregulatedCrossroad(String street1, String street2, String priorityType) {
        super(street1, street2);
        super.setRegulated(false);
        this.priorityType = PriorityType.fromString(priorityType);
        this.mainStreet = "";
        this.hasSpeedBump = false;
        this.speedLimit = 50;
    }


    public String getPriorityType() {
        return priorityType.getDescription();
    }
    public void setPriorityType(String priorityType) {
        if (priorityType.equals("головна/другорядна") && mainStreet.isEmpty()) {
            System.out.println("Увага: для типу 'головна/другорядна' потрібно вказати головну вулицю");
        } else {
            this.priorityType = PriorityType.fromString(priorityType);
        }
    }

    public String getMainStreet() {
        return mainStreet;
    }
    public void setMainStreet(String mainStreet) {
        this.mainStreet = mainStreet;
    }

    public boolean isHasSpeedBump() {
        return hasSpeedBump;
    }
    public void setHasSpeedBump(boolean hasSpeedBump) {
        this.hasSpeedBump = hasSpeedBump;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }
    public void setSpeedLimit(int speedLimit) {
        if (speedLimit > 0 && speedLimit <= 60) {
            this.speedLimit = speedLimit;
        } else {
            System.out.println("Помилка: обмеження швидкості має бути 1..60 км/год");
        }
    }

    @Override
    public String getSafetyLevel() {
        if (hasSpeedBump && speedLimit <= 30) {
            return "Нерегульоване — високий рівень безпеки (лежачий поліцейський + обмеження)";
        } else if (priorityType.getDescription().equals("кільце")) {
            return "Нерегульоване (кільце) — середній рівень безпеки";
        } else if (priorityType.getDescription().equals("рівнозначні")) {
            return "Нерегульоване (рівнозначні дороги) — ПІДВИЩЕНА НЕБЕЗПЕКА";
        } else {
            return "Нерегульоване (головна/другорядна) — помірний рівень безпеки";
        }
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("  [Додатково — нерегульоване]");
        System.out.println("  Тип пріоритету: " + priorityType);
        if (!mainStreet.isEmpty()) {
            System.out.println("  Головна вулиця: " + mainStreet);
        }
        System.out.println("  Лежачий поліцейський: " + (hasSpeedBump ? "так" : "ні"));
        System.out.println("  Обмеження швидкості: " + speedLimit + " км/год");
    }

    public boolean needsTrafficLight() {
        if (getStats() == null) return false;
        return getStats().getVehiclesPerHour() > 500
                || getStats().getAccidentsPerYear() > 5;
    }

    public void setMainRoad(String mainStreet) {
        this.priorityType = PriorityType.MAIN_SECONDARY;
        this.mainStreet = mainStreet;
        System.out.println("Перехрестя " + getCrossroadName()
                + ": встановлено головну дорогу — " + mainStreet);
    }
}