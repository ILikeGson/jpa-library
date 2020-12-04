package com.example.repository;

import com.example.model.Book;
import com.example.model.BookComment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookCommentRepositoryJpaImpl implements BookCommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId()==0) {
            Book book = entityManager.find(Book.class, bookComment.getBook().getId());
            book.add(bookComment);
            bookComment.setBook(book);
            entityManager.persist(bookComment);
            return bookComment;
        } else {
            return entityManager.merge(bookComment);
        }
    }

    @Override
    public Optional<BookComment> findById(Long id) {
        TypedQuery<BookComment> query = entityManager.createQuery("select b from BookComment b where b.id = :id", BookComment.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<BookComment> findByName(String comment) {
        TypedQuery<BookComment> query = entityManager.createQuery("select b from BookComment b where b.comment = :comment", BookComment.class);
        query.setParameter("comment", comment);
        return query.getResultList();
    }

    @Override
    public List<BookComment> findAll() {
        TypedQuery<BookComment> query = entityManager.createQuery("select b from BookComment b", BookComment.class);
        return query.getResultList();
    }

    @Override
    public void updateById(BookComment bookComment, Long id) {
        Query query = entityManager.createQuery("update BookComment b set b.comment = :comment," +
                " b.book = :book where b.id = :id");
        query.setParameter("comment", bookComment.getComment());
        query.setParameter("book", bookComment.getBook());
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteById(Long id) {
        Query query = entityManager.createQuery("delete from BookComment b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
