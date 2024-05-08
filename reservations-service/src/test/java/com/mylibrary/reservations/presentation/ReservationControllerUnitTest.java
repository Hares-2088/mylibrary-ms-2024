package com.mylibrary.reservations.presentation;

import com.mylibrary.reservations.business.ReservationService;
import com.mylibrary.reservations.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = ReservationController.class)
class ReservationControllerUnitTest {

    private final String FOUND_RESERVATION_ID = "r1";
    private final String FOUND_MEMBER_ID = "m1";
    private final String NOT_FOUND_RESERVATION_ID = "r1000";

    @Autowired
    private ReservationController reservationController;

    @MockBean
    private ReservationService reservationService;

    @Test
    public void whenNoReservationExists_thenReturnEmptyList() {
        //arrange
        when(reservationService.getAllReservations()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<ReservationResponseModel>> responseEntityList = reservationController.getAllReservations();

        //assert
        assertNotNull(responseEntityList);
        assertEquals(HttpStatus.OK, responseEntityList.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntityList.getBody()).isEmpty());
        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    public void whenReservationsExist_thenReturnAllReservations() {
        ReservationResponseModel reservationResponseModel = buildReservationResponseModel();

        //arrange
        when(reservationService.getAllReservations()).thenReturn(Collections.singletonList(reservationResponseModel));

        //act
        ResponseEntity<List<ReservationResponseModel>> responseEntityList = reservationController.getAllReservations();

        //assert
        assertNotNull(responseEntityList);
        assertEquals(HttpStatus.OK, responseEntityList.getStatusCode());
        assertFalse(Objects.requireNonNull(responseEntityList.getBody()).isEmpty());
        assertEquals(1, responseEntityList.getBody().size());
        assertEquals(reservationResponseModel, responseEntityList.getBody().get(0));
        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    public void whenReservationExists_thenReturnReservation() {
        ReservationResponseModel reservationResponseModel = buildReservationResponseModel();

        //arrange
        when(reservationService.getReservationByReservationId(FOUND_RESERVATION_ID,FOUND_MEMBER_ID)).thenReturn(reservationResponseModel);

        //act
        ResponseEntity<ReservationResponseModel> responseEntity = reservationController.getReservationByReservationId(FOUND_RESERVATION_ID, FOUND_MEMBER_ID);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reservationResponseModel, responseEntity.getBody());
        verify(reservationService, times(1)).getReservationByReservationId(FOUND_RESERVATION_ID, FOUND_MEMBER_ID);
    }

    @Test
    public void whenReservationDoesNotExist_thenReturnNotFound() {
        //arrange
        when(reservationService.getReservationByReservationId(NOT_FOUND_RESERVATION_ID,FOUND_MEMBER_ID)).thenThrow(new NotFoundException("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID));

        //act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> reservationController.getReservationByReservationId(NOT_FOUND_RESERVATION_ID, FOUND_MEMBER_ID));

        //assert
        assertEquals("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID, exception.getMessage());
        verify(reservationService, times(1)).getReservationByReservationId(NOT_FOUND_RESERVATION_ID, FOUND_MEMBER_ID);
    }

    @Test
    public void whenReservationExistsOnCreate_thenReturnReservation() {
        ReservationRequestModel reservationRequestModel = buildReservationRequestModel();
        ReservationResponseModel reservationResponseModel = buildReservationResponseModel();

        //arrange
        when(reservationService.createReservation(reservationRequestModel, FOUND_MEMBER_ID)).thenReturn(reservationResponseModel);

        //act
        ResponseEntity<ReservationResponseModel> responseEntity = reservationController.createReservation(reservationRequestModel, FOUND_MEMBER_ID);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(reservationResponseModel, responseEntity.getBody());
        verify(reservationService, times(1)).createReservation(reservationRequestModel, FOUND_MEMBER_ID);
    }

    @Test
    public void whenReservationDoesNotExistOnCreate_thenReturnNotFound() {
        ReservationRequestModel reservationRequestModel = buildReservationRequestModel();

        //arrange
        when(reservationService.createReservation(reservationRequestModel, FOUND_MEMBER_ID)).thenThrow(new NotFoundException("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID));

        //act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> reservationController.createReservation(reservationRequestModel, FOUND_MEMBER_ID));
        //assert
        assertEquals("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID, exception.getMessage());
        verify(reservationService, times(1)).createReservation(reservationRequestModel, FOUND_MEMBER_ID);
    }

    @Test
    public void whenReservationExistsOnUpdate_thenReturnUpdatedReservation() {
        ReservationRequestModel reservationRequestModel = buildReservationRequestModel();
        ReservationResponseModel reservationResponseModel = buildReservationResponseModel();

        //arrange
        when(reservationService.updateReservation(reservationRequestModel, FOUND_RESERVATION_ID, FOUND_MEMBER_ID)).thenReturn(reservationResponseModel);

        //act
        ResponseEntity<ReservationResponseModel> responseEntity = reservationController.updateReservation(reservationRequestModel, FOUND_RESERVATION_ID, FOUND_MEMBER_ID);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reservationResponseModel, responseEntity.getBody());
        verify(reservationService, times(1)).updateReservation(reservationRequestModel, FOUND_RESERVATION_ID, FOUND_MEMBER_ID);
    }

    @Test
    public void whenReservationDoesNotExistOnUpdate_thenReturnNotFound() {
        ReservationRequestModel reservationRequestModel = buildReservationRequestModel();

        //arrange
        when(reservationService.updateReservation(reservationRequestModel, NOT_FOUND_RESERVATION_ID, FOUND_MEMBER_ID)).thenThrow(new NotFoundException("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID));

        //act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> reservationController.updateReservation(reservationRequestModel, NOT_FOUND_RESERVATION_ID, FOUND_MEMBER_ID));

        //assert
        assertEquals("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID, exception.getMessage());
        verify(reservationService, times(1)).updateReservation(reservationRequestModel, NOT_FOUND_RESERVATION_ID, FOUND_MEMBER_ID);
    }

    @Test
    public void whenReservationExistsOnDelete_thenReturnNoContent() {
        //arrange
        doNothing().when(reservationService).deleteReservation(FOUND_RESERVATION_ID, FOUND_MEMBER_ID);

        //act
        ResponseEntity<Void> responseEntity = reservationController.deleteReservation(FOUND_RESERVATION_ID, FOUND_MEMBER_ID);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(reservationService, times(1)).deleteReservation(FOUND_RESERVATION_ID, FOUND_MEMBER_ID);
    }

    @Test
    public void whenReservationDoesNotExistOnDelete_thenReturnNotFound() {

        // Ensure that the reservationService mock behaves as expected
        doThrow(new NotFoundException("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID))
                .when(reservationService)
                .deleteReservation(NOT_FOUND_RESERVATION_ID, "m1");

        // Act and Assert
        // Ensure that the deleteReservation method throws NotFoundException when the reservation does not exist
        NotFoundException exception = assertThrows(NotFoundException.class, () -> reservationController.deleteReservation(NOT_FOUND_RESERVATION_ID, FOUND_MEMBER_ID));

        // Assert
        assertEquals("Unknown reservationId: " + NOT_FOUND_RESERVATION_ID, exception.getMessage());
        verify(reservationService, times(1)).deleteReservation(NOT_FOUND_RESERVATION_ID, FOUND_MEMBER_ID);
    }



    //here are the builders for the models
    private ReservationRequestModel buildReservationRequestModel() {
        return ReservationRequestModel.builder()
                .reservationDate(LocalDate.now())
                .build();
    }

    private ReservationResponseModel buildReservationResponseModel() {
        return ReservationResponseModel.builder()
                .reservationId("r1")
                .reservationDate(LocalDate.now())
                .build();
    }
}