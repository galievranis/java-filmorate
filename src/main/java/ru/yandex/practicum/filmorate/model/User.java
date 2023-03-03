package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode
public class User {
    private Long id;
    private String name;
    private LocalDate birthday;

    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "\\S*", message = "Логин не должен содержать пробелов")
    private String login;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Указан невалидный email")
    private String email;
}
