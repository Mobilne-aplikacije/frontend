package project.roomeo.models.enums;

public enum BookingMethod {
    AUTOMATIC("Automatic"), NON_AUTOMATIC("Non-automatic");
    private final String displayName;
    BookingMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
