package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping
    public Set<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable String id) {
        if (userService.getById(Long.parseLong(id)) == null) {
            throw new NoSuchElementException("Пользователя с ID " + id + " не существует");
        }
        return userService.getById(Long.parseLong(id));
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable String id) {
        if (userService.getById(Long.parseLong(id)) == null) {
            throw new NoSuchElementException("Пользователя с ID " + id + " не существует");
        }
        return userService.getFriends(Long.parseLong(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable String id,
                                      @PathVariable String otherId) {
        return userService.getCommonFriends(Long.parseLong(id), Long.parseLong(otherId));
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        validate(user);
        return userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        if (userService.getById(user.getId()) == null) {
            throw new NoSuchElementException("Такого пользователя не существует");
        }

        validate(user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriend(@PathVariable String id,
                            @PathVariable String friendId) {
        if (userService.getById(Long.parseLong(id)) == null) {
            throw new NoSuchElementException("Пользователя с ID " + id + " не существует");
        }

        if (userService.getById(Long.parseLong(friendId)) == null) {
            throw new NoSuchElementException("Пользователя с ID " + friendId + " не существует");
        }

        return userService.addFriend(Long.parseLong(id), Long.parseLong(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFromFriend(@PathVariable String id,
                                 @PathVariable String friendId) {
        return userService.deleteFriend(Long.parseLong(id), Long.parseLong(friendId));
    }

    @DeleteMapping("/{id}")
    public User delete(@PathVariable String id) {
        return userService.delete(getById(id));
    }

    public void validate(User user) throws ValidationException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем");
        }
    }
}
