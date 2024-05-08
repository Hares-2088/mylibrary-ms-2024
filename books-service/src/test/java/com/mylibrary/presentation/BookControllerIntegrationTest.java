package com.mylibrary.presentation;

import com.mylibrary.dataacess.Book;
import com.mylibrary.dataacess.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookControllerIntegrationTest {

    private final String FOUND_BOOK_ID = "b1";
    private final String NOT_FOUND_BOOK_ID = "b0";
    private final String BASE_URL = "api/v1/books";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenGetBooks_thenReturnBooks(){
        //arrange
        Long sizeDB = bookRepository.count();

        //act
        webTestClient.get()
                .uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookResponseModel.class)
                .value(bookResponseModels -> {
                    assertNotNull(bookResponseModels);
                    assertEquals(sizeDB, (long) bookResponseModels.size());
                });
    }

    @Test
    public void whenNoBookExists_thenReturnEmptyList(){
        //arrange
        bookRepository.deleteAll();

        //act
        webTestClient.get()
                .uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookResponseModel.class)
                .value(bookResponseModels -> {
                    assertNotNull(bookResponseModels);
                    assertTrue(bookResponseModels.isEmpty());
                });
    }

    @Test
    public void whenBookExists_thenReturnBook(){
        //act
        webTestClient.get()
                .uri(BASE_URL + "/" + FOUND_BOOK_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value(bookResponseModelResponse -> {
                    assertNotNull(bookResponseModelResponse);
                    assertEquals("Sample Book 1", bookResponseModelResponse.getTitle());
                });
    }

    @Test
    public void whenBookDoesNotExist_thenReturnNotFound(){
        //act
        webTestClient.get()
                .uri(BASE_URL + "/" + NOT_FOUND_BOOK_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown bookId: " + NOT_FOUND_BOOK_ID);

    }

    @Test
    public void whenCreateBook_thenReturnBook(){
        //arrange
        BookRequestModel bookRequestModel = new BookRequestModel();
        bookRequestModel.setTitle("Sample Book 2");
        bookRequestModel.setAuthorName("Author 1");
        bookRequestModel.setPublicationYear(new Date());

        //act
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value(bookResponseModelResponse -> {
                    assertNotNull(bookResponseModelResponse);
                    assertEquals("Sample Book 2", bookResponseModelResponse.getTitle());
                    assertEquals("Author 1", bookResponseModelResponse.getAuthorName());
                });
    }


    @Test
    public void whenUpdateBook_thenReturnBook(){
        //arrange
        BookRequestModel bookRequestModel = new BookRequestModel();
        bookRequestModel.setTitle("Sample Book 2");
        bookRequestModel.setAuthorName("a1");
        bookRequestModel.setPublicationYear(new Date());

        //act
        webTestClient.put()
                .uri(BASE_URL + "/" + FOUND_BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value(bookResponseModelResponse -> {
                    assertNotNull(bookResponseModelResponse);
                    assertEquals("Sample Book 2", bookResponseModelResponse.getTitle());
                    assertEquals("a1", bookResponseModelResponse.getAuthorName());
                });
    }

    @Test
    public void whenNotValidPublicationYearOnUpdate_thenThrowInvalidPublicationYear(){
        //arrange
        BookRequestModel bookRequestModel = new BookRequestModel();
        bookRequestModel.setTitle("Sample Book 2");
        bookRequestModel.setAuthorName("a1");
        bookRequestModel.setPublicationYear(new Date(3000, 1, 1));

        //act
        webTestClient.put()
                .uri(BASE_URL + "/" + FOUND_BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("Year of publication cannot be in the future");
    }

    @Test
    public void whenNotValidPublicationYear_thenThrowInvalidPublicationYear(){
        //arrange
        BookRequestModel bookRequestModel = new BookRequestModel();
        bookRequestModel.setTitle("Sample Book 2");
        bookRequestModel.setAuthorName("a1");
        bookRequestModel.setPublicationYear(new Date(3000, 1, 1));

        //act
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookRequestModel)
                .exchange()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("Year of publication cannot be in the future");
    }
    @Test
    public void whenDeleteBook_thenNoContent(){
        //act
        webTestClient.delete()
                .uri(BASE_URL + "/" + FOUND_BOOK_ID)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void whenBookDoesNotExistOnDelete_thenThrowNotFound(){
        //act
        webTestClient.delete()
                .uri(BASE_URL + "/" + NOT_FOUND_BOOK_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown bookId: " + NOT_FOUND_BOOK_ID);
    }

    //test the reduceAvailableCopies method
    @Test
    public void whenReduceAvailableCopies_thenReduceAvailableCopies(){
        //arrange
        Book book = bookRepository.findByBookIdentifier_BookId(FOUND_BOOK_ID);
        int availableCopies = book.getAvailableCopies();

        //act
        webTestClient.patch()
                .uri(BASE_URL + "/" + book.getBookIdentifier().getBookId()+ "/reduceAvailability" )
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value(bookResponseModelResponse -> {
                    assertNotNull(bookResponseModelResponse);
                    assertEquals(availableCopies - 1, bookResponseModelResponse.getAvailableCopies());
                });
    }

    //test the increaseAvailableCopies method
    @Test
    public void whenIncreaseAvailableCopies_thenIncreaseAvailableCopies(){
        //arrange
        Book book = bookRepository.findByBookIdentifier_BookId(FOUND_BOOK_ID);
        int availableCopies = book.getAvailableCopies();

        //act
        webTestClient.patch()
                .uri(BASE_URL + "/" + book.getBookIdentifier().getBookId()+ "/increaseAvailability" )
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookResponseModel.class)
                .value(bookResponseModelResponse -> {
                    assertNotNull(bookResponseModelResponse);
                    assertEquals(availableCopies + 1, bookResponseModelResponse.getAvailableCopies());
                });
    }


}