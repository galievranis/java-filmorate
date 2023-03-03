package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserControllerTest {
    @Autowired
    private final UserController userController;
    @Autowired
    private final Validator validator;

    @Test
    public void shouldAddNewUser() {
        User user = User.builder()
                .birthday(LocalDate.of(1953, 12, 20))
                .email("mail@mail.ru")
                .login("login")
                .name("name")
                .build();
        userController.create(user);

        List<User> expectedResult = new ArrayList<>();
        expectedResult.add(user);
        List<User> actualResult = userController.getAll();

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

    @Test
    public void shouldNotAddUserWithNullLogin() {
        User user = User.builder()
                .birthday(LocalDate.of(1994, 10, 8))
                .email("mail@mail.ru")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        int expectedResult = 1;
        int actualResult = violations.size();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldNotAddUserWithLoginThatContainsSpaces() {
        User user = User.builder()
                .login("invalid user login")
                .birthday(LocalDate.of(1994, 10, 8))
                .email("mail@mail.ru")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        int expectedResult = 1;
        int actualResult = violations.size();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldNotAddUserWithNullEmail() {
        User user = User.builder()
                .login("login")
                .birthday(LocalDate.of(1994, 10, 8))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        int expectedResult = 1;
        int actualResult = violations.size();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldNotAddUserWithInvalidEmail() {
        User user = User.builder()
                .login("login")
                .birthday(LocalDate.of(1994, 10, 8))
                .email("mailmail.ru")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        int expectedResult = 1;
        int actualResult = violations.size();

        assertEquals(expectedResult, actualResult);
    }
}
