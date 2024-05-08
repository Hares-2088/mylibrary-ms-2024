package com.mylibrary.business;



import com.mylibrary.presentation.BookRequestModel;
import com.mylibrary.presentation.BookResponseModel;

import java.util.List;

public interface BookService {

    List<BookResponseModel> getBooks();
    BookResponseModel getBook(String bookId);
    BookResponseModel createBook(BookRequestModel bookRequestModel);
    BookResponseModel updateBook(String bookId, BookRequestModel bookRequestModel);
    BookResponseModel reduceBookAvailability(String bookId);
    BookResponseModel increaseBookAvailability(String bookId);
    void deleteBook(String bookId);

}
