package learning.junit.services;

import learning.junit.entities.User;
import learning.junit.exceptions.InvalidRequestParameterException;
import learning.junit.exceptions.UserNotFoundException;
import learning.junit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        user.setActive(true);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        if (id == null) {
            throw new InvalidRequestParameterException("id");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }


    @Override
    public List<User> getListOfUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public boolean isUserAdult(Long id) {
        User user = getUserById(id);
        return user.getBirthDate() != null && LocalDate.now().isAfter(user.getBirthDate().plusYears(18));
    }
}
