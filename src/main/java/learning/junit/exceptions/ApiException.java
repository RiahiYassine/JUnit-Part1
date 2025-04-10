package learning.junit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * Base exception for all API errors.
 * This abstract class serves as the foundation for the application's exception hierarchy,
 * providing common error metadata needed for consistent API error responses.
 * <p>
 * All API-specific exceptions should extend this class to ensure they can be
 * properly handled by the {@link GlobalExceptionHandler}.
 * <p>
 * The class follows the RFC 7807 Problem Details standard for HTTP APIs.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7807">RFC 7807</a>
 */
@Getter
public abstract class ApiException extends RuntimeException {

    /**
     * The HTTP status code that should be used in the response.
     */
    private final HttpStatus status;

    /**
     * A short, human-readable title for the error type.
     */
    private final String title;

    /**
     * A URI reference that identifies the error type.
     */
    private final String type;

    /**
     * The timestamp when the exception was created.
     */
    private final Instant timestamp;

    /**
     * Creates a new API exception with the specified details.
     *
     * @param message The detailed error message.
     * @param status The HTTP status code to return.
     * @param title A short, human-readable title for the error.
     * @param type A URI reference that identifies the error type.
     */
    protected ApiException(String message, HttpStatus status, String title, String type) {
        super(message);
        this.status = status;
        this.title = title;
        this.type = type;
        this.timestamp = Instant.now();
    }
}