package com.example.repository;

import com.example.model.Book;
import com.example.model.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpaImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final GenreRepository genreRepository;
    private final BookCommentRepository bookCommentRepository;

    public BookRepositoryJpaImpl(GenreRepository genreRepository, BookCommentRepository bookCommentRepository) {
        this.genreRepository = genreRepository;
        this.bookCommentRepository = bookCommentRepository;
    }

    @Override
    public Book save(Book book) {
        Genre genre = genreRepository.save(book.getGenre());
        book.setGenre(genre);
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        } else {
            return entityManager.merge(book);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("all-data-entity-graph");
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b where b.id = :id", Book.class);
        query.setParameter("id", id);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Book> findByName(String name) {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b where b.title = :name", Book.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("all-data-entity-graph");
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public void updateById(Book book, Long id) {
        book.setId(id);
        Book oldBook = entityManager.find(Book.class, id);
        book.getAuthor().setId(oldBook.getAuthor().getId());
        if (!book.getGenre().equals(oldBook.getGenre())) {
            List<Genre> genres = genreRepository.findByName(book.getGenre().getGenreName());
            Genre genre = genres.isEmpty() ? genreRepository.save(book.getGenre()) : genres.get(0);
            book.setGenre(genre);
        } else {
            book.setGenre(oldBook.getGenre());
        }
        entityManager.merge(book);
    }

    @Override
    public void deleteById(Long id) {
        Book book = entityManager.find(Book.class, id);
        entityManager.remove(book);
    }
}
