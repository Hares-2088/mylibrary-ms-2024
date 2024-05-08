package com.cardealership.apigateway.presentationlayer.reservations;

import com.cardealership.apigateway.presentationlayer.members.MemberResponseModel;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponseModel extends RepresentationModel<ReservationResponseModel> {

    private String reservationId;

    private String bookId;

    private LocalDate reservationDate;

    private String memberId;
}
