package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    @Qualifier("genreDbStorage")
    private final GenreDbStorage genreDbStorage;

    public List<Genre> getAll() {
        log.info("Получение списка всех рейтингов");
        return genreDbStorage.getAll();
    }

    public Genre getById(Long id) {
        log.info("Получение рейтинга по ID {}", id);
        return genreDbStorage.getById(id);
    }
}
