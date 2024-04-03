package com.mylibrary.books.business.author;



import com.mylibrary.books.presentation.author.AuthorRequestModel;
import com.mylibrary.books.presentation.author.AuthorResponseModel;
import com.mylibrary.books.presentation.book.BookResponseModel;

import java.util.List;

public interface AuthorService {

    List<AuthorResponseModel> getAuthors();
    List<BookResponseModel> getBooksByAuthor(String authorId);
    BookResponseModel getBookByAuthor(String authorId, String bookId);
    AuthorResponseModel addAuthor(AuthorRequestModel authorRequestModel);
    AuthorResponseModel getAuthor(String authorId);
    AuthorResponseModel updateAuthor(String authorId, AuthorRequestModel authorRequestModel);
    void deleteAuthor(String authorId);
}
