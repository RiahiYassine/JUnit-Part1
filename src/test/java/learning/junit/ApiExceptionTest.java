package learning.junit;

import learning.junit.exceptions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionTest {

    static Stream<Arguments> exceptionProvider() {
        return Stream.of(
                Arguments.of(
                        new UserNotFoundException(42L),
                        "User not found with id: 42",
                        HttpStatus.NOT_FOUND,
                        "User Not Found",
                        "/errors/user-not-found"
                ),
                Arguments.of(
                        new InvalidRequestParameterException("id"),
                        "Invalid request parameter: id",
                        HttpStatus.BAD_REQUEST,
                        "Invalid Request",
                        "/errors/invalid-parameter"
                )
                // Add more exceptions here as you create them
        );
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void testApiExceptions(ApiException exception, String expectedMessage,
                           HttpStatus expectedStatus, String expectedTitle,
                           String expectedType) {
        // Assert common properties
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedTitle, exception.getTitle());
        assertEquals(expectedType, exception.getType());
        assertNotNull(exception.getTimestamp());

        // Assert specific properties based on exception type
        if (exception instanceof UserNotFoundException) {
            UserNotFoundException unfe = (UserNotFoundException) exception;
            assertEquals(42L, unfe.getUserId());
        } else if (exception instanceof InvalidRequestParameterException) {
            InvalidRequestParameterException irpe = (InvalidRequestParameterException) exception;
            assertEquals("id", irpe.getParameterName());
        }
    }
}