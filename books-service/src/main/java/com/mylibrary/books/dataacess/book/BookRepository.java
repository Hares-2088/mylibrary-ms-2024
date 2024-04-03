package com.mylibrary.books.dataacess.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByBookIdentifier_BookId(String bookId);
    Book findByAuthorIdentifier_AuthorIdAndBookIdentifier_BookId(String authorId, String bookId);

    List<Book> findAllByAuthorIdentifier_AuthorId(String authorId);
}
