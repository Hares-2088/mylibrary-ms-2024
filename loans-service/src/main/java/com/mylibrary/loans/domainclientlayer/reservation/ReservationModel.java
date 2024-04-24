package com.mylibrary.loans.domainclientlayer.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
public class ReservationModel {

    String reservationId;
    String bookId;
    String memberId;
    LocalDate reservationDate;
}
