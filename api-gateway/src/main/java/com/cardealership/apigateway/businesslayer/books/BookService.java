package com.cardealership.apigateway.businesslayer.books;


import com.cardealership.apigateway.presentationlayer.books.BookRequestModel;
import com.cardealership.apigateway.presentationlayer.books.BookResponseModel;

import java.util.List;

public interface BookService {

    List<BookResponseModel> getAllBooks();
    BookResponseModel getBookByBookId(String bookId);
    BookResponseModel createBook(BookRequestModel bookRequestModel);
    void updateBook(String bookId, BookRequestModel bookRequestModel);
    void deleteBook(String bookId);
}
