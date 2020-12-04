package com.example;

import com.example.model.Genre;
import com.example.repository.GenreRepositoryJpaImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Genre repo tests")
@DataJpaTest
@Import(GenreRepositoryJpaImpl.class)
public class GenreRepositoryJpaTest {

    private static final long ID = 2L;
    private static final Genre GENRE = new Genre("anime");
    private static final String GENRE_NAME = "detective";
    private static final String ANIME = "anime";
    private static final String DETECTIVE = "detective";

    @Autowired
    private GenreRepositoryJpaImpl genreRepositoryJpa;


    @Test
    void shouldSaveGenreAndReturnSavedObject() {
        Genre genre = genreRepositoryJpa.save(GENRE);
        assertThat(genre.getGenreName()).isEqualTo(GENRE.getGenreName());
    }

    @Test
    void shouldBeFoundById() {
        Genre genre = genreRepositoryJpa.findById(ID).orElseThrow();
        assertThat(genre.getGenreName()).isNotNull();
        assertThat(genre.getGenreName()).isEqualTo(GENRE_NAME);
        assertThat(genre.getBooks()).allMatch(book -> book.getAuthor() != null)
                                    .allMatch((book -> book.getTitle() != null))
                                    .allMatch(book -> !book.getComments().isEmpty());
    }

    @Test
    void shouldBeFoundByName() {
        List<Genre> genres = genreRepositoryJpa.findByName(GENRE_NAME);
        assertThat(genres).allMatch(genre -> Objects.nonNull(genre.getGenreName()))
                            .allMatch(genre -> genre.getId() != 0);
    }

    @Test
    void shouldFindAllGenres(){
        List<Genre> genreList = genreRepositoryJpa.findAll();
        assertThat(genreList.size() > 0);
        assertThat(genreList).allMatch(genre -> Objects.nonNull(genre.getGenreName()));
        assertThat(genreList).allMatch(genre -> !genre.getBooks().isEmpty());
    }

    @Test
    void shouldUpdateGenreById() {
        Genre oldGenre = genreRepositoryJpa.findById(ID).orElseThrow();
        oldGenre.setGenreName(ANIME);
        genreRepositoryJpa.updateById(GENRE, oldGenre.getId());
        Genre newGenre = genreRepositoryJpa.findById(ID).orElseThrow();
        assertThat(newGenre.getGenreName()).isNotEqualTo(DETECTIVE);
        assertThat(newGenre.getGenreName()).isEqualTo(ANIME);
    }

    @Test
    void shouldDeleteGenreById(){
        Genre genre = genreRepositoryJpa.findById(ID).orElseThrow();
        assertThat(genre).isNotNull();
        genreRepositoryJpa.deleteById(ID);
        assertThatThrownBy(() ->genreRepositoryJpa.findById(ID)).isInstanceOf(NoResultException.class);
    }
}
