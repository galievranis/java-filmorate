package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Long id = 1L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        if (!users.containsValue(user)) {
            user.setId(id++);
            users.put(user.getId(), user);
            log.info("Добавлен новый пользователь с ID {}", user.getId());
        }

        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь с ID {} обновлен", user.getId());
        }

        return user;
    }

    @Override
    public User delete(User user) {
        users.remove(user.getId());
        log.info("Пользователь с ID {} удален", user.getId());
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
