package com.example.booksapi.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private String message;
    private T data;
}
