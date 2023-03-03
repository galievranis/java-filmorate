package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class Film {
    private Long id;
    private LocalDate releaseDate;
    private Mpa mpa;
    private List<Genre> genres;

    @Size(min = 1, max = 200, message = "Максимальная длина описания не должна превышать 200 символов")
    private String description;

    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;

    @Min(value = 0, message = "Продолжительность фильма не должна быть отрициательной")
    private long duration;
}
