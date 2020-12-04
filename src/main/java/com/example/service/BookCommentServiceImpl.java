package com.example.service;

import com.example.exception.BookCommentNotFoundException;
import com.example.model.BookComment;
import com.example.repository.BookCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Service
public class BookCommentServiceImpl implements BookCommentService{
    private static final String EXCEPTION_MESSAGE = "Book comment with id %d not found";
    private final BookCommentRepository bookCommentRepository;

    public BookCommentServiceImpl(BookCommentRepository bookCommentRepository) {
        this.bookCommentRepository = bookCommentRepository;
    }

    @Transactional
    @Override
    public BookComment save(BookComment comment) {
        return bookCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public BookComment findById(Long id) {
        Optional<BookComment> comment = bookCommentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        }
        throw new BookCommentNotFoundException(String.format(EXCEPTION_MESSAGE, id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookComment> findAll() {
        return bookCommentRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookComment> findByName(String name) {
        return bookCommentRepository.findByName(name);
    }

    @Transactional
    @Override
    public void updateById(BookComment comment, Long id) {
        bookCommentRepository.updateById(comment, id);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookCommentRepository.deleteById(id);
    }

    @Override
    public BookComment parseString(final String str) {
        String[] info = str.trim().split("\\s+");
        BookComment bookComment =  new BookComment(info[1].trim());
        bookComment.setId(Long.parseLong(info[0].trim()));
        return bookComment;
    }
}
