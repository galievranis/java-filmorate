package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public User create(User user) {
        log.info("Добавление пользователя с ID {}", user.getId());
        return userStorage.create(user);
    }

    public User update(User user) {
        log.info("Обновление пользователя с ID {}", user.getId());
        return userStorage.update(user);
    }

    public User delete(User user) {
        log.info("Удаление пользователя с ID {}", user.getId());
        return userStorage.delete(user);
    }

    public List<User> getAll() {
        log.info("Получение списка всех пользователей");
        return userStorage.getAll();
    }

    public User getById(Long id) {
        log.info("Получение пользователя с ID {}", id);
        return userStorage.getById(id);
    }

    public User addFriend(Long userId, Long friendId) {
        validateUser(userId);
        validateUser(friendId);

        final String sqlQueryForGettingFriends = "SELECT * " +
                "FROM friendship " +
                "WHERE first_user_id = ? AND second_user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQueryForGettingFriends, userId, friendId);

        final String sqlQueryForUpdate = "UPDATE friendship SET friendship_status = ? " +
                "WHERE first_user_id = ? AND second_user_id = ?";
        final String sqlQueryForInsert = "INSERT INTO friendship(first_user_id, second_user_id, friendship_status) " +
                "VALUES (?, ?, ?)";

        if (sqlRowSet.first()) {
            jdbcTemplate.update(sqlQueryForUpdate, FriendshipStatus.CONFIRMED.toString(), userId, friendId);
        } else {
            jdbcTemplate.update(sqlQueryForInsert, userId, friendId, FriendshipStatus.PENDING.toString());
        }

        log.info("Пользователь с ID {} добавил в друзья пользователя с ID {}", userId, friendId);
        return userStorage.getById(userId);
    }

    public User deleteFriend(Long userId, Long friendId) {
        final String sqlQuery = "DELETE FROM friendship WHERE first_user_id = ? AND second_user_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Удаление пользователя с ID {} из друзей пользователя с ID {}", friendId, userId);
        return userStorage.getById(userId);
    }

    public List<User> getFriends(Long userId) {
        final String getUserSqlQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getUserSqlQuery, userId);

        if (!sqlRowSet.next()) {
            throw new NoSuchElementException("Пользователя с ID " + userId + " не существует");
        }

        final String sqlQuery = "SELECT * " +
                "FROM users AS u " +
                "JOIN friendship AS f ON u.user_id = f.second_user_id " +
                "WHERE first_user_id = ? AND friendship_status LIKE 'PENDING'";

        log.info("Получение списка всех друзей пользователя с ID {}", userId);
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        validateUser(userId);
        validateUser(otherUserId);

        final String sqlQuery = "SELECT * " +
                "FROM friendship AS f " +
                "JOIN users AS u ON u.user_id = f.second_user_id " +
                "WHERE f.first_user_id = ? AND f.second_user_id IN (" +
                "SELECT second_user_id " +
                "FROM friendship AS f " +
                "JOIN users AS u ON u.user_id = f.second_user_id " +
                "WHERE f.first_user_id = ?)";

        log.info("Получение списка общих друзей пользователей с ID {} и с ID {}", userId, otherUserId);
        log.info("Список общих друзей пользователей {}", jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherUserId));
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherUserId);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .name(rs.getString("user_name"))
                .login(rs.getString("user_login"))
                .email(rs.getString("user_email"))
                .birthday(rs.getDate("user_birthday").toLocalDate())
                .build();
    }

    void validateUser(Long userId) {
        final String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet firstRowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId);

        if (!firstRowSet.next()) {
            throw new NoSuchElementException("Пользователя с ID " + userId + " не существует");
        }
    }
}
