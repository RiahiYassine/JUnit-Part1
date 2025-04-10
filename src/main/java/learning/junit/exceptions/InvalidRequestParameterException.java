package learning.junit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a request contains an invalid parameter.
 * <p>
 * This exception is typically thrown during request validation when a parameter
 * fails to meet the required format, range, or other validation rules. It can
 * be used for path variables, query parameters, or request body fields.
 * <p>
 * When caught by the {@link GlobalExceptionHandler}, it will generate a 400 Bad Request
 * response with details about which parameter was invalid.
 */
@Getter
public final class InvalidRequestParameterException extends ApiException {

    /**
     * The name of the parameter that was invalid.
     */
    private final String parameterName;

    /**
     * Creates a new InvalidRequestParameterException for the specified parameter.
     *
     * @param parameterName The name of the parameter that was invalid.
     */
    public InvalidRequestParameterException(String parameterName) {
        super(
                "Invalid request parameter: " + parameterName,
                HttpStatus.BAD_REQUEST,
                "Invalid Request",
                "/errors/invalid-parameter"
        );
        this.parameterName = parameterName;
    }
}