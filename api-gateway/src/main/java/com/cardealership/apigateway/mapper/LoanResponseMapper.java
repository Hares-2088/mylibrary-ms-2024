package com.cardealership.apigateway.mapper;

import com.cardealership.apigateway.presentationlayer.loans.LoanController;
import com.cardealership.apigateway.presentationlayer.loans.LoanResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))

public interface LoanResponseMapper {

    LoanResponseModel responseModelToResponseModel(LoanResponseModel responseModel);

    List<LoanResponseModel> responseModelListToResponseModelList(List<LoanResponseModel> responseModel);

    @AfterMapping
    default void afterMapping(@MappingTarget LoanResponseModel loanResponseModel) {

        //customer Link
        Link selfLink = linkTo(methodOn(LoanController.class)
                .getLoan(loanResponseModel.getLoanId(), loanResponseModel.getMemberId()))
                .withSelfRel();

        loanResponseModel.add(selfLink);

        //all customers link
        Link allLink = linkTo(methodOn(LoanController.class)
                .getLoans(loanResponseModel.getMemberId()))
                .withRel("all loans");

        loanResponseModel.add(allLink);
    }

}
