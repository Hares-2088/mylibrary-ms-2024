package com.mylibrary.books.dataacess.book;

import com.mylibrary.books.dataacess.author.AuthorIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setupDb(){
        bookRepository.deleteAll();
    }

    @Test
    public void whenBookExists_ReturnBookByBookId() {
        //arrange
        Book book = new Book("Intro to programming", new AuthorIdentifier(), new Date(), "Programming", "1234", 5);
        bookRepository.save(book);

        //act
        Book savedBook = bookRepository.findByBookIdentifier_BookId(book.getBookIdentifier().getBookId());

        //assert
        assertNotNull(savedBook);
        assertEquals(book.getBookIdentifier(), savedBook.getBookIdentifier());
        assertEquals(book.getTitle(), savedBook.getTitle());
        assertEquals(book.getGenre(), savedBook.getGenre());
    }

    @Test
    public void whenBookDoesNotExist_ReturnNull() {
        //act
        Book savedBook = bookRepository.findByBookIdentifier_BookId("1234");

        //assert
        assertNull(savedBook);
    }

}