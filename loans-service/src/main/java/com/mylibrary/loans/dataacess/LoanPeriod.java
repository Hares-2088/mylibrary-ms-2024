package com.mylibrary.loans.dataacess;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LoanPeriod {
    private LocalDate loanDate;
    private LocalDate dueDate;

    public LoanPeriod() {
        // Set loan date to current date
        this.loanDate = LocalDate.now();

        // Calculate due date as 2 weeks later
        this.dueDate = this.loanDate.plusWeeks(2);
    }
}
