package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final GenreDbStorage genreDbStorage;

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    public Film create(Film film) {
        log.info("Добавление фильма с ID {}", film.getId());
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.info("Обновление фильма с ID {}", film.getId());
        return filmStorage.update(film);
    }

    public Film delete(Film film) {
        log.info("Удаление фильма с ID {}", film.getId());
        return filmStorage.delete(film);
    }

    public List<Film> getAll() {
        log.info("Получение списка всех фильмов");
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        log.info("Получение фильма с ID {}", id);
        return filmStorage.getById(id);
    }

    public Film addLikeToFilm(Long filmId, Long userId) {
        validateFilm(filmId);
        userService.validateUser(userId);
        final String sqlQuery = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
        return getById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        validateFilm(filmId);
        userService.validateUser(userId);
        final String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Пользователь с ID {} удалил лайк с фильма с ID {}", userId, filmId);
        return getById(filmId);
    }

    public List<Film> getPopularFilms(Long count) {
        log.info("Получение списка популярных фильмов");
        final String sqlQuery = "SELECT * " +
                "FROM movies AS m " +
                "LEFT JOIN ratings AS r ON m.rating_id = r.rating_id " +
                "LEFT JOIN likes AS l ON m.film_id = l.film_id " +
                "GROUP BY m.film_id, l.film_id IN (SELECT film_id FROM likes) " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private void validateFilm(Long filmId) {
        final String getFilmSqlQuery = "SELECT * FROM movies WHERE film_id = ?";
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(getFilmSqlQuery, filmId);

        if (!filmRowSet.next()) {
            throw new NoSuchElementException("Фильма с ID " + filmId + " нет в базе данных");
        }
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("film_id");
        String name = rs.getString("film_name");
        String description = rs.getString("film_description");
        LocalDate releaseDate = rs.getDate("film_release_date").toLocalDate();
        int duration = rs.getInt("film_duration");
        Long ratingId = rs.getLong("rating_id");
        String ratingName = rs.getString("rating_name");

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(new Mpa(ratingId, ratingName))
                .genres(genreDbStorage.getGenresByFilmId(id))
                .build();
    }
}
