package com.example.shell;

import com.example.model.Author;
import com.example.service.AuthorService;
import com.example.service.IOService;
import com.example.service.LongParser;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class AuthorShellCommands {

    private static final String ENTER_AUTHOR = "Enter author first name and last name\nexample: Vasya Ivanov\n";
    private static final String SAVED_AUTHOR_INFO = "Author have been saved with id(%d)";
    private static final String ENTER_AUTHOR_ID = "Enter author id: ";
    private static final String AUTHOR_DATA = "\nid: %d\nauthor first name: %s\nauthor last name: %s\n";
    private static final String ENTER_AUTHOR_NAME = "Enter the author you look for: ";
    private static final String UPDATE_AUTHOR = "Enter a new author: ";
    private static final String AUTHORS = "authors";
    private static final String REASON = "which data you want to work with?";
    private final AuthorService authorService;
    private final IOService ioService;
    private final LongParser parserService;
    private String login;


    public AuthorShellCommands(AuthorService authorService, IOService ioService, LongParser parserService) {
        this.authorService = authorService;
        this.ioService = ioService;
        this.parserService = parserService;
    }

    @ShellMethod(value = "method to login", key = "authors")
    public void loginToWorkWithAuthors() {
        login = AUTHORS;
    }


    @ShellMethod(value = "save author", key = "save")
    public void saveAuthor() {
        ioService.out(ENTER_AUTHOR);
        Author author = authorService.save(authorService.parseString(ioService.readString()));
        ioService.out(String.format(SAVED_AUTHOR_INFO, author.getId()));
    }

    @ShellMethod(value = "find author by id", key = "find by id")
    public void findAuthorById() {
        ioService.out(ENTER_AUTHOR_ID);
        long id = parserService.parse(ioService.readString());
        Author author = authorService.findById(id);
        ioService.out(String.format(AUTHOR_DATA, author.getId(), author.getFirstName(), author.getLastName()));
    }

    @ShellMethod(value = "find all authors", key = "find all")
    public void findAllAuthors() {
        List<Author> authorList = authorService.findAll();
        authorList.forEach(author -> ioService.out(String.format(AUTHOR_DATA, author.getId(), author.getFirstName(), author.getLastName())));
    }

    @ShellMethod(value = "find authors by name", key = "find by name")
    public void findAuthorsByName() {
        ioService.out(ENTER_AUTHOR_NAME );
        List<Author> authorList = authorService.findByName(ioService.readString());
        authorList.forEach(author -> ioService.out(String.format(AUTHOR_DATA, author.getId(), author.getFirstName(), author.getLastName())));
    }

    @ShellMethod(value = "update author by id", key = "update by id")
    public void updateAuthorById() {
        ioService.out(UPDATE_AUTHOR);
        Author author = authorService.parseString(ioService.readString());
        ioService.out(ENTER_AUTHOR_ID);
        long id = parserService.parse(ioService.readString());
        authorService.updateById(author, id);
    }
    @ShellMethod(value = "delete author by id", key = "delete by id")
    public void deleteAuthorById() {
        ioService.out(ENTER_AUTHOR_ID);
        authorService.deleteById(parserService.parse(ioService.readString()));
    }

    @ShellMethodAvailability(value = {"saveAuthor", "findAuthorById", "findAllAuthors", "findAuthorsByName", "updateAuthorById", "deleteAuthorById"})
    public Availability workWithAuthors() {
        return login.equals(AUTHORS) ? Availability.available() : Availability.unavailable(REASON);
    }
}
