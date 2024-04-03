package com.mylibrary.reservations.dataacess;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
public class ReservationIdentifier {

    private String reservationId;
    public ReservationIdentifier() {
        this.reservationId = UUID.randomUUID().toString();
    }
}
