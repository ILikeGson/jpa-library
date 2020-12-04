package com.example.service;

import com.example.model.Author;

import java.util.List;

public interface AuthorService {
    Author save(Author author);
    Author findById(Long id);
    List<Author> findAll();
    List<Author> findByName(String name);
    void updateById(Author author, Long id);
    void deleteById(Long id);
    Author parseString(String str);
}
