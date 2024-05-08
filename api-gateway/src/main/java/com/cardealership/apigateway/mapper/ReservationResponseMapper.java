package com.cardealership.apigateway.mapper;

import com.cardealership.apigateway.presentationlayer.reservations.ReservationController;
import com.cardealership.apigateway.presentationlayer.reservations.ReservationResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))

public interface ReservationResponseMapper {

    ReservationResponseModel responseModelToResponseModel(ReservationResponseModel responseModel);

    List<ReservationResponseModel> responseModelListToResponseModelList(List<ReservationResponseModel> responseModel);

    @AfterMapping
    default void afterMapping(@MappingTarget ReservationResponseModel reservationResponseModel) {

        //customer Link
        Link selfLink = linkTo(methodOn(ReservationController.class)
                .getReservationByReservationId(reservationResponseModel.getReservationId(), reservationResponseModel.getMemberId()))
                .withSelfRel();

        reservationResponseModel.add(selfLink);

        //all customers link
        Link allLink = linkTo(methodOn(ReservationController.class)
                .getAllReservations())
                .withRel("all reservations");

        reservationResponseModel.add(allLink);
    }
}
