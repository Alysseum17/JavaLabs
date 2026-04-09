package lab5;

public enum SettlementType {
    VILLAGE("Село"),
    TOWN("Місто"),
    CITY("Велике місто"),
    CAPITAL("Столиця");

    private final String description;

    SettlementType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static SettlementType fromString(String s) {
        for (SettlementType t : values()) {
            if (t.description.equalsIgnoreCase(s) || t.name().equalsIgnoreCase(s)) return t;
        }
        return VILLAGE;
    }

    @Override
    public String toString() {
        return description;
    }
}