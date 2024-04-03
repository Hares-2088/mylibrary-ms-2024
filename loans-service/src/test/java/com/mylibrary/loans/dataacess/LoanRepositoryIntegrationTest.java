package com.mylibrary.loans.dataacess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
//class LoanRepositoryIntegrationTest {
//
//    @Autowired
//    private LoanRepository loanRepository;
//
//    @BeforeEach
//    public void setupDb(){
//        loanRepository.deleteAll();
//    }

//    @Test
//    public void whenLoanExists_ReturnLoanByLoanId() {
//        //arrange
//        Loan loan = new Loan("1234", "1234", "1234", "1234");
//        loanRepository.save(loan);
//
//        //act
//        Loan savedLoan = loanRepository.findLoanByLoanId(loan.getLoanId());
//
//        //assert
//        assertNotNull(savedLoan);
//        assertEquals(loan.getLoanId(), savedLoan.getLoanId());
//        assertEquals(loan.getBookId(), savedLoan.getBookId());
//        assertEquals(loan.getUserId(), savedLoan.getUserId());
//        assertEquals(loan.getLoanDate(), savedLoan.getLoanDate());
//    }

//}