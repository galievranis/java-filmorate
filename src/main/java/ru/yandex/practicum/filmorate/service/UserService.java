package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(User user) {
        log.info("Добавление пользователя с ID {}", user.getId());
        return userStorage.add(user);
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
        if (userStorage.getById(userId) == null) {
            throw new NoSuchElementException("Пользователя с ID " + userId + " не существует");
        }

        if (userStorage.getById(friendId) == null) {
            throw new NoSuchElementException("Пользователя с ID " + friendId + " не существует");
        }

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        userStorage.update(user);
        userStorage.update(friend);
        log.info("Добавление пользователя с ID {} в друзья пользователя с ID {}", friendId, userId);
        return user;
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
