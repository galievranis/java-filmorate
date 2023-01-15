package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class Film {
    int id;
    String description;
    LocalDate releaseDate;

    @NotBlank(message = "Название фильма не должно быть пустым")
    String name;

    @Min(value = 0, message = "Продолжительность фильма не должна быть отрициательной")
    int duration;
}
