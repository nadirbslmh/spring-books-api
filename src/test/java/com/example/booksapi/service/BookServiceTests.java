package com.example.booksapi.service;

import com.example.booksapi.model.Book;
import com.example.booksapi.model.BookRequest;
import com.example.booksapi.model.Response;
import com.example.booksapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTests {

    @Mock
    private BookRepository repository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(repository);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "Book 1", "Description 1", "Publisher 1", "Author 1"));
        books.add(new Book(2, "Book 2", "Description 2", "Publisher 2", "Author 2"));

        when(repository.findAll()).thenReturn(books);

        Response<List<Book>> response = bookService.getAll();

        assertEquals("all books", response.getMessage());
        assertEquals(2, response.getData().size());
    }

    @Test
    void testGetBookByIdFound() {
        Book book = new Book(1, "Book 1", "Description 1", "Publisher 1", "Author 1");

        when(repository.findById(1)).thenReturn(Optional.of(book));

        Response<Book> response = bookService.getById(1);

        assertEquals("book found", response.getMessage());
        assertEquals(book, response.getData());
    }

    @Test
    void testGetBookByIdNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Response<Book> response = bookService.getById(1);

        assertEquals("book not found", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testCreateBook() {
        BookRequest request = new BookRequest("New Book", "New Description", "New Publisher", "New Author");
        Book createdBook = new Book(1, "New Book", "New Description", "New Publisher", "New Author");

        when(repository.save(any(Book.class))).thenReturn(createdBook);

        Response<Book> response = bookService.create(request);

        assertEquals("book created", response.getMessage());
        assertEquals(createdBook, response.getData());
    }

    @Test
    void testUpdateBookFound() {
        BookRequest request = new BookRequest("Updated Book", "Updated Description", "Updated Publisher", "Updated Author");
        Book existingBook = new Book(1, "Existing Book", "Description", "Publisher", "Author");

        when(repository.findById(1)).thenReturn(Optional.of(existingBook));
        when(repository.save(any(Book.class))).thenReturn(existingBook);

        Response<Book> response = bookService.update(request, 1);

        assertEquals("book updated", response.getMessage());
        assertEquals(existingBook, response.getData());
        assertEquals("Updated Book", existingBook.getTitle());
        assertEquals("Updated Description", existingBook.getDescription());
        assertEquals("Updated Author", existingBook.getAuthor());
        assertEquals("Updated Publisher", existingBook.getPublisher());
    }

    @Test
    void testUpdateBookNotFound() {
        BookRequest request = new BookRequest("Updated Book", "Updated Description", "Updated  Publisher", "Updated Author");

        when(repository.findById(1)).thenReturn(Optional.empty());

        Response<Book> response = bookService.update(request, 1);

        assertEquals("book not found", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testDeleteBookFound() {
        when(repository.findById(1)).thenReturn(Optional.of(new Book()));

        boolean result = bookService.delete(1);

        assertTrue(result);
        verify(repository, times(1)).delete(any(Book.class));
    }

    @Test
    void testDeleteBookNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        boolean result = bookService.delete(1);

        assertFalse(result);
        verify(repository, never()).delete(any(Book.class));
    }
}
