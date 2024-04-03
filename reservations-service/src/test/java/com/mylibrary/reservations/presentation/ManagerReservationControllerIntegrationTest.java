package com.mylibrary.reservations.presentation;

import com.mylibrary.reservations.dataacess.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2.")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ManagerReservationControllerIntegrationTest {

    private final String BASE_URI_RESERVATIONS = "api/v1/reservations";
    private final String NOT_FOUND_CUSTOMER_ID = "r100";

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
}