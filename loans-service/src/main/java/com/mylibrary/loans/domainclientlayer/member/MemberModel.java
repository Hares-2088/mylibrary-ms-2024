package com.mylibrary.loans.domainclientlayer.member;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
public class MemberModel {

    String memberId;

    String memberFirstName;

    String memberLastName;

    String reservationId;
}
