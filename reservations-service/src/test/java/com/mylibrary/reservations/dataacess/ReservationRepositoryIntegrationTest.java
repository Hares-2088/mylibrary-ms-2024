package com.mylibrary.reservations.dataacess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReservationRepositoryIntegrationTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    public void setupDb(){
        reservationRepository.deleteAll();
    }

    @Test
    public void whenReservationExists_ReturnReservationByReservationId() {

        //arrange
        Reservation reservation = new Reservation(LocalDate.now());
        reservation.setMemberId("m1");
        reservationRepository.save(reservation);

        //act
        Reservation savedReservation = reservationRepository.findByReservationIdentifier_ReservationIdAndMemberId(reservation.getReservationIdentifier().getReservationId(), reservation.getMemberId());

        //assert
        assertNotNull(savedReservation);
        assertEquals(reservation.getReservationIdentifier(), savedReservation.getReservationIdentifier());
    }

    @Test
    public void whenReservationDoesNotExist_ReturnNull() {
        //act
        Reservation savedReservation = reservationRepository.findByReservationIdentifier_ReservationIdAndMemberId("1234", "1234");

        //assert
        assertNull(savedReservation);
    }
}