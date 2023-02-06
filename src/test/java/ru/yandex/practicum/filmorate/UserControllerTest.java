package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    private final UserController userController = new UserController();


    @Test
    public void shouldAddNewUser() {
        User user = User.builder()
                .birthday(LocalDate.of(1953, 12, 20))
                .email("mail@mail.ru")
                .login("login")
                .name("name")
                .build();
        inMemoryUserStorage.add(user);

        Set<User> expectedResult = new HashSet<>();
        expectedResult.add(user);
        Set<User> actualResult = inMemoryUserStorage.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldAddNewUserWithEmptyName() throws ValidationException {
        User user = User.builder()
                .birthday(LocalDate.of(1953, 12, 20))
                .email("mail@mail.ru")
                .login("login")
                .build();
        userController.create(user);

        Set<User> expectedResult = new HashSet<>();
        expectedResult.add(user);
        Set<User>  actualResult = userController.getAll();

        assertEquals(expectedResult, actualResult);

        String expectedName = "login";
        String actualName = user.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void shouldThrowExceptionWhenInvalidBirthday() {
        User user = User.builder()
                .birthday(LocalDate.now().plusDays(1))
                .email("mail@mail.ru")
                .login("login")
                .build();
        Throwable exception = assertThrows(ValidationException.class, () -> userController.create(user));

        String expectedResult = "День рождения не может быть в будущем";
        String actualResult = exception.getMessage();

        assertEquals(expectedResult, actualResult);
    }
}
