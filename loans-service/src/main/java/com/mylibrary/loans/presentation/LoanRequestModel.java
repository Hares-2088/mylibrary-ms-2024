package com.mylibrary.loans.presentation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoanRequestModel {

    private String bookId;

    private Boolean returned;
}
