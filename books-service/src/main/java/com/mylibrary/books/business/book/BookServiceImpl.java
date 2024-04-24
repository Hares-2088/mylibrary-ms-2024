package com.mylibrary.books.business.book;


import com.mylibrary.books.dataacess.author.Author;
import com.mylibrary.books.dataacess.author.AuthorIdentifier;
import com.mylibrary.books.dataacess.author.AuthorRepository;
import com.mylibrary.books.dataacess.book.Book;
import com.mylibrary.books.dataacess.book.BookIdentifier;
import com.mylibrary.books.dataacess.book.BookRepository;
import com.mylibrary.books.mapper.book.BookRequestMapper;
import com.mylibrary.books.mapper.book.BookResponseMapper;
import com.mylibrary.books.presentation.book.BookRequestModel;
import com.mylibrary.books.presentation.book.BookResponseModel;
import com.mylibrary.books.utils.exceptions.InvalidPublicationYear;
import com.mylibrary.books.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookResponseMapper bookResponseMapper;
    private final BookRequestMapper bookRequestMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, BookResponseMapper bookResponseMapper, BookRequestMapper bookRequestMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookResponseMapper = bookResponseMapper;
        this.bookRequestMapper = bookRequestMapper;
    }

    @Override
    public List<BookResponseModel> getBooks() {
        List<Book> books = bookRepository.findAll();
        return bookResponseMapper.entityListToResponseModelList(books);
    }

    @Override
    public BookResponseModel getBook(String bookId) {
        Book book = bookRepository.findByBookIdentifier_BookId(bookId);

        if (book == null) {
            throw new NotFoundException("Unknown bookId: " + bookId);
        }

        return bookResponseMapper.entityToResponseModel(book);
    }

    @Override
    public BookResponseModel createBook(BookRequestModel bookRequestModel) {
        Author author = authorRepository.findByAuthorIdentifier_AuthorId(bookRequestModel.getAuthorId());
        if (author == null) {
            throw new NotFoundException("Unknown authorId: " + bookRequestModel.getAuthorId());
        }

        // Check if the year of publication is in the future and throw an exception if it is
        if (bookRequestModel.getPublicationYear().after(new Date())) {
            throw new InvalidPublicationYear("Year of publication cannot be in the future");
        }

        Book book = bookRequestMapper.requestModelToEntity(bookRequestModel, author.getAuthorIdentifier() ,new BookIdentifier() );

        return bookResponseMapper.entityToResponseModel(bookRepository.save(book));
    }

    @Override
    public BookResponseModel updateBook(String bookId, BookRequestModel bookRequestModel) {

        Author author = authorRepository.findByAuthorIdentifier_AuthorId(bookRequestModel.getAuthorId());

        if (author == null) {
            throw new NotFoundException("Unknown authorId: " + bookRequestModel.getAuthorId());
        }

        Book foundBook = bookRepository.findByBookIdentifier_BookId(bookId);

        if (foundBook == null) {
            throw new NotFoundException("Unknown bookId: " + bookId);
        }

        // Check if the year of publication is in the future and throw an exception if it is
        if (bookRequestModel.getPublicationYear().after(new Date())) {
            throw new InvalidPublicationYear("Year of publication cannot be in the future");
        }

        Book book = bookRequestMapper.requestModelToEntity(bookRequestModel, author.getAuthorIdentifier()  ,foundBook.getBookIdentifier());
        book.setId(foundBook.getId());

        return bookResponseMapper.entityToResponseModel(bookRepository.save(book));
    }

    @Override
    public void deleteBook(String bookId) {
        Book book = bookRepository.findByBookIdentifier_BookId(bookId);

        if (book == null) {
            throw new NotFoundException("Unknown bookId: " + bookId);
        }

        bookRepository.delete(book);
    }
}
