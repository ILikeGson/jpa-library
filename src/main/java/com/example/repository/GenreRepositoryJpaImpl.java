package com.example.repository;

import com.example.model.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryJpaImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Genre save(Genre genre) {
        List<Genre> genres = findByName(genre.getGenreName());
        if (genres.size() > 0) {
            return genres.get(0);
        } else {
            entityManager.persist(genre);
            return genre;
        }
    }

    @Override
    public Optional<Genre> findById(Long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("genre_all_data");
        TypedQuery<Genre> query = entityManager.createQuery("select g from Genre g where g.id = :id", Genre.class);
        query.setParameter("id", id);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Genre> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("genre_all_data");
        TypedQuery<Genre> query = entityManager.createQuery("select g from Genre g", Genre.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public void updateById(Genre genre, Long id) {
        Query query = entityManager.createQuery("update Genre g set g.genreName = :genre " +
                " where g.id = :id");
        query.setParameter("genre", genre.getGenreName());
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id).orElseThrow());
    }

    @Override
    public List<Genre> findByName(String name) {
        TypedQuery<Genre> query = entityManager.createQuery("select g from Genre g where g.genreName = :genre", Genre.class);
        query.setParameter("genre", name);
        return query.getResultList();
    }
}
