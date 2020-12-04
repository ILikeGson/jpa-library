package com.example.shell;

import com.example.model.Genre;
import com.example.service.GenreService;
import com.example.service.IOService;
import com.example.service.LongParser;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class GenreShellCommands {
    private static final String ENTER_GENRE_NAME = "Enter genre name\nexample: anime\n";
    private static final String SAVED_GENRE_INFO = "Genre have been saved with id(%d)";
    private static final String ENTER_GENRE_ID = "Enter genre id: ";
    private static final String GENRE_DATA = "id: %d\ngenre name: %s\n";
    private static final String ENTER_GENRE_NAME_FOR_SEARCH = "Enter the genre you look for: ";
    private static final String UPDATE_GENRE = "Enter a new genre: ";
    private static final String REASON = "which data you want to work with?";
    private static final String GENRES = "genres";
    private String login;
    private final GenreService genreService;
    private final IOService ioService;
    private final LongParser parserService;


    public GenreShellCommands(GenreService genreService, IOService ioService, LongParser parserService) {
        this.genreService = genreService;
        this.ioService = ioService;
        this.parserService = parserService;
    }

    @ShellMethod(value = "method to login", key = "genres")
    public void loginToWorkWithGenres() {
        login = GENRES;
    }

    @ShellMethod(value = "save genre", key = "save genre")
    public void saveGenre() {
        ioService.out(ENTER_GENRE_NAME);
        Genre genre = genreService.save(genreService.parseString(ioService.readString()));
        ioService.out(String.format(SAVED_GENRE_INFO, genre.getId()));
    }

    @ShellMethod(value = "find genre by id", key = "find genre by id")
    public void findGenreById() {
        ioService.out(ENTER_GENRE_ID);
        long id = parserService.parse(ioService.readString());
        Genre genre = genreService.findById(id);
        ioService.out(String.format(GENRE_DATA, genre.getId(), genre.getGenreName()));
    }

    @ShellMethod(value = "find all genres", key = "find all genres")
    public void findAllGenres() {
        List<Genre> genreList = genreService.findAll();
        genreList.forEach(genre -> ioService.out(String.format(GENRE_DATA, genre.getId(), genre.getGenreName())));
    }

    @ShellMethod(value = "find genres by name", key = "find genres by name")
    public void findGenresByName() {
        ioService.out(ENTER_GENRE_NAME_FOR_SEARCH);
        List<Genre> genreList = genreService.findByName(ioService.readString());
        genreList.forEach(genre -> ioService.out(String.format(GENRE_DATA, genre.getId(), genre.getGenreName())));
    }

    @ShellMethod(value = "update genre by id", key = "update genre by id")
    public void updateGenreById() {
        ioService.out(UPDATE_GENRE);
        Genre genre = genreService.parseString(ioService.readString());
        ioService.out(ENTER_GENRE_ID);
        long id = parserService.parse(ioService.readString());
        genreService.updateById(genre, id);
    }

    @ShellMethod(value = "delete genre by id", key = "delete genre by id")
    public void deleteGenreById() {
        ioService.out(ENTER_GENRE_ID);
        genreService.deleteById(parserService.parse(ioService.readString()));
    }

    @ShellMethodAvailability(value = {"saveGenre", "findGenreById", "findAllGenres", "findGenresByName", "updateGenreById", "deleteGenreById"})
    public Availability workWithGenres() {
        return login.equals(GENRES) ? Availability.available() : Availability.unavailable(REASON);
    }
}
