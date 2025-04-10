package learning.junit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Global exception handler for the application.
 * <p>
 * This class intercepts exceptions thrown by the application and converts them
 * into appropriate HTTP responses following the RFC 7807 Problem Details standard.
 * <p>
 * It provides specialized handling for API-specific exceptions and a fallback
 * handler for unexpected exceptions.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7807">RFC 7807</a>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Property name for the timestamp in the problem detail response.
     */
    private static final String TIMESTAMP_PROPERTY = "timestamp";

    /**
     * Registry of exception-specific property extractors.
     * <p>
     * This map associates each API exception type with a function that can extract
     * its unique properties for inclusion in the problem detail response.
     * <p>
     * When adding a new exception type that extends ApiException, register a
     * corresponding property extractor here to ensure its unique properties
     * are included in the response.
     */
    private static final Map<Class<? extends ApiException>, Function<ApiException, Map<String, Object>>>
            EXCEPTION_PROPERTY_EXTRACTORS = new HashMap<>();

    // Initialize the registry with property extractors for each exception type
    static {
        // UserNotFoundException property extractor
        EXCEPTION_PROPERTY_EXTRACTORS.put(UserNotFoundException.class,
                ex -> {
                    Map<String, Object> props = new HashMap<>();
                    UserNotFoundException unfe = (UserNotFoundException) ex;
                    props.put("userId", unfe.getUserId());
                    return props;
                });

        // InvalidRequestParameterException property extractor
        EXCEPTION_PROPERTY_EXTRACTORS.put(InvalidRequestParameterException.class,
                ex -> {
                    Map<String, Object> props = new HashMap<>();
                    InvalidRequestParameterException irpe = (InvalidRequestParameterException) ex;
                    props.put("parameterName", irpe.getParameterName());
                    return props;
                });

        // Add more exception types here as needed
    }

    /**
     * Handles all exceptions that extend ApiException.
     * <p>
     * This method converts API exceptions into RFC 7807 Problem Detail responses,
     * including the exception's status, title, type, and any exception-specific
     * properties.
     *
     * @param ex The API exception that was thrown.
     * @return A ProblemDetail object representing the error response.
     */
    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException ex) {
        // Create the base problem detail
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        detail.setTitle(ex.getTitle());
        detail.setType(URI.create(ex.getType()));
        detail.setProperty(TIMESTAMP_PROPERTY, ex.getTimestamp().toString());

        // Add exception-specific properties if there's an extractor for this type
        Function<ApiException, Map<String, Object>> propertyExtractor =
                EXCEPTION_PROPERTY_EXTRACTORS.get(ex.getClass());

        if (propertyExtractor != null) {
            Map<String, Object> properties = propertyExtractor.apply(ex);
            properties.forEach(detail::setProperty);
        } else {
            log.warn("No property extractor registered for exception type: {}", ex.getClass().getName());
        }

        return detail;
    }

    /**
     * Fallback handler for unexpected exceptions.
     * <p>
     * This method handles any exceptions that don't have specialized handlers.
     * It logs the error and returns a generic 500 Internal Server Error response.
     *
     * @param ex The exception that was thrown.
     * @return A ProblemDetail object representing the error response.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unexpected error occurred", ex);
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        detail.setTitle("Internal Server Error");
        detail.setType(URI.create("/errors/internal-server-error"));
        detail.setProperty(TIMESTAMP_PROPERTY, Instant.now().toString());
        return detail;
    }
}