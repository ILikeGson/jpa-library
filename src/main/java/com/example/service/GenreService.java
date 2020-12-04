package com.example.service;

import com.example.model.Genre;

import java.util.List;

public interface GenreService {
    Genre save(Genre genre);
    Genre findById(Long id);
    List<Genre> findAll();
    List<Genre> findByName(String name);
    void updateById(Genre genre, Long id);
    void deleteById(Long id);
    Genre parseString(String str);
}
