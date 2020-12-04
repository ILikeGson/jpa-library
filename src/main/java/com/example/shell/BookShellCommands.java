package com.example.shell;

import java.util.List;
import com.example.model.Book;
import com.example.service.BookServiceImpl;
import com.example.service.LongParser;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import com.example.service.IOService;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class BookShellCommands {

    private static final String ENTER_BOOK_ID = "Enter book id: ";
    private static final String ENTER_BOOK_INFO_TO_SAVE = "Enter book info. " +
            "EXAMPLE: \"title\", \"author\", \"genre\", \"comment1\", \"comment2\"....\n";
    private static final String BOOK_ID = "Book's id: %d\n";
    private static final String PRINT_ALL_BOOK_INFO = "Title: %s\nAuthor: %s\nGenre: %s\nComments: %s\n";
    private static final String BOOK_INFO_TO_UPDATE = "Enter book info. \n" +
            "EXAMPLE: \"title\", \"author\", \"genre\", \"comment1\", \"comment2\"...etc";
    private static final String ENTER_THE_BOOK_NAME = "Enter the book name: ";
    private static final String REASON = "which data you want to work with?\n";
    private static final String BOOK_TITLE = "Book title: %s\n";
    private static final String BOOKS = "books";
    private String login;

    private final IOService consoleIOService;
    private final BookServiceImpl bookService;
    private final LongParser parserService;

    public BookShellCommands(IOService consoleIOService, BookServiceImpl bookService, LongParser parserService) {
        this.consoleIOService = consoleIOService;
        this.bookService = bookService;
        this.parserService = parserService;
    }

    @ShellMethod(value = "method to login", key = "books")
    public void loginToWorkWithBooks() {
        login = BOOKS;
    }

    @ShellMethod(value = "Insert command", key = "save book")
    public void saveBook() {
        consoleIOService.out(ENTER_BOOK_INFO_TO_SAVE);
        String book = consoleIOService.readString();
        consoleIOService.out(String.format(BOOK_ID, bookService.save(bookService.parseString(book)).getId()));
    }

    @ShellMethod(value = "Get by id command", key = "find book by id")
    public void findBookById() {
        consoleIOService.out(ENTER_BOOK_ID);
        long id = Long.parseLong(consoleIOService.readString());
        Book book = bookService.findById(id);
        consoleIOService.out(String.format(BOOK_TITLE, book.getTitle()));
    }

    @ShellMethod(value = "Get by name", key = "find books by name")
    public void findBooksByName() {
        consoleIOService.out(ENTER_THE_BOOK_NAME);
        List<Book> books = bookService.findByName(consoleIOService.readString());
        books.forEach(book -> consoleIOService.out(String.format(BOOK_TITLE, book.getTitle())));
    }

    @ShellMethod(value = "Get all command", key = "find all books")
    public void findAllBooks() {
        bookService.findAll().forEach(book -> consoleIOService.out(String.format(PRINT_ALL_BOOK_INFO,
                book.getTitle(), book.getAuthor(), book.getGenre(), book.getComments())));
    }

    @ShellMethod(value = "Update by id command", key = "update book by id")
    public void updateBookById() {
        consoleIOService.out(BOOK_INFO_TO_UPDATE);
        String book = consoleIOService.readString();
        consoleIOService.out(ENTER_BOOK_ID);
        long id = parserService.parse(consoleIOService.readString());
        bookService.updateById(bookService.parseString(book), id);
    }

    @ShellMethod(value = "delete by id command", key = "delete book by id")
    public void deleteBookById() {
        consoleIOService.out(ENTER_BOOK_ID);
        long id = parserService.parse(consoleIOService.readString());
        bookService.deleteById(id);
    }

    @ShellMethodAvailability(value = {"saveBook", "findBookById", "findAllBooks", "findBooksByName", "updateBookById", "deleteBookById"})
    public Availability workWithBooks() {
        return login.equals(BOOKS) ? Availability.available() : Availability.unavailable(REASON);
    }
}
