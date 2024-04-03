package com.mylibrary.members.presentation;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberResponseModel {

    private String memberId;

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
