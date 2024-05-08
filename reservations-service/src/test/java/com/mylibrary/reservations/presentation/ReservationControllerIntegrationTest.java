package com.mylibrary.reservations.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylibrary.reservations.dataacess.Reservation;
import com.mylibrary.reservations.dataacess.ReservationRepository;
import com.mylibrary.reservations.domainclientlayer.BookModel;
import com.mylibrary.reservations.domainclientlayer.MemberModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;


import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationControllerIntegrationTest {

    private final String BASE_URI_RESERVATIONS = "api/v1/members/";
    private final String NOT_FOUND_RESERVATION_ID = "r0";
    private final String FOUND_RESERVATION_ID = "/reservations/r1";
    private final String MEMBER_ID = "m1";
    private final String BOOK_ID = "b1";


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReservationRepository reservationRepository;

    private MockRestServiceServer mockRestServiceServer;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    RestTemplate restTemplate;

    @BeforeEach
    void init() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void whenGetReservations_thenReturnAllReservations() {
        //arrange
        Long sizeDB = reservationRepository.count();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_RESERVATIONS + MEMBER_ID + "/reservations")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ReservationResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() == sizeDB);
                });
    }

    @Test
    public void whenNoReservationExists_thenReturnEmptyList() {
        //arrange
        reservationRepository.deleteAll();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_RESERVATIONS + MEMBER_ID + "/reservations")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ReservationResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.isEmpty());
                });
    }

    @Test
    public void whenGetReservationByReservationId_thenReturnReservation() {

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_RESERVATIONS + MEMBER_ID + FOUND_RESERVATION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReservationResponseModel.class)
                .value((reservation) -> {
                    assertNotNull(reservation);
                    assertEquals("r1", reservation.getReservationId());
                });
    }

    @Test
    public void whenGetReservationByReservationId_thenNotFound() {
        // act & assert
        webTestClient.get()
                .uri(BASE_URI_RESERVATIONS + "/m2" + FOUND_RESERVATION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND");
    }

    @Test
    public void whenUpdateReservation_thenUpdatedReservation() throws JsonProcessingException {
        //arrange
        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2024, 12, 31));
        reservationRequestModel.setMemberId(MEMBER_ID);
        reservationRequestModel.setBookId(BOOK_ID);

        var bookModel = BookModel.builder()
                .bookId(BOOK_ID)
                .availableCopies(1)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo("http://books-service:7001/api/v1/books/" + BOOK_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bookModel))
                );

        Reservation reservation = reservationRepository.findByReservationIdentifier_ReservationIdAndMemberId("r1", MEMBER_ID);

        assertNotNull(reservation);
        // act & assert
        webTestClient.put()
                .uri(BASE_URI_RESERVATIONS + MEMBER_ID + FOUND_RESERVATION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReservationResponseModel.class)
                .value((response) -> {
                    assertNotNull(response);
                    assertEquals(reservationRequestModel.getReservationDate(), response.getReservationDate());
                });
    }

    @Test
    public void whenValidReservation_thenAddReservation() throws JsonProcessingException {
        //arrange
        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2024, 12, 31));
        reservationRequestModel.setMemberId(MEMBER_ID);
        reservationRequestModel.setBookId(BOOK_ID);

        var bookModel = BookModel.builder()
                .bookId(BOOK_ID)
                .availableCopies(1)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://books-service:7001/api/v1/books/" + BOOK_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bookModel))
                );

        var memberModel = MemberModel.builder()
                .memberId(MEMBER_ID)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://members-service:7002/api/v1/members/" + MEMBER_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(memberModel))
                );

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://books-service:7001/api/v1/books/b1/reduceAvailability" ))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bookModel))
                );

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(startsWith("http://members-service:7002/api/v1/members/m1/addReservation/")))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(memberModel))
                );

        // act & assert
        webTestClient.post()
                .uri(BASE_URI_RESERVATIONS + MEMBER_ID + "/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReservationResponseModel.class)
                .value((reservation) -> {
                    assertNotNull(reservation);
                    assertEquals(reservationRequestModel.getReservationDate(), reservation.getReservationDate());
                });
    }

    @Test
    public void whenInvalidReservation_thenBadRequest() throws JsonProcessingException {
        //arrange
        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2021, 12, 31));
        reservationRequestModel.setMemberId(MEMBER_ID);
        reservationRequestModel.setBookId(BOOK_ID);

        var bookModel = BookModel.builder()
                .bookId(BOOK_ID)
                .availableCopies(1)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://books-service:7001/api/v1/books/" + BOOK_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bookModel))
                );

        var memberModel = MemberModel.builder()
                .memberId(MEMBER_ID)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://members-service:7002/api/v1/members/" + MEMBER_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(memberModel))
                );

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://books-service:7001/api/v1/books/b1/reduceAvailability" ))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bookModel))
                );

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(startsWith("http://members-service:7002/api/v1/members/m1/addReservation/")))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(memberModel))
                );

        // act & assert
        webTestClient.post()
                .uri(BASE_URI_RESERVATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationRequestModel)
                .exchange()
                .expectBody()
                .jsonPath("$.httpStatus");
    }

    @Test
    public void whenReservationDoesNotExistOnUpdate_thenNotFound() throws JsonProcessingException {
        //arrange
        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2024, 12, 31));
        reservationRequestModel.setMemberId(MEMBER_ID);
        reservationRequestModel.setBookId(BOOK_ID);

        var bookModel = BookModel.builder()
                .bookId(BOOK_ID)
                .availableCopies(1)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://books-service:7001/api/v1/books/" + BOOK_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bookModel))
                );

        var memberModel = MemberModel.builder()
                .memberId(MEMBER_ID)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://members-service:7002/api/v1/members/" + MEMBER_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(memberModel))
                );

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://books-service:7001/api/v1/books/b1/reduceAvailability" ))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bookModel))
                );

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(startsWith("http://members-service:7002/api/v1/members/m1/addReservation/")))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(memberModel))
                );

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_RESERVATIONS + "/" + NOT_FOUND_RESERVATION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationRequestModel)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenInvalidReservationOnUpdate_thenUnprocessableEntity() {
        //arrange

        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2021, 12, 31));

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_RESERVATIONS + "m1" + FOUND_RESERVATION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationRequestModel)
                .exchange()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("This date is in the past: " + reservationRequestModel.getReservationDate());
    }

    @Test
    public void whenDeleteReservation_thenNoContent() throws JsonProcessingException {

        var bookModel = BookModel.builder()
                .bookId(BOOK_ID)
                .availableCopies(1)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://books-service:7001/api/v1/books/b1/increaseAvailability" ))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bookModel))
                );
        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_RESERVATIONS + "m1" + FOUND_RESERVATION_ID)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void whenReservationDoesNotExistOnDelete_thenNotFound() {
        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_RESERVATIONS + "m1" + NOT_FOUND_RESERVATION_ID)
                .exchange()
                .expectStatus().isNotFound();
    }


}