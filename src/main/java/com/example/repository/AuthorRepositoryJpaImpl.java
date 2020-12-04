package com.example.repository;

import com.example.model.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class AuthorRepositoryJpaImpl implements AuthorRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            entityManager.persist(author);
            return author;
        }
        return entityManager.merge(author);
    }

    @Override
    public Optional<Author> findById(Long id) {
        TypedQuery<Author> query = entityManager.createQuery("select a from Author a where a.id = :id", Author.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Author> findByName(String name) {
        TypedQuery<Author> query = entityManager.createQuery("select a from Author a where a.firstName = :name", Author.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = entityManager.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public void updateById(Author author, Long id) {
        Query query = entityManager.createQuery("update Author a set a.firstName = :firstName, a.lastName = :lastName");
        query.setParameter("firstName", author.getFirstName());
        query.setParameter("lastName", author.getLastName());
        query.executeUpdate();
    }

    @Override
    public void deleteById(Long id) {
        Author author = findById(id).orElseThrow();
        entityManager.remove(author);
        //Query query = entityManager.createQuery("delete from Author a where a.id = :id");
        //query.setParameter("id", id);
        //query.executeUpdate();
    }
}
