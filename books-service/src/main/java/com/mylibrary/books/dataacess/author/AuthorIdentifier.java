package com.mylibrary.books.dataacess.author;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
public class AuthorIdentifier {
    private String authorId;

    public AuthorIdentifier() {
        this.authorId = java.util.UUID.randomUUID().toString();
    }
}
