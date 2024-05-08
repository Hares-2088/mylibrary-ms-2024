package com.mylibrary.presentation;

import com.mylibrary.business.BookService;
import com.mylibrary.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = BookController.class)
class BookControllerUnitTest {

    private final String FOUND_BOOK_ID = "b1";
    private final String NOT_FOUND_BOOK_ID = "b0";

    @Autowired
    BookController bookController;

    @MockBean
    private BookService bookService;

    @Test
    public void whenNoBookExists_thenReturnEmptyList() {
        //arrange
        when(bookService.getBooks()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<BookResponseModel>> responseEntityList = bookController.getBooks();

        //assert
        assertNotNull(responseEntityList);
        assertEquals(HttpStatus.OK, responseEntityList.getStatusCode());
        assertTrue(responseEntityList.getBody().isEmpty());
        verify(bookService, times(1)).getBooks();
    }

    @Test
    public void whenBooksExists_thenReturnBooks(){
        //arrange
        List<BookResponseModel> bookResponseModels = Collections.singletonList(buildBookResponseModel());
        when(bookService.getBooks()).thenReturn(bookResponseModels);

        //act
        ResponseEntity<List<BookResponseModel>> responseEntityList = bookController.getBooks();

        //assert
        assertNotNull(responseEntityList);
        assertEquals(HttpStatus.OK, responseEntityList.getStatusCode());
        assertFalse(responseEntityList.getBody().isEmpty());
        assertEquals(bookResponseModels, responseEntityList.getBody());
        verify(bookService, times(1)).getBooks();
    }

    @Test
    public void whenBookExists_thenReturnBook(){

        BookRequestModel bookRequestModel = buildBookRequestModel();
        BookResponseModel bookResponseModel = buildBookResponseModel();

        //arrange
        when(bookService.createBook(bookRequestModel)).thenReturn(bookResponseModel);

        //act
        ResponseEntity<BookResponseModel> responseEntity = bookController.createBook(bookRequestModel);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(bookResponseModel, responseEntity.getBody());
        verify(bookService, times(1)).createBook(bookRequestModel);
    }

    @Test
    public void whenBookExist_thenReturnBook() {
        // Arrange
        BookResponseModel bookResponseModel = buildBookResponseModel();
        when(bookService.getBook(FOUND_BOOK_ID)).thenReturn(bookResponseModel);

        // Act
        ResponseEntity<BookResponseModel> responseEntity = bookController.getBook(FOUND_BOOK_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookResponseModel, responseEntity.getBody());
        verify(bookService, times(1)).getBook(FOUND_BOOK_ID);
    }

    @Test
    public void whenBookDoesNotExist_thenThrowNotFoundException() {
        // Arrange
        when(bookService.getBook(NOT_FOUND_BOOK_ID)).thenThrow(new NotFoundException());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> bookController.getBook(NOT_FOUND_BOOK_ID));
        verify(bookService, times(1)).getBook(NOT_FOUND_BOOK_ID);
    }

    @Test
    public void whenBookExist_thenDeleteBook(){
        // Arrange
        doNothing().when(bookService).deleteBook(FOUND_BOOK_ID);

        // Act
        ResponseEntity<BookResponseModel> responseEntity = bookController.deleteBook(FOUND_BOOK_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(bookService, times(1)).deleteBook(FOUND_BOOK_ID);
    }

    @Test
    public void whenBookDoesNotExistOnDelete_thenThrowNotFoundException() {
        // Arrange
        doThrow(NotFoundException.class).when(bookService).deleteBook(NOT_FOUND_BOOK_ID);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> bookController.deleteBook(NOT_FOUND_BOOK_ID));
        verify(bookService, times(1)).deleteBook(NOT_FOUND_BOOK_ID);
    }

    @Test
    public void whenBookExist_thenUpdateBook(){
        // Arrange
        BookRequestModel bookRequestModel = buildBookRequestModel();
        BookResponseModel bookResponseModel = buildBookResponseModel();
        when(bookService.updateBook(FOUND_BOOK_ID, bookRequestModel)).thenReturn(bookResponseModel);

        // Act
        ResponseEntity<BookResponseModel> responseEntity = bookController.updateBook(bookRequestModel, FOUND_BOOK_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookResponseModel, responseEntity.getBody());
        verify(bookService, times(1)).updateBook(FOUND_BOOK_ID, bookRequestModel);
    }

    @Test
    public void whenBookDoesNotExistOnUpdate_thenThrowNotFoundException() {
        // Arrange
        BookRequestModel bookRequestModel = buildBookRequestModel();
        doThrow(NotFoundException.class).when(bookService).updateBook(NOT_FOUND_BOOK_ID, bookRequestModel);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> bookController.updateBook(bookRequestModel, NOT_FOUND_BOOK_ID));
        verify(bookService, times(1)).updateBook(NOT_FOUND_BOOK_ID, bookRequestModel);
    }







    //The following are the two builders used in the tests above
    private BookRequestModel buildBookRequestModel() {
        return BookRequestModel.builder()
                .authorName("authorId")
                .title("title")
                .publicationYear(new Date())
                .genre("genre")
                .description("description")
                .availableCopies(5)
                .build();
    }

    private BookResponseModel buildBookResponseModel() {
        Date date = new Date();
        return BookResponseModel.builder()
                .bookId("id")
                .authorName("authorId")
                .title("title")
                .publicationYear(date)
                .genre("genre")
                .description("description")
                .availableCopies(5)
                .build();
    }
}