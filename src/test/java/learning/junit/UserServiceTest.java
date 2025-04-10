package learning.junit;

import learning.junit.entities.User;
import learning.junit.exceptions.InvalidRequestParameterException;
import learning.junit.exceptions.UserNotFoundException;
import learning.junit.repositories.UserRepository;
import learning.junit.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;
    private User testUser;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
        testUser = new User(
                1L,
                "Yassine",
                "yassineriahi0417@gmail.com",
                List.of("ADMIN"),
                true,
                LocalDate.of(2000, 4, 1)
        );
    }

    @Test
    @DisplayName("Should return user when a valid ID is provided")
    void whenValidUserIdProvided_thenReturnMatchingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        User foundUser = userService.getUserById(1L);
        assertNotNull(foundUser, "Expected a user for a valid ID");
        assertEquals(testUser, foundUser, "Retrieved user should match the expected user");
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException for a non-existing user ID")
    void whenNonExistingUserIdProvided_thenThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L),
                "Expected a UserNotFoundException for a non-existing user ID");
        verify(userRepository).findById(99L);
    }

    @Test
    @DisplayName("Should throw InvalidRequestParameterException when null ID is provided")
    void whenNullIdProvided_thenThrowException() {
        assertThrows(InvalidRequestParameterException.class, () -> userService.getUserById(null),
                "Expected an InvalidRequestParameterException for a null ID");
        verifyNoInteractions(userRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0})
    @DisplayName("Should throw UserNotFoundException for negative or zero user ID")
    void whenNegativeOrZeroIdProvided_thenThrowException(long negativeId) {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(negativeId),
                "Expected a UserNotFoundException for negative or zero user ID");
        assertEquals(negativeId, exception.getUserId(), "Exception should contain the correct user ID");
    }

    @Test
    @DisplayName("Should mark user as inactive after deactivation")
    void whenUserDeactivated_thenReturnInactive() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        userService.deactivateUser(1L);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertFalse(savedUser.isActive(), "Expected the user to be deactivated");
    }

    @Test
    @DisplayName("Should create user successfully")
    void whenCreateUser_thenReturnCreatedUser() {
        LocalDate fixedDate = LocalDate.of(2020, 1, 1);
        User newUser = new User(null, "New User", "newuser@example.com", List.of("USER"), false, fixedDate);
        User savedUser = new User(2L, "New User", "newuser@example.com", List.of("USER"), true, fixedDate);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        User result = userService.createUser(newUser);
        assertNotNull(result.getId(), "Created user should have an ID");
        assertEquals(2L, result.getId(), "Created user should have the expected ID");
        assertEquals(newUser.getUsername(), result.getUsername(), "Usernames should match");
        verify(userRepository).save(any(User.class));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertNull(capturedUser.getId(), "User passed to save should have null ID (to be auto-generated)");
        assertTrue(capturedUser.isActive(), "User should be active before saving");
    }

    @Test
    @DisplayName("Should return consistent details on repeated calls")
    void whenCalledRepeatedly_thenReturnSameDetails() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        User firstCall = userService.getUserById(1L);
        User secondCall = userService.getUserById(1L);
        assertEquals(firstCall, secondCall, "Expected consistent user details on repeated calls");
        verify(userRepository, times(2)).findById(1L);
    }
}