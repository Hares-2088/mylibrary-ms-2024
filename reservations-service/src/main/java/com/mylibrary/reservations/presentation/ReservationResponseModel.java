package com.mylibrary.reservations.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseModel{

    private String reservationId;

//    private BookResponseModel book;
//
//    private MemberResponseModel member;

    private Date reservationDate;
}
