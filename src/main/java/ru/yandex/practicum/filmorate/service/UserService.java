package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserStorage userStorage;

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User delete(User user) {
        return userStorage.delete(user);
    }

    public Set<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public User addFriend(Long userId, Long friendId) {
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
