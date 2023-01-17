package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends AbstractController<User> {
    @Override
    public void validate(User value) throws ValidationException {
        if (value.getName() == null || value.getName().isEmpty()) {
            value.setName(value.getLogin());
        }

        if (value.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем");
        }
    }
}
