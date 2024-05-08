package com.cardealership.apigateway.mapper;

import com.cardealership.apigateway.presentationlayer.members.MemberController;
import com.cardealership.apigateway.presentationlayer.members.MemberResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface MemberResponseMapper {
    MemberResponseModel responseModelToResponseModel(MemberResponseModel responseModel);

    List<MemberResponseModel> responseModelListToResponseModelList(List<MemberResponseModel> responseModel);

    @AfterMapping
    default void afterMapping(@MappingTarget MemberResponseModel memberResponseModel) {

        //customer Link
        Link selfLink = linkTo(methodOn(MemberController.class)
                .getMemberByMemberId(memberResponseModel.getMemberId()))
                .withSelfRel();

        memberResponseModel.add(selfLink);

        //all customers link
        Link allLink = linkTo(methodOn(MemberController.class)
                .getMembers())
                .withRel("all members");

        memberResponseModel.add(allLink);
    }

}
