package com.cardealership.apigateway.presentationlayer.members;

import com.cardealership.apigateway.domainclientlayer.member.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestModel {

    private String reservationId;

    private String firstName;

    private String lastName;

    private String email;

    private String benefits;

    private MemberType memberType;

    private String street;

    private String city;

    private String province;

    private String country;
}
