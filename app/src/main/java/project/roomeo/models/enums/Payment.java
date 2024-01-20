package project.roomeo.models.enums;

public enum Payment {
    PerPerson("Price per person"), PerAccommodation("Price per room");

    private final String displayName;
    Payment(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
