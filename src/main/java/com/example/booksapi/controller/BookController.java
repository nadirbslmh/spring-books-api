package com.example.booksapi.controller;

import com.example.booksapi.model.Book;
import com.example.booksapi.model.BookRequest;
import com.example.booksapi.model.Response;
import com.example.booksapi.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping
    public ResponseEntity<Response<List<Book>>> getAll() {
        Response<List<Book>> response = service.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Book>> getById(@PathVariable("id") Integer id) {
        Response<Book> response = service.getById(id);

        if (response.getData() == null) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<Book>> create(@Valid @RequestBody BookRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Response<>(bindingResult.getFieldErrors().toString(), null), HttpStatus.BAD_REQUEST);
        }

        Response<Book> response = service.create(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Book>> update(@PathVariable("id") Integer id, @Valid @RequestBody BookRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Response<>(bindingResult.getFieldErrors().toString(), null), HttpStatus.BAD_REQUEST);
        }

        Response<Book> response = service.update(request, id);

        if (response.getData() == null) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Object>> delete(@PathVariable("id") Integer id) {
        boolean isDeleted = service.delete(id);

        if (!isDeleted) {
            Response<Object> response = new Response<>("book not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(new Response<>("book deleted", null));
    }
}
