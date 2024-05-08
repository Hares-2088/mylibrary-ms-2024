package com.cardealership.apigateway.presentationlayer.reservations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequestModel {

    private String bookId;

    private String memberId;

    private LocalDate reservationDate;
}
