package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Repository("genreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        final String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getById(Long id) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (!rowSet.next()) {
            throw new NoSuchElementException("Жанр c ID " + id + " не найден");
        }

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    public List<Genre> getGenresByFilmId(Long filmId) {
        final String sqlQuery = "SELECT * " +
                "FROM genres AS g " +
                "JOIN movies_genres AS mg ON g.genre_id = mg.genre_id " +
                "WHERE mg.film_id = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }
}
