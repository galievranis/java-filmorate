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

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private JdbcTemplate jdbcTemplate;

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

    public Set<User> getAll() {
        log.info("Получение списка всех пользователей");
        return userStorage.getAll();
    }

    public User getById(Long id) {
        log.info("Получение пользователя с ID {}", id);
        return userStorage.getById(id);
    }

    public User addFriend(Long userId, Long friendId) {
        final String getUserSqlQuery = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(getUserSqlQuery, userId);
        SqlRowSet friendRow = jdbcTemplate.queryForRowSet(getUserSqlQuery, friendId);

        if (userRow.next()) {
            throw new NoSuchElementException("Пользователя с ID " + userId + " не существует");
        }

        if (friendRow.next()) {
            throw new NoSuchElementException("Пользователя с ID " + friendId + " не существует");
        }

        final String sqlQueryForGettingFriends = "SELECT * FROM friendship WHERE first_user_id = ? AND second_user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQueryForGettingFriends, userId, friendId);

        final String sqlQueryForUpdate = "UPDATE friendship SET friendship_status = ? WHERE first_user_id = ? AND second_user_id = ?";
        final String sqlQueryForInsert = "INSERT INTO friendship (first_user_id, second_user_id, friendship_status) VALUES (?, ?, ?)";

        if (userRow.first()) {
            jdbcTemplate.update(sqlQueryForUpdate, FriendshipStatus.CONFIRMED.toString(), userId, friendId);
        } else {
            jdbcTemplate.update(sqlQueryForInsert, userId, friendId, FriendshipStatus.PENDING.toString());
        }

        log.info("Пользователь с ID {} добавил в друзья пользователя с ID {}", userId, friendId);
        return userStorage.getById(userId);
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        userStorage.update(user);
        userStorage.update(friend);
        log.info("Удаление пользователя с ID {} из друзей пользователя с ID {}", friendId, userId);
        return user;
    }

    public Set<User> getFriends(Long userId) {
        if (userStorage.getById(userId) == null) {
            throw new NoSuchElementException("Пользователя с ID " + userId + " не существует");
        }

        Set<User> friends = new HashSet<>();
        User user = userStorage.getById(userId);

        for (Long id : user.getFriends()) {
            User friend = userStorage.getById(id);
            friends.add(friend);
        }

        log.info("Получение списка всех друзей пользователя с ID {}", userId);
        return friends;
    }

    public Set<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherUserId);
        Set<User> commonFriends = new HashSet<>();

        if (user.getFriends() == null || otherUser.getFriends() == null) {
            return new HashSet<>();
        }

        user.getFriends().stream()
                .filter(id -> otherUser.getFriends().contains(id))
                .forEach(id -> commonFriends.add(userStorage.getById(id)));

        log.info("Получение списка общих друзей пользователей с ID {} и с ID {}", userId, otherUser);
        return commonFriends;
    }
}
