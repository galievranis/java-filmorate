package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    UserController userController = new UserController();

    @Test
    public void shouldAddNewUser() throws ValidationException {
        User user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1953, 12, 20));
        userController.create(user);

        Set<User> expectedResult = new HashSet<>();
        expectedResult.add(user);
        Set<User> actualResult = userController.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldAddNewUserWithEmptyName() throws ValidationException {
        User user = new User();
        user.setLogin("login");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1953, 12, 20));
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
        User user = new User();
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));
        Throwable exception = assertThrows(ValidationException.class, () -> userController.create(user));

        String expectedResult = "День рождения не может быть в будущем";
        String actualResult = exception.getMessage();

        assertEquals(expectedResult, actualResult);
    }
}
