package com.example.service;

import com.example.model.BookComment;

import java.util.List;

public interface BookCommentService {
    BookComment save(BookComment comment);
    BookComment findById(Long id);
    List<BookComment> findAll();
    List<BookComment> findByName(String name);
    void updateById(BookComment comment, Long id);
    void deleteById(Long id);
    BookComment parseString(String str);
}
