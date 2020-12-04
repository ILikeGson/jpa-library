package com.example.service;

import com.example.exception.BookNotFoundException;
import com.example.model.Author;
import com.example.model.Book;
import com.example.model.BookComment;
import com.example.model.Genre;
import com.example.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private static final String EXCEPTION_MESSAGE = "Book with id %d not found";
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Book findById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()){
            return book.get();
        }
        throw new BookNotFoundException(String.format(EXCEPTION_MESSAGE, id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByName(String name) {
        return bookRepository.findByName(name);
    }

    @Transactional
    @Override
    public void updateById(Book book, Long id) {
        bookRepository.updateById(book, id);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public Book parseString(String str) {
        String[] info = str.split(",");
        String title = info[0].trim();
        String author = info[1].trim();
        String genre = info[2].trim();

        String[] authorsInfo = author.split("\\s");
        List<BookComment> comments = new ArrayList<>();
        Book newBook = new Book(title, new Author(authorsInfo[0].trim(), authorsInfo[1].trim()), new Genre(genre), List.of());
        for (int i = 3; i < info.length; i++) {
            BookComment bookComment = new BookComment(info[i].trim());
            bookComment.setBook(newBook);
            comments.add(bookComment);
        }
        newBook.setComments(comments);
        return newBook;
    }
}
