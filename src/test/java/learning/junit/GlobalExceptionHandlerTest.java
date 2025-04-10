package learning.junit;

import learning.junit.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    static Stream<Arguments> exceptionProvider() {
        return Stream.of(
                Arguments.of(
                        new UserNotFoundException(42L),
                        HttpStatus.NOT_FOUND.value(),
                        "User not found with id: 42",
                        "User Not Found",
                        "/errors/user-not-found",
                        "userId",
                        "42"
                ),
                Arguments.of(
                        new InvalidRequestParameterException("id"),
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid request parameter: id",
                        "Invalid Request",
                        "/errors/invalid-parameter",
                        "parameterName",
                        "id"
                )
                // Add more exceptions here as you create them
        );
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void testHandleApiExceptions(ApiException exception, int expectedStatus,
                                 String expectedDetail, String expectedTitle,
                                 String expectedType, String propertyName,
                                 String propertyValue) {
        // Act
        ProblemDetail result = handler.handleApiException(exception);

        // Assert
        assertEquals(expectedStatus, result.getStatus());
        assertEquals(expectedDetail, result.getDetail());
        assertEquals(expectedTitle, result.getTitle());
        assertEquals(expectedType, result.getType().toString());
        assertNotNull(result.getProperties());
        assertEquals(propertyValue, result.getProperties().get(propertyName).toString());
        assertNotNull(result.getProperties().get("timestamp"));
    }

    @Test
    void testHandleUnexpected() {
        // Arrange - This one is different enough to keep separate
        Exception exception = new RuntimeException("Unexpected error");

        // Act
        ProblemDetail result = handler.handleUnexpected(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getStatus());
        assertEquals("An unexpected error occurred.", result.getDetail());
        assertEquals("Internal Server Error", result.getTitle());
        assertEquals("/errors/internal-server-error", result.getType().toString());
        assertNotNull(result.getProperties());
        assertNotNull(result.getProperties().get("timestamp"));
    }
}