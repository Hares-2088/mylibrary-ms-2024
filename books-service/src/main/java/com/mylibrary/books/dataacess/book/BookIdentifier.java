package com.mylibrary.books.dataacess.book;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
public class BookIdentifier {

    private String bookId;

    public BookIdentifier() {
        this.bookId = java.util.UUID.randomUUID().toString();
    }
}
