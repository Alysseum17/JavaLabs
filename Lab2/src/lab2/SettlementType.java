package lab2;

public enum SettlementType {
    VILLAGE("село"),
    TOWN("місто"),
    CITY("місто"),
    UNKNOWN("невідомо");

    private final String description;

    SettlementType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static SettlementType fromString(String text) {
        if (text == null || text.isBlank()) return UNKNOWN;

        String input = text.trim().toLowerCase();

        for (SettlementType type : SettlementType.values()) {
            if (type.description.toLowerCase().equals(input)) {
                return type;
            }
        }

        String technicalName = input.toUpperCase();
        try {
            return SettlementType.valueOf(technicalName);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
