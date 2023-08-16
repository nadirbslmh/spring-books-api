package com.example.booksapi.controller;

import com.example.booksapi.model.Book;
import com.example.booksapi.model.BookRequest;
import com.example.booksapi.model.Response;
import com.example.booksapi.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookControllerTests {

    @Mock
    private BookService service;

    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookController = new BookController(service);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "Book 1", "Description 1", "Publisher 1", "Author 1"));
        books.add(new Book(2, "Book 2", "Description 2", "Publisher 2", "Author 2"));

        when(service.getAll()).thenReturn(new Response<>("all books", books));

        ResponseEntity<Response<List<Book>>> responseEntity = bookController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("all books", responseEntity.getBody().getMessage());
        assertEquals(2, responseEntity.getBody().getData().size());
    }

    @Test
    void testGetBookByIdFound() {
        Book book = new Book(1, "Book 1", "Description 1", "Publisher 1", "Author 1");

        when(service.getById(1)).thenReturn(new Response<>("book found", book));

        ResponseEntity<Response<Book>> responseEntity = bookController.getById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("book found", responseEntity.getBody().getMessage());
        assertEquals(book, responseEntity.getBody().getData());
    }

    @Test
    void testGetBookByIdNotFound() {
        when(service.getById(1)).thenReturn(new Response<>("book not found", null));

        ResponseEntity<Response<Book>> responseEntity = bookController.getById(1);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("book not found", responseEntity.getBody().getMessage());
    }

    @Test
    void testCreateBook() {
        BookRequest request = new BookRequest("New Book", "New Description", "New Publisher", "New Author");
        Book createdBook = new Book(1, "New Book", "New Description", "New Publisher", "New Author");

        when(service.create(request)).thenReturn(new Response<>("book created", createdBook));

        BindingResult bindingResult = mock(BindingResult.class);

        ResponseEntity<Response<Book>> responseEntity = bookController.create(request, bindingResult);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("book created", responseEntity.getBody().getMessage());
        assertEquals(createdBook, responseEntity.getBody().getData());
    }

    @Test
    void testCreateBookInvalidRequest() {
        BookRequest request = new BookRequest("", "", "","");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Response<Book>> responseEntity = bookController.create(request, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateBookFound() {
        BookRequest request = new BookRequest("Updated Book", "Updated Description", "Updated Publisher", "Updated Author");
        Book updatedBook = new Book(1, "Updated Book", "Updated Description", "Updated Publisher", "Updated Author");

        when(service.update(request, 1)).thenReturn(new Response<>("book updated", updatedBook));

        BindingResult bindingResult = mock(BindingResult.class);

        ResponseEntity<Response<Book>> responseEntity = bookController.update(1, request, bindingResult);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("book updated", responseEntity.getBody().getMessage());
        assertEquals(updatedBook, responseEntity.getBody().getData());
    }

    @Test
    void testUpdateBookInvalidRequest() {
        BookRequest request = new BookRequest("", "", "","");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Response<Book>> responseEntity = bookController.update(1, request, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateBookNotFound() {
        BookRequest request = new BookRequest("Updated Book", "Updated Description", "Updated Publisher", "Updated Author");

        when(service.update(request, 1)).thenReturn(new Response<>("book not found", null));

        BindingResult bindingResult = mock(BindingResult.class);

        ResponseEntity<Response<Book>> responseEntity = bookController.update(1, request, bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("book not found", responseEntity.getBody().getMessage());
    }

    @Test
    void testDeleteBookFound() {
        when(service.delete(1)).thenReturn(true);

        ResponseEntity<Response<Object>> responseEntity = bookController.delete(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("book deleted", responseEntity.getBody().getMessage());
    }

    @Test
    void testDeleteBookNotFound() {
        when(service.delete(1)).thenReturn(false);

        ResponseEntity<Response<Object>> responseEntity = bookController.delete(1);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("book not found", responseEntity.getBody().getMessage());
    }
}
