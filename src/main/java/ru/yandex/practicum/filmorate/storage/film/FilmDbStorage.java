package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingDbStorage ratingDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Override
    public Film create(Film film) {
        final String sqlQuery = "INSERT INTO movies(film_name, film_description, film_release_date, film_duration) "
                + "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            return stmt;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        final String ratingSqlQuery = "INSERT INTO movies_ratings (film_id, rating_id) VALUES (?, ?)";
        jdbcTemplate.update(ratingSqlQuery, film.getId(), film.getMpa().getId());
        final String genreSqlQuery = "INSERT INTO movies_genres (film_id, genre_id) VALUES (?, ?)";

        if (film.getGenres() != null) {
            for (Genre g: film.getGenres()) {
                jdbcTemplate.update(genreSqlQuery, film.getId(), g.getId());
            }
        }

        film.setMpa(ratingDbStorage.getRatingByFilmId(film.getId()));
        film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
        return film;
    }

    @Override
    public Film update(Film film) {
        final String getFilmSqlQuery = "SELECT * FROM movies WHERE film_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getFilmSqlQuery, film.getId());

        if (!rowSet.next()) {
            throw new NoSuchElementException("Фильма с ID " + film.getId() + " нет в базе данных");
        }

        if (film.getMpa() != null) {
            final String removeRating = "DELETE FROM movies_ratings WHERE film_id = ?";
            final String updateRating = "INSERT INTO movies_ratings (film_id, rating_id) VALUES (?, ?)";

            jdbcTemplate.update(removeRating, film.getId());
            jdbcTemplate.update(updateRating, film.getId(), film.getMpa().getId());
        }

        if (film.getGenres() != null) {
            final String deleteGenreSqlQuery = "DELETE FROM movies_genres WHERE film_id = ?";
            final String updateGenreSqlQuery = "INSERT INTO movies_genres (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(deleteGenreSqlQuery, film.getId());

            for (Genre g : film.getGenres()) {
                String duplicateCheckSqlQuery = "SELECT * FROM movies_genres WHERE film_id = ? AND genre_id = ?";
                SqlRowSet duplicateCheckRowSet = jdbcTemplate.queryForRowSet(duplicateCheckSqlQuery, film.getId(), g.getId());

                if (!duplicateCheckRowSet.next()) {
                    jdbcTemplate.update(updateGenreSqlQuery, film.getId(), g.getId());
                }
            }
        }

        final String sqlQuery = "UPDATE movies " +
                "SET film_name = ?, film_description = ?, film_release_date = ?, film_duration = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        film.setMpa(ratingDbStorage.getRatingByFilmId(film.getId()));
        film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
        return film;
    }

    @Override
    public Film delete(Film film) {
        final String sqlQuery = "DELETE FROM movies WHERE film_id = ?";
        Long filmId = film.getId();
        jdbcTemplate.update(sqlQuery, filmId);
        return film;
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "SELECT * FROM movies";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getById(Long id) {
        final String sqlQueryForChecking = "SELECT * FROM movies WHERE film_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQueryForChecking, id);

        if (!rowSet.next()) {
            throw new NoSuchElementException("Фильма с ID " + id + " нет в базе данных");
        }

        final String sqlQuery = "SELECT * FROM movies WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .releaseDate(rs.getDate("film_release_date").toLocalDate())
                .duration(rs.getLong("film_duration"))
                .mpa(ratingDbStorage.getRatingByFilmId(rs.getLong("film_id")))
                .genres(genreDbStorage.getGenresByFilmId(rs.getLong("film_id")))
                .build();
    }
}
