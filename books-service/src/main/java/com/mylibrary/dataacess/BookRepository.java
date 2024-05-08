package com.mylibrary.dataacess;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByBookIdentifier_BookId(String bookId);
}
