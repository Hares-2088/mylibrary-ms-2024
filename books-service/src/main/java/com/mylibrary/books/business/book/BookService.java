package com.mylibrary.books.business.book;



import com.mylibrary.books.presentation.book.BookRequestModel;
import com.mylibrary.books.presentation.book.BookResponseModel;

import java.util.List;

public interface BookService {

    List<BookResponseModel> getBooks();
    BookResponseModel getBook(String bookId);
    BookResponseModel createBook(BookRequestModel bookRequestModel);
    BookResponseModel updateBook(String bookId, BookRequestModel bookRequestModel);
    void deleteBook(String bookId);

}
