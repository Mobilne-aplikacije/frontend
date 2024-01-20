package project.roomeo.models.enums;

public enum AccommodationType {
    STUDIO("STUDIO"), ROOM("ROOM");

    private final String displayName;
    AccommodationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
