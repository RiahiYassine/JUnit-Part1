package learning.junit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user with the specified ID is not found in the system.
 * <p>
 * This exception is typically thrown by service methods that attempt to retrieve,
 * update, or delete a user by ID when the ID doesn't exist in the database.
 * <p>
 * When caught by the {@link GlobalExceptionHandler}, it will generate a 404 Not Found
 * response with details about the specific user ID that wasn't found.
 */
@Getter
public final class UserNotFoundException extends ApiException {

    /**
     * The ID of the user that wasn't found.
     */
    private final Long userId;

    /**
     * Creates a new UserNotFoundException for the specified user ID.
     *
     * @param userId The ID of the user that wasn't found.
     */
    public UserNotFoundException(Long userId) {
        super(
                "User not found with id: " + userId,
                HttpStatus.NOT_FOUND,
                "User Not Found",
                "/errors/user-not-found"
        );
        this.userId = userId;
    }
}