package com.mylibrary.reservations.presentation;

import lombok.*;


import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponseModel{

    private String reservationId;

    private String bookId;

    private LocalDate reservationDate;
}
