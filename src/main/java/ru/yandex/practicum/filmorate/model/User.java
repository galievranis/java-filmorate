package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@EqualsAndHashCode
public class User {
    private Long id;
    private String name;
    private LocalDate birthday;

    @JsonIgnoreProperties("friends")
    private List<Long> friendsIds;

    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "\\S*", message = "Логин не должен содержать пробелов")
    private String login;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Указан невалидный email")
    private String email;

    public List<Long> getFriends() {
        if (friendsIds == null) {
            friendsIds = new ArrayList<>();
        }

        return friendsIds;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("user_name", name);
        values.put("user_login", login);
        values.put("user_email", email);
        values.put("user_birthday", birthday);
        return values;
    }
}
