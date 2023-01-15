package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

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
