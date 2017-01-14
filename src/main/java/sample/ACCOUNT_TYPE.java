package sample;

/**
 * Describes account type that is used during application usage and data that is inserted to database
 */
public enum ACCOUNT_TYPE {
    STANDARD("Standard"),
    ADMIN("Admin"),
    COMPANY("Company");

    private final String type;

    ACCOUNT_TYPE(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
