package de.fyreum.customitemsxl.serialization;

public class ConfigSerializationError extends RuntimeException {

    private static final long serialVersionUID = -7690046356726382292L;

    public ConfigSerializationError() {
        super();
    }

    public ConfigSerializationError(String message) {
        super(message);
    }

    public ConfigSerializationError(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigSerializationError(Throwable cause) {
        super(cause);
    }

    protected ConfigSerializationError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
