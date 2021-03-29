package mccreery.betterhud.api.property;

public final class SettingValueException extends Exception {
    private static final long serialVersionUID = 1L;

    public SettingValueException() {
        super();
    }

    public SettingValueException(String message) {
        super(message);
    }

    public SettingValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public SettingValueException(Throwable cause) {
        super(cause);
    }
}
