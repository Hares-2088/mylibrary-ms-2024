package com.mylibrary.members.presentation;

import com.mylibrary.members.dataacess.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestModel {

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
