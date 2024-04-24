package com.mylibrary.reservations.presentation;

import com.mylibrary.reservations.dataacess.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ManagerReservationControllerIntegrationTest {

    private final String BASE_URI_RESERVATIONS = "api/v1/reservations";
    private final String NOT_FOUND_RESERVATION_ID = "r0";
    private final String FOUND_RESERVATION_ID = "r1";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void whenGetReservations_thenReturnAllReservations() {
        //arrange
        Long sizeDB = reservationRepository.count();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_RESERVATIONS)
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
                .uri(BASE_URI_RESERVATIONS)
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
        //arrange
        String reservationId = "r1";

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_RESERVATIONS + "/" + reservationId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReservationResponseModel.class)
                .value((reservation) -> {
                    assertNotNull(reservation);
                    assertEquals(reservationId, reservation.getReservationId());
                });
    }

    @Test
    public void whenGetReservationByReservationId_thenNotFound() {
        // act & assert
        webTestClient.get()
                .uri(BASE_URI_RESERVATIONS + "/" + NOT_FOUND_RESERVATION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID);
    }

    @Test
    public void whenUpdateReservation_thenUpdatedReservation() {
        //arrange
        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2024, 12, 31));

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_RESERVATIONS + "/" + FOUND_RESERVATION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReservationResponseModel.class)
                .value((reservation) -> {
                    assertNotNull(reservation);
                    assertEquals(reservationRequestModel.getReservationDate(), reservation.getReservationDate());
                });
    }

    @Test
    public void whenValidReservation_thenAddReservation() {
        //arrange
        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2024, 12, 31));

        // act & assert
        webTestClient.post()
                .uri(BASE_URI_RESERVATIONS)
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
    public void whenInvalidReservation_thenBadRequest() {
        //arrange
        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2021, 12, 31));

        // act & assert
        webTestClient.post()
                .uri(BASE_URI_RESERVATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationRequestModel)
                .exchange()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("This date is in the past: " + reservationRequestModel.getReservationDate());
    }

    @Test
    public void whenReservationDoesNotExistOnUpdate_thenNotFound() {
        //arrange
        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2024, 12, 31));

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_RESERVATIONS + "/" + NOT_FOUND_RESERVATION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationRequestModel)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID);
    }

    @Test
    public void whenInvalidReservationOnUpdate_thenUnprocessableEntity() {
        //arrange

        ReservationRequestModel reservationRequestModel = new ReservationRequestModel();
        reservationRequestModel.setReservationDate(LocalDate.of(2021, 12, 31));

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_RESERVATIONS + "/" + FOUND_RESERVATION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationRequestModel)
                .exchange()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("This date is in the past: " + reservationRequestModel.getReservationDate());
    }

    @Test
    public void whenDeleteReservation_thenNoContent() {
        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_RESERVATIONS + "/" + FOUND_RESERVATION_ID)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void whenReservationDoesNotExistOnDelete_thenNotFound() {
        // act & assert
        webTestClient.delete()
                .uri(BASE_URI_RESERVATIONS + "/" + NOT_FOUND_RESERVATION_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID);
    }


}