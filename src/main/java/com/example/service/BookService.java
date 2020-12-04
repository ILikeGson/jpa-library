package com.example.service;

import com.example.model.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);
    Book findById(Long id);
    List<Book> findAll();
    List<Book> findByName(String name);
    void updateById(Book t, Long id);
    void deleteById(Long id);
    Book parseString(String str);
}
