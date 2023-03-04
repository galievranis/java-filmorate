CREATE TABLE IF NOT EXISTS movies (
    film_id IDENTITY PRIMARY KEY,
    film_name VARCHAR(255),
    film_description VARCHAR(200),
    film_release_date DATE,
    film_duration INT
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id IDENTITY PRIMARY KEY,
    genre_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS movies_genres (
    film_id BIGINT,
    genre_id BIGINT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES movies (film_id),
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);

CREATE TABLE IF NOT EXISTS ratings (
    rating_id IDENTITY PRIMARY KEY,
    rating_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS movies_ratings (
    film_id BIGINT,
    rating_id INT,
    PRIMARY KEY (film_id, rating_id),
    FOREIGN KEY (film_id) REFERENCES movies (film_id),
    FOREIGN KEY (rating_id) REFERENCES ratings (rating_id)
);

CREATE TABLE IF NOT EXISTS users (
    user_id IDENTITY PRIMARY KEY,
    user_name VARCHAR(255),
    user_login VARCHAR(50),
    user_email VARCHAR(50),
    user_birthday DATE
);

CREATE TABLE IF NOT EXISTS friendship (
    first_user_id BIGINT,
    second_user_id BIGINT,
    friendship_status VARCHAR(50),
    PRIMARY KEY (first_user_id, second_user_id),
    FOREIGN KEY (first_user_id) REFERENCES users (user_id),
    FOREIGN KEY (second_user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS likes (
    user_id BIGINT,
    film_id BIGINT,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (film_id) REFERENCES movies (film_id)
);

