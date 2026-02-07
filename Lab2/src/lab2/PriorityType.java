package lab2;

public enum PriorityType {
    EQUAL_PRIORITY("рівнозначні"),
    MAIN_SECONDARY("головна/другорядна"),
    ROUNDABOUT("кільце");

    private final String description;

    PriorityType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PriorityType fromString(String text) {
        if (text == null || text.isBlank()) return EQUAL_PRIORITY;

        String input = text.trim().toLowerCase();

        for (PriorityType type : PriorityType.values()) {
            if (type.description.toLowerCase().equals(input)) {
                return type;
            }
        }

        String technicalName = input.toUpperCase().replace("/", "_");
        try {
            return PriorityType.valueOf(technicalName);
        } catch (IllegalArgumentException e) {
            return EQUAL_PRIORITY;
        }
    }
}
