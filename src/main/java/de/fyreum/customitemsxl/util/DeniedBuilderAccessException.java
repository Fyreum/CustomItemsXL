package de.fyreum.customitemsxl.util;

public class DeniedBuilderAccessException extends RuntimeException {

    private static final long serialVersionUID = -6950834413981695554L;

    public DeniedBuilderAccessException() {
        super();
    }

    public DeniedBuilderAccessException(String message) {
        super(message);
    }

    public DeniedBuilderAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeniedBuilderAccessException(Throwable cause) {
        super(cause);
    }

    protected DeniedBuilderAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
