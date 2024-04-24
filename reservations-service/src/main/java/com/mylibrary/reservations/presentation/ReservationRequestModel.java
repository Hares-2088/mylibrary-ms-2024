package com.mylibrary.reservations.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequestModel {

    private String bookId;

    private String memberId;

    private LocalDate reservationDate;
}
