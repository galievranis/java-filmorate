package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class User extends StorageData {
    String name;
    LocalDate birthday;

    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "\\S*", message = "Логин не должен содержать пробелов")
    String login;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Указан невалидный email")
    String email;
}
