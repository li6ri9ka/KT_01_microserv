package com.example.KT_01.user;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public synchronized List<User> findAll() {
        return users.stream()
                .sorted(Comparator.comparing(User::getId))
                .map(user -> new User(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    public synchronized User findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(user -> new User(user.getId(), user.getName(), user.getEmail()))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public synchronized User create(UserCreateRequest request) {
        long nextId = sequence.incrementAndGet();
        User user = new User(nextId, request.getName().trim(), request.getEmail().trim());
        users.add(user);
        return new User(user.getId(), user.getName(), user.getEmail());
    }

    public synchronized User update(Long id, UserUpdateRequest request) {
        User existing = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));

        existing.setName(request.getName().trim());
        existing.setEmail(request.getEmail().trim());
        return new User(existing.getId(), existing.getName(), existing.getEmail());
    }

    public synchronized void delete(Long id) {
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (!removed) {
            throw new UserNotFoundException(id);
        }
    }

    synchronized void clearForTests() {
        users.clear();
        sequence.set(0);
    }
}
