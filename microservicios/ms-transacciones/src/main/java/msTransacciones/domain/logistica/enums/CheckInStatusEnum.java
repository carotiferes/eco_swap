package msTransacciones.domain.logistica.enums;

public enum CheckInStatusEnum {
    CREATED("created"),
    RECEIVED("received"),
    PROCESSING("processing"),
    COMPLETED("completed");

    public final String name;

    private CheckInStatusEnum(String name) {
        this.name = name;
    }
}
