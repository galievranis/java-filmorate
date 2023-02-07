package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Long id = 1L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        if (!users.containsValue(user)) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }

            user.setId(id++);
            users.put(user.getId(), user);
        }

        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }

        return user;
    }

    @Override
    public User delete(User user) {
        users.remove(user.getId());
        return user;
    }

    @Override
    public Set<User> getAll() {
        return new HashSet<>(users.values());
    }

    @Override
    public User getById(Long id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("Пользователя с ID " + id + " нет в базе данных");
        }
        return users.get(id);
    }
}
