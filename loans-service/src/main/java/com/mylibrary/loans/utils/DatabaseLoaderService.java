package com.mylibrary.loans.utils;
import com.mylibrary.loans.dataacess.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DatabaseLoaderService implements CommandLineRunner {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public void run(String... args) throws Exception {

        var saleIdentifier = new SaleIdentifier();
        var vehicleModel = VehicleModel.builder()
                .vehicleId("veh01")
                .model("Toyota")
                .model("Camry")
                .inventoryId("inv01")
                .build();

        var customerModel = CustomerModel.builder()
                .customerId("cus01")
                .firstName("John")
                .lastName("Doe")
                .build();

        var employeeModel = EmployeeModel.builder()
                .employeeId("emp01")
                .firstName("John")
                .lastName("Doe")
                .build();

        var financingAgreement = FinancingAgreementDetails.builder()
                .numberOfMonthlyPayments(60)
                .monthlyPaymentAmount(new BigDecimal(500))
                .downPaymentAmount(new BigDecimal(1000))
                .paymentCurrency(Currency.USD)
                .build();

        var sale1 = Sale.builder()
                .saleIdentifier(saleIdentifier)
                .vehicleModel(vehicleModel)
                .customerModel(customerModel)
                .employeeModel(employeeModel)
                .financingAgreementsDetails(financingAgreement)
                .salePrice(new Price(new BigDecimal(20000), Currency.USD))
                .saleStatus(SaleStatus.PURCHASE_COMPLETED)
                .saleOfferDate(LocalDate.of(2024, 1, 1))
                .warranty(new Warranty(LocalDate.of(2024, 1, 1), "Warranty 1"))
                .build();

        loanRepository.save(sale1);
    }
}
