package com.mylibrary.reservations.dataacess;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

//    Reservation findByReservationIdentifier_ReservationId(String reservationId);

    Reservation findByReservationIdentifier_ReservationIdAndMemberId(String reservationId, String memberId);
}
