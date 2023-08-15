package com.example.booksapi.service;

import com.example.booksapi.model.Book;
import com.example.booksapi.model.BookRequest;
import com.example.booksapi.model.Response;
import com.example.booksapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;

    public Response<List<Book>> getAll() {
        List<Book> books = repository.findAll();

        return new Response<>(
                "all books",
                books
        );
    }

    public Response<Book> getById(Integer id) {
        Optional<Book> bookData = repository.findById(id);

        return bookData.map(book -> new Response<>(
                "book found",
                book
        )).orElseGet(() -> new Response<>(
                "book not found",
                null
        ));
    }

    public Response<Book> create(BookRequest request) {
        Book bookData = Book.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .publisher(request.getPublisher())
                .author(request.getAuthor())
                .build();

        Book book = repository.save(bookData);

        return new Response<>(
                "book created",
                book
        );
    }

    public Response<Book> update(BookRequest request, Integer id) {
        Optional<Book> bookData = repository.findById(id);

        if (bookData.isEmpty()) {
            return new Response<>(
                    "book not found",
                    null
            );
        }

        Book updatedBook = bookData.get();

        updatedBook.setTitle(request.getTitle());
        updatedBook.setDescription(request.getDescription());
        updatedBook.setPublisher(request.getPublisher());
        updatedBook.setAuthor(request.getAuthor());

        Book book = repository.save(updatedBook);

        return new Response<>(
                "book updated",
                book
        );
    }

    public boolean delete(Integer id) {
        Optional<Book> bookData = repository.findById(id);

        if (bookData.isEmpty()) {
            return false;
        }

        repository.delete(bookData.get());

        return true;
    }

}
