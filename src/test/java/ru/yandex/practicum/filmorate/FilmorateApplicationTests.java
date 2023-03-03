package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userDbStorage;
	private final FilmDbStorage filmDbStorage;
	private final GenreDbStorage genreDbStorage;
	private final RatingDbStorage ratingDbStorage;

	@Test
	public void testFindUserById() {
		User user = User.builder()
				.id(1L)
				.name("name")
				.email("mail@mail.ru")
				.login("login")
				.birthday(LocalDate.of(1994, 10, 8))
				.build();
		userDbStorage.create(user);

		User actualResult = userDbStorage.getById(1L);

		assertNotNull(actualResult);
		assertEquals(1, actualResult.getId());
	}

	@Test
	public void testFindFilmById() {
		Mpa mpa = Mpa.builder()
				.id(1L)
				.name("G")
				.build();
		Film film = Film.builder()
				.id(1L)
				.name("film")
				.description("description")
				.duration(300)
				.releaseDate(LocalDate.of(2020, 12, 20))
				.mpa(mpa)
				.build();
		filmDbStorage.create(film);

		Film actualResult = filmDbStorage.getById(1L);

		assertNotNull(actualResult);
		assertEquals(1, actualResult.getId());
	}

	@Test
	public void testFindGenreById(){
		Genre genre = genreDbStorage.getById(1L);
		assertNotNull(genre);
		assertEquals("Комедия", genre.getName());
	}

	@Test
	public void testFindMpaById(){
		Mpa mpa = ratingDbStorage.getById(1L);
		assertNotNull(mpa);
		assertEquals("G", mpa.getName());
	}
}
