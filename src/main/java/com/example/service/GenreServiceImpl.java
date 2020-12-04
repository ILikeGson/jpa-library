package com.example.service;

import com.example.exception.GenreNotFoundException;
import com.example.model.Genre;
import com.example.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService{
    private static final String EXCEPTION_MESSAGE = "Genre with id %d not found";
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional
    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Transactional(readOnly = true)
    @Override
    public Genre findById(Long id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            return genre.get();
        }
        throw new GenreNotFoundException(String.format(EXCEPTION_MESSAGE, id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Transactional
    @Override
    public void updateById(Genre genre, Long id) {
        genreRepository.updateById(genre, id);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }

    @Override
    public Genre parseString(String str) {
        return new Genre(str.trim());
    }
}
