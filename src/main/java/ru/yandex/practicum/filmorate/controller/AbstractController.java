package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.StorageData;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@RestController
@Slf4j
public abstract class AbstractController<T extends StorageData> {
    private int id = 1;
    private final Map<Integer, T> data = new HashMap<>();

    @GetMapping
    public Set<T> getAll() {
        log.info("Количество данных на текущий момент: {}", data.size());
        return new HashSet<>(data.values());
    }

    @PostMapping
    public T create(@Valid @RequestBody T value) throws ValidationException {
        validate(value);

        if (!data.containsValue(value)) {
            value.setId(id++);
            data.put(value.getId(), value);
            log.info("Добавлен новый объект в базу данных");
        }

        return value;
    }

    @PutMapping
    public T update(@Valid @RequestBody T value) throws ValidationException {
        validate(value);

        if (!data.containsKey(value.getId())) {
            throw new ValidationException("Элемента с id:" + value.getId() + "нет в базе данных.");
        }

        data.put(value.getId(), value);
        log.info("Элемент {} обновлен", value.getId());

        return value;
    }

    public abstract void validate(T value) throws ValidationException;
}
