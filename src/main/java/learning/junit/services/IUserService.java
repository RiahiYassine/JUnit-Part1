package learning.junit.services;

import learning.junit.entities.User;
import java.util.List;

public interface IUserService {

    User createUser(User user);

    User getUserById(Long id);

    List<User> getListOfUsers();

    List<User> findByRole(String role);

    void deactivateUser(Long id);

    boolean isUserAdult(Long id);
}
