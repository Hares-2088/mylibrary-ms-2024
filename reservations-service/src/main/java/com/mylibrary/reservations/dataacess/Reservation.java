package com.mylibrary.reservations.dataacess;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
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

    private String memberId;

    private String bookId;

    private LocalDate reservationDate;

    public Reservation(LocalDate reservationDate) {
        this.reservationIdentifier = new ReservationIdentifier();
        this.reservationDate = reservationDate;
    }
}
