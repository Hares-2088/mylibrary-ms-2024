package com.mylibrary.reservations.dataacess;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private ReservationIdentifier reservationIdentifier;

    private Date reservationDate;

    public Reservation(ReservationIdentifier reservationIdentifier, Date reservationDate) {
        this.reservationIdentifier = reservationIdentifier;
        this.reservationDate = reservationDate;
    }
}
