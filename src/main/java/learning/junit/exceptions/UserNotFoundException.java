package learning.junit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public final class UserNotFoundException extends RuntimeException {
    public MyCustomException(String message) {
        super(message);
    }
}