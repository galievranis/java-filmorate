package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

@Repository("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        final String sqlQuery = "INSERT INTO users(user_name, user_login, user_email, user_birthday) "
                + "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        final String getUserSqlQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(getUserSqlQuery, user.getId());

        if (!userRow.next()) {
            throw new NoSuchElementException("Пользователя с ID " + user.getId() + " нет в базе данных");
        }

        final String sqlQuery = "UPDATE users SET user_name = ?, user_login = ?, user_email = ?, user_birthday = ?" +
                "WHERE user_id = ?";

        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        return user;
    }

    @Override
    public User delete(User user) {
        final String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        long userId = user.getId();
        jdbcTemplate.update(sqlQuery, userId);
        return user;
    }

    @Override
    public Set<User> getAll() {
        final String sqlQuery = "SELECT * FROM users";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToUser));
    }

    @Override
    public User getById(Long id) {
        final String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .name(rs.getString("user_name"))
                .login(rs.getString("user_login"))
                .email(rs.getString("user_email"))
                .birthday(rs.getDate("user_birthday").toLocalDate())
                .build();
    }
}
