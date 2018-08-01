package org.mule.modules.logger.exception;

public class DynamicLoggerException extends Exception {
    public DynamicLoggerException() {
        super();
    }

    public DynamicLoggerException(String message) {
        super(message);
    }

    public DynamicLoggerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamicLoggerException(Throwable cause) {
        super(cause);
    }

    protected DynamicLoggerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
