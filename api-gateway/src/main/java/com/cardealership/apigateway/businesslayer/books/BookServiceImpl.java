package com.cardealership.apigateway.businesslayer.books;

import com.cardealership.apigateway.domainclientlayer.book.BookServiceClient;
import com.cardealership.apigateway.mapper.BookResponseMapper;
import com.cardealership.apigateway.presentationlayer.books.BookRequestModel;
import com.cardealership.apigateway.presentationlayer.books.BookResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    private final BookServiceClient bookServiceClient;
    private final BookResponseMapper bookResponseMapper;

    public BookServiceImpl(BookServiceClient bookServiceClient, BookResponseMapper bookResponseMapper) {
        this.bookServiceClient = bookServiceClient;
        this.bookResponseMapper = bookResponseMapper;
    }

    @Override
    public List<BookResponseModel> getAllBooks() {
        return bookResponseMapper.responseModelListToResponseModelList(bookServiceClient.getAllBooks());
    }

    @Override
    public BookResponseModel getBookByBookId(String bookId) {
        return bookResponseMapper.responseModelToResponseModel(bookServiceClient.getBookByBookId(bookId));
    }

    @Override
    public BookResponseModel createBook(BookRequestModel bookRequestModel) {
        return bookResponseMapper.responseModelToResponseModel(bookServiceClient.createBook(bookRequestModel));
    }

    @Override
    public void updateBook(String bookId, BookRequestModel bookRequestModel) {
        bookServiceClient.updateBook(bookId, bookRequestModel);
    }

    @Override
    public void deleteBook(String bookId) {
        bookServiceClient.deleteBook(bookId);
    }
}
