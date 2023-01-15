package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Map<Integer, User> getAll() {
        log.info("Количество пользователей на данный момент: {}", users.size());
        return users;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        validateBirthday(user.getBirthday());

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (!users.containsValue(user)) {
            user.setId(id++);
            users.put(user.getId(), user);
            log.info("Добавлен новый пользователь {}", user.getName());
        }

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        validateBirthday(user.getBirthday());

        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Такого пользователя не существует");
        }

        users.put(user.getId(), user);
        log.info("Пользователь {} обновлен", user.getLogin());

        return user;
    }

    public void validateBirthday(LocalDate birthday) throws ValidationException {
        if (birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем");
        }
    }
}
