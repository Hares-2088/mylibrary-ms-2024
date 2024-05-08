package com.cardealership.apigateway.presentationlayer.members;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberResponseModel extends RepresentationModel<MemberResponseModel> {

    private String memberId;

    private String reservationId;

    private String firstName;

    private String lastName;

    private String email;

    private String benefits;

    private String memberType;

    private String street;

    private String city;

    private String province;

    private String country;
}
