package learning.junit.repositories;

import learning.junit.entities.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final Map<Long, User> database = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1L);

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.getAndIncrement());
        }
        database.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(database.values());
    }

    public List<User> findByRole(String role) {
        return database.values().stream()
                .filter(user -> user.getRoles() != null && user.getRoles().contains(role))
                .toList();
    }
}
