package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        if (userStorage.getById(user.getId()) == null) {
            throw new NoSuchElementException("Такого пользователя не существует");
        }

        return userStorage.update(user);
    }

    public User delete(User user) {
        return userStorage.delete(user);
    }

    public Set<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        if (userStorage.getById(id) == null) {
            throw new NoSuchElementException("Пользователя с ID " + id + " не существует");
        }
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
        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        userStorage.update(user);
        userStorage.update(friend);
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

        return commonFriends;
    }
}
