package ru.yandex.practicum.filmorate.storage.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Repository("ratingDbStorage")
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        final String sqlQuery = "SELECT * FROM ratings";
        return jdbcTemplate.query(sqlQuery, this::mapRowToRating);
    }

    @Override
    public Mpa getById(Long id) {
        String sqlQuery = "SELECT * FROM ratings WHERE rating_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (!rowSet.next()) {
            throw new NoSuchElementException("Рейтинг c ID " + id + " не найден");
        }

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, id);
    }

    private Mpa mapRowToRating(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("rating_id"))
                .name(rs.getString("rating_name"))
                .build();
    }
}
