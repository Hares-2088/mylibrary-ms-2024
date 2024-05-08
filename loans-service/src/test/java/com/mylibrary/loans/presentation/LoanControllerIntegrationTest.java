package com.mylibrary.loans.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylibrary.loans.domainclientlayer.book.BookServiceClient;
import com.mylibrary.loans.domainclientlayer.member.MemberModel;
import com.mylibrary.loans.domainclientlayer.member.MemberServiceClient;
import com.mylibrary.loans.domainclientlayer.reservation.ReservationServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class LoanControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    private MemberServiceClient memberServiceClient;

    @MockBean
    private BookServiceClient bookServiceClient;

    @MockBean
    private ReservationServiceClient reservationServiceClient;

    @BeforeEach
    void init() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    private MockRestServiceServer mockRestServiceServer;
    private ObjectMapper mapper = new ObjectMapper();

    private final String BASE_URI_Loans = "api/v1/members/";
    private final String FOUND_MEMBER = "m1";
    private final String FOUND_LOAN = "l1";

}