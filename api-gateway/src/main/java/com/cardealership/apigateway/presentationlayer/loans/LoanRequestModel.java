package com.cardealership.apigateway.presentationlayer.loans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequestModel {

    private String bookId;

    private Boolean returned;
}
