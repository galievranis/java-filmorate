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
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends StorageData {
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
}
