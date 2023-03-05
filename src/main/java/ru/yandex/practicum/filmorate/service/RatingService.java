package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {
    @Qualifier("ratingDbStorage")
    private final RatingDbStorage ratingDbStorage;

    public List<Mpa> getAll() {
        log.info("Получение списка всех рейтингов");
        return ratingDbStorage.getAll();
    }

    public Mpa getById(Long id) {
        log.info("Получение рейтинга по ID {}", id);
        return ratingDbStorage.getById(id);
    }
}
