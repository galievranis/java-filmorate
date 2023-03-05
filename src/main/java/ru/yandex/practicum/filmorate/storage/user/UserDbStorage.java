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
import java.time.LocalDate;
import java.util.*;

@Repository("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        final String sqlQuery = "INSERT INTO users(user_name, user_login, user_email, user_birthday) "
                + "VALUES (?, ?, ?, ?)";

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
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getUserSqlQuery, user.getId());

        if (!rowSet.next()) {
            throw new NoSuchElementException("Пользователя с ID " + user.getId() + " нет в базе данных");
        }

        final String sqlQuery = "UPDATE users " +
                "SET user_name = ?, user_login = ?, user_email = ?, user_birthday = ? " +
                "WHERE user_id = ?";

        jdbcTemplate.update(sqlQuery, user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public User delete(User user) {
        final String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        Long userId = user.getId();
        jdbcTemplate.update(sqlQuery, userId);
        return user;
    }

    @Override
    public List<User> getAll() {
        final String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getById(Long id) {
        final String sqlQueryForChecking = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQueryForChecking, id);

        if (!rowSet.next()) {
            throw new NoSuchElementException("Пользователя с ID " + id + " нет в базе данных");
        }

        final String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return getUser(rs);
    }

    public static User getUser(ResultSet rs) throws SQLException {
        Long id = rs.getLong("user_id");
        String name = rs.getString("user_name");
        String login = rs.getString("user_login");
        String email = rs.getString("user_email");
        LocalDate birthday = rs.getDate("user_birthday").toLocalDate();

        return User.builder()
                .id(id)
                .name(name)
                .login(login)
                .email(email)
                .birthday(birthday)
                .build();
    }
}
