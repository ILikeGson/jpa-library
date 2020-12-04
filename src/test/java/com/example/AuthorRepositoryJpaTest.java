package com.example;

import com.example.model.Author;
import com.example.repository.AuthorRepositoryJpaImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.NoResultException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Author repo test")
@DataJpaTest
@Import(AuthorRepositoryJpaImpl.class)
public class AuthorRepositoryJpaTest {

    private static final String FIRST_NAME = "Maria";
    private static final String LAST_NAME = "Bao";
    private static final long ID = 1L;
    @Autowired
    private AuthorRepositoryJpaImpl authorRepositoryJpa;


    @Test
    void shouldSaveAuthorAndReturnIt() {
        Author author = new Author("Vasya", "Ivanov");
        Author savedAuthor = authorRepositoryJpa.save(author);
        assertThat(savedAuthor.getLastName()).isEqualTo(author.getLastName());
        assertThat(savedAuthor.getFirstName()).isEqualTo(author.getFirstName());
        assertThat(savedAuthor.getId()).isNotNull();
    }

    @Test
    void shouldFindAuthorById(){
        Author foundAuthor = authorRepositoryJpa.findById(1L).orElseThrow(IllegalArgumentException::new);
        assertThat(foundAuthor.getLastName()).isEqualTo("Rowling");
        assertThat(foundAuthor.getFirstName()).isEqualTo("Joanne");
        assertThat(foundAuthor.getId()).isNotNull();
    }

    @Test
    void shouldReturnListOfAllAuthors() {
        assertThat(authorRepositoryJpa.findAll().size() > 0);
    }

    @Test
    void shouldFindAuthorByName(){
        List<Author> authors = authorRepositoryJpa.findByName("Vasya");
        assertThat(authors).allMatch(author -> author.getLastName() != null)
                            .allMatch(author -> author.getFirstName() != null)
                            .allMatch(author -> !author.getBooks().isEmpty())
                            .allMatch(author -> author.getId() != 0);
    }

    @Test
    void shouldUpdateAuthorInfo() {
        Author author = authorRepositoryJpa.findById(ID).orElseThrow();
        author.setFirstName(FIRST_NAME);
        author.setLastName(LAST_NAME);
        authorRepositoryJpa.updateById(author, author.getId());
        author = authorRepositoryJpa.findById(ID).orElseThrow();
        assertThat(author.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(author.getLastName()).isEqualTo(LAST_NAME);
    }

    @Test
    void shouldDeleteAuthorByGivenId() {
        Author author = authorRepositoryJpa.findById(ID).orElseThrow();
        assertThat(author).isNotNull();
        authorRepositoryJpa.deleteById(ID);
        assertThatThrownBy(() -> authorRepositoryJpa.findById(ID)).isInstanceOf(NoResultException.class);
    }

}
