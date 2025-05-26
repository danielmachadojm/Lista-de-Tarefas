package todoapp.enums;

public enum Priority {
    BAIXA(1),
    MÉDIA(2),
    ALTA(3);

    private final int value;

    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}