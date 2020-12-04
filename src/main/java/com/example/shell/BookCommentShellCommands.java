package com.example.shell;

import com.example.model.BookComment;
import com.example.service.BookCommentService;
import com.example.service.IOService;
import com.example.service.LongParser;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class BookCommentShellCommands {
    private static final String ENTER_BOOK_COMMENT = "Enter book id and book comment\nexample: 2, i haven't read this\n";
    private static final String SAVED_BOOK_COMMENT_INFO = "Book comment have been saved with id(%d) and comment(%s)";
    private static final String ENTER_BOOK_COMMENT_ID = "Enter book comment id: ";
    private static final String BOOK_COMMENT_DATA = "id: %d\ncomment: %s\n";
    private static final String ENTER_COMMENT = "Enter the comment you look for: ";
    private static final String UPDATE_COMMENT = "Enter a new book comment : ";
    private static final String REASON = "which data you want to work with?";
    private static final String BOOK_COMMENTS = "book comments";
    private String login;

    private final IOService ioService;
    private final LongParser parserService;
    private final BookCommentService bookCommentService;

    public BookCommentShellCommands(IOService ioService, BookCommentService bookCommentService, LongParser longParser) {
        this.ioService = ioService;
        this.parserService = longParser;
        this.bookCommentService = bookCommentService;
    }

    @ShellMethod(value = "method to login", key = "book comments")
    public void loginToWorkWithBookComments() {
        login = BOOK_COMMENTS;
    }

    @ShellMethod(value = "save object", key = "save book comment")
    public void saveBookComment() {
        ioService.out(ENTER_BOOK_COMMENT);
        BookComment bookComment = bookCommentService.parseString(ioService.readString());
        ioService.out(String.format(SAVED_BOOK_COMMENT_INFO, bookComment.getId(), bookComment.getComment()));
    }

    @ShellMethod(value = "find book comment by id", key = "find book comment by id")
    public void findBookCommentById() {
        ioService.out(ENTER_BOOK_COMMENT_ID);
        BookComment bookComment = bookCommentService.findById(parserService.parse(ioService.readString()));
        ioService.out(String.format(BOOK_COMMENT_DATA, bookComment.getId(), bookComment.getComment()));
    }

    @ShellMethod(value = "get all book comments", key = "find all book comments")
    public void findAllBookComments() {
        List<BookComment> bookComments = bookCommentService.findAll();
        bookComments.forEach(comment -> ioService.out(String.format(BOOK_COMMENT_DATA, comment.getId(), comment.getComment())));
    }

    @ShellMethod(value = "get all comments with specific content", key = "find by name book comments")
    public void findBookCommentsByName() {
        ioService.out(ENTER_COMMENT);
        List<BookComment> bookComments = bookCommentService.findByName(ioService.readString().trim());
        bookComments.forEach(comment -> ioService.out(String.format(BOOK_COMMENT_DATA, comment.getId(), comment.getComment())));
    }

    @ShellMethod(value = "update book comment by id", key = "update book comment by id")
    public void updateBookCommentById() {
        ioService.out(UPDATE_COMMENT);
        BookComment comment = bookCommentService.parseString(ioService.readString());
        ioService.out(ENTER_BOOK_COMMENT_ID);
        long id = parserService.parse(ioService.readString());
        bookCommentService.updateById(comment, id);
    }

    @ShellMethod(value = "delete book comment by id", key = "delete book comment by id")
    public void deleteBookCommentById() {
        ioService.out(ENTER_BOOK_COMMENT_ID);
        long id = parserService.parse(ioService.readString());
        bookCommentService.deleteById(id);
    }

    @ShellMethodAvailability(value = {"saveBookComment", "findBookCommentById", "findAllBookComments",
            "findBookCommentsByName", "updateBookCommentById", "deleteBookCommentById"})
    public Availability workWithBookComments() {
        return login.equals(BOOK_COMMENTS) ? Availability.available() : Availability.unavailable(REASON);
    }
}
