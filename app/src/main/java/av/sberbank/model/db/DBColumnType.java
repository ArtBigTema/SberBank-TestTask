package av.sberbank.model.db;

/**
 * Created by Artem on 12.04.2017.
 */

public enum DBColumnType {
    INTEGER("INTEGER"),
    TEXT("TEXT");

    private String value;

    DBColumnType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}