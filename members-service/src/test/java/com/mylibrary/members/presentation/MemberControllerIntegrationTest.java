package com.mylibrary.members.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylibrary.members.dataacess.MemberRepository;
import com.mylibrary.members.dataacess.MemberType;
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
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberControllerIntegrationTest {

    private final String BASE_URI_MEMBERS = "api/v1/members";
    private final String FOUND_MEMBER_ID = "m1";
    private final String NOT_FOUND_MEMBER_ID = "m0";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockRestServiceServer;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    public void whenGetMembers_thenReturnAllMembers() {
        //arrange
        Long sizeDB = memberRepository.count();

        // act & assert
        webTestClient.get()
                .uri(BASE_URI_MEMBERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(MemberResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() == sizeDB);
                });
    }

    @Test
    public void whenNoMemberExists_thenReturnEmptyList() {
        //arrange
        memberRepository.deleteAll();

        // assert
        webTestClient.get()
                .uri(BASE_URI_MEMBERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(MemberResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.isEmpty());
                });
    }

    @Test
    public void whenGetMember_thenReturnMember() {
        // act & assert
        webTestClient.get()
                .uri(BASE_URI_MEMBERS + "/" + FOUND_MEMBER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MemberResponseModel.class)
                .value((memberResponseModel) -> {
                    assertNotNull(memberResponseModel);
                    assertEquals("Adem", memberResponseModel.getFirstName());
                    assertEquals("Doe", memberResponseModel.getLastName());
                });
    }

    @Test
    public void whenGetMemberDoesNotExist_thenReturnNotFound() {
        // act & assert
        webTestClient.get()
                .uri(BASE_URI_MEMBERS + "/" + NOT_FOUND_MEMBER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown memberId: " + NOT_FOUND_MEMBER_ID);
    }

    @Test
    public void whenValidMember_thenCreateMember() {
        //arrange
        long sizeDB = memberRepository.count();
        MemberRequestModel memberRequestModel = new MemberRequestModel("John", "Doe", "email", "benefits",
                MemberType.SENIOR, "street", "city", "Quebec", "country");

        webTestClient.post()
                .uri(BASE_URI_MEMBERS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(memberRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MemberResponseModel.class)
                .value((memberResponseModel) -> {
                    assertNotNull(memberResponseModel);
                    assertEquals(memberRequestModel.getFirstName(), memberResponseModel.getFirstName());
                    assertEquals(memberRequestModel.getLastName(), memberResponseModel.getLastName());
                    assertEquals(memberRequestModel.getEmail(), memberResponseModel.getEmail());
                    assertEquals(memberRequestModel.getBenefits(), memberResponseModel.getBenefits());
                    assertEquals(memberRequestModel.getStreet(), memberResponseModel.getStreet());
                    assertEquals(memberRequestModel.getCity(), memberResponseModel.getCity());
                    assertEquals(memberRequestModel.getProvince(), memberResponseModel.getProvince());
                    assertEquals(memberRequestModel.getCountry(), memberResponseModel.getCountry());
                });
        long sizeDBAfter = memberRepository.count();
        assertEquals(sizeDB + 1, sizeDBAfter);
    }

    @Test
    public void whenNotValidMemberAddressOnAdd_thenReturnBadRequest() {
        //arrange
        MemberRequestModel memberRequestModel = new MemberRequestModel("John", "Doe", "email", "benefits",
                MemberType.SENIOR, "street", "city", "Ontario", "country");

        // act & assert
        webTestClient.post()
                .uri(BASE_URI_MEMBERS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(memberRequestModel)
                .exchange()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("Member must be from Quebec to be accepted in this library.");
    }

    @Test
    public void whenNotValidMemberAddressOnUpdate_thenReturnBadRequest() {
        //arrange
        MemberRequestModel memberRequestModel = new MemberRequestModel("John", "Doe", "email", "benefits",
                MemberType.SENIOR, "street", "city", "Ontario", "country");

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_MEMBERS + "/" + FOUND_MEMBER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(memberRequestModel)
                .exchange()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("UNPROCESSABLE_ENTITY")
                .jsonPath("$.message").isEqualTo("Member must be from Quebec to be accepted in this library.");
    }

    @Test
    public void whenMemberDoesNotExistOnUpdate_thenReturnNotFound() {
        // Arrange
        MemberRequestModel memberRequestModel = new MemberRequestModel("John", "Doe", "email", "benefits",
                MemberType.SENIOR, "street", "city", "Quebec", "country");

        // Perform a PUT or PATCH request to update a league that does not exist
        webTestClient.put()
                .uri(BASE_URI_MEMBERS + "/" + NOT_FOUND_MEMBER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(memberRequestModel) // Set the request body with the updated league data
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown memberId: " + NOT_FOUND_MEMBER_ID);
    }

    @Test
    public void whenValidMember_thenUpdateMember(){
        //arrange
        MemberRequestModel memberRequestModel = new MemberRequestModel("John", "Doe", "email", "benefits",
                MemberType.SENIOR, "street", "city", "Quebec", "country");

        // act & assert
        webTestClient.put()
                .uri(BASE_URI_MEMBERS + "/" + FOUND_MEMBER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(memberRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MemberResponseModel.class)
                .value((memberResponseModel) -> {
                    assertNotNull(memberResponseModel);
                    assertEquals(memberRequestModel.getFirstName(), memberResponseModel.getFirstName());
                    assertEquals(memberRequestModel.getLastName(), memberResponseModel.getLastName());
                    assertEquals(memberRequestModel.getEmail(), memberResponseModel.getEmail());
                    assertEquals(memberRequestModel.getBenefits(), memberResponseModel.getBenefits());
                    assertEquals(memberRequestModel.getStreet(), memberResponseModel.getStreet());
                    assertEquals(memberRequestModel.getCity(), memberResponseModel.getCity());
                    assertEquals(memberRequestModel.getProvince(), memberResponseModel.getProvince());
                    assertEquals(memberRequestModel.getCountry(), memberResponseModel.getCountry());
                });
    }

    @Test
    public void whenMemberExist_thenDeleteMember() {

        // Mock the response of reservationServiceClient
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://reservations-service:7003/api/v1/members/" + FOUND_MEMBER_ID + "/reservations/" + "r1"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        // Mock the response of loanServiceClient
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://loans-service:7001/api/v1/members/" + FOUND_MEMBER_ID + "/loans"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        // Act: Delete the member
        webTestClient.delete()
                .uri(BASE_URI_MEMBERS + "/" + FOUND_MEMBER_ID)
                .exchange()
                .expectStatus().isNoContent();

        // Assert: Verify the member is deleted
        assertNull(memberRepository.findByMemberIdentifier_MemberId(FOUND_MEMBER_ID));
    }

    @Test
    public void whenMemberDoesNotExistOnDelete_thenReturnNotFound() {
        // Perform a DELETE request to delete a league that does not exist
        webTestClient.delete()
                .uri(BASE_URI_MEMBERS + "/" + NOT_FOUND_MEMBER_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown memberId: " + NOT_FOUND_MEMBER_ID);
    }



}