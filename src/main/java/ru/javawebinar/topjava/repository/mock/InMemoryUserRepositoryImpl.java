package ru.javawebinar.topjava.repository.mock;


import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(user.getId(), (id, oldMeal) -> user);
    }

    @Override
    public boolean delete(int id) {
        if (repository.remove(id) != null)
            return true;
        return false;
    }

    @Override
    public User get(int id) {
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {

        return repository.values().stream()
                .sorted((s1, s2) -> s1.getName().compareTo(s2.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email){
        List<User> list =  repository.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .collect(Collectors.toList());

        if (list.size() != 0)
            return list.get(0);

        return null;
    }
}
