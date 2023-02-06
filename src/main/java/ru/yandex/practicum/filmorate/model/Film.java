package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class Film extends StorageData {
    LocalDate releaseDate;
    Set<Long> likes;

    @Size(min = 1, max = 200, message = "Максимальная длина описания не должна превышать 200 символов")
    String description;

    @NotBlank(message = "Название фильма не должно быть пустым")
    String name;

    @Min(value = 0, message = "Продолжительность фильма не должна быть отрициательной")
    long duration;

    public Set<Long> getLikes() {
        if (likes == null) {
            likes = new HashSet<>();
        }

        return likes;
    }
}
