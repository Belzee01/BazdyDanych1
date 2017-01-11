package sample;

/**
 * Created by DW on 2017-01-11.
 */
public enum ACCOUNT_TYPE {
    STANDARD("Standard"),
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
