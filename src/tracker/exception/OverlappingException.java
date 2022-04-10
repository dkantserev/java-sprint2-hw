package tracker.exception;

import java.time.DateTimeException;

public class OverlappingException extends DateTimeException {
    public OverlappingException(String message) {
        super(message);
    }
}
