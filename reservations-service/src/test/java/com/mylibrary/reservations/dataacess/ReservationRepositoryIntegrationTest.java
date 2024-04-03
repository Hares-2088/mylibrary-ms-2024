package com.mylibrary.reservations.dataacess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.AfterMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

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

        Date date = new Date(2005, 5, 5);
        //arrange
        Reservation reservation = new Reservation(new ReservationIdentifier(), date);
        reservationRepository.save(reservation);

        //act
        Reservation savedReservation = reservationRepository.findByReservationIdentifier_ReservationId(reservation.getReservationIdentifier().getReservationId());

        //assert
        assertNotNull(savedReservation);
        assertEquals(reservation.getReservationIdentifier(), savedReservation.getReservationIdentifier());
    }
}