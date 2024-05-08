package com.mylibrary.business;

import com.mylibrary.dataacess.Book;
import com.mylibrary.dataacess.BookIdentifier;
import com.mylibrary.dataacess.BookRepository;
import com.mylibrary.mapper.BookRequestMapper;
import com.mylibrary.mapper.BookResponseMapper;
import com.mylibrary.presentation.BookRequestModel;
import com.mylibrary.presentation.BookResponseModel;
import com.mylibrary.utils.exceptions.InvalidPublicationYear;
import com.mylibrary.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookResponseMapper bookResponseMapper;
    private final BookRequestMapper bookRequestMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookResponseMapper bookResponseMapper, BookRequestMapper bookRequestMapper) {
        this.bookRepository = bookRepository;
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
        // Check if the year of publication is in the future and throw an exception if it is
        if (bookRequestModel.getPublicationYear().after(new Date())) {
            throw new InvalidPublicationYear("Year of publication cannot be in the future");
        }

        Book book = bookRequestMapper.requestModelToEntity(bookRequestModel,new BookIdentifier());

        return bookResponseMapper.entityToResponseModel(bookRepository.save(book));
    }

    @Override
    public BookResponseModel updateBook(String bookId, BookRequestModel bookRequestModel) {

        Book foundBook = bookRepository.findByBookIdentifier_BookId(bookId);

        if (foundBook == null) {
            throw new NotFoundException("Unknown bookId: " + bookId);
        }

        // Check if the year of publication is in the future and throw an exception if it is
        if (bookRequestModel.getPublicationYear().after(new Date())) {
            throw new InvalidPublicationYear("Year of publication cannot be in the future");
        }

        Book book = bookRequestMapper.requestModelToEntity(bookRequestModel, foundBook.getBookIdentifier());
        book.setId(foundBook.getId());

        return bookResponseMapper.entityToResponseModel(bookRepository.save(book));
    }

    @Override
    public BookResponseModel reduceBookAvailability(String bookId) {
        Book book = bookRepository.findByBookIdentifier_BookId(bookId);

        if (book == null) {
            throw new NotFoundException("Unknown bookId: " + bookId);
        }

        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }

        return bookResponseMapper.entityToResponseModel(bookRepository.save(book));
    }

    @Override
    public BookResponseModel increaseBookAvailability(String bookId) {
        Book book = bookRepository.findByBookIdentifier_BookId(bookId);

        if (book == null) {
            throw new NotFoundException("Unknown bookId: " + bookId);
        }

        book.setAvailableCopies(book.getAvailableCopies() + 1);

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
