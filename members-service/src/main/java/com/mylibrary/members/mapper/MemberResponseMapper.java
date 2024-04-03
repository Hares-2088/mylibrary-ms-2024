package com.mylibrary.members.mapper;


import com.mylibrary.members.dataacess.Member;
import com.mylibrary.members.presentation.MemberResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;



@Mapper(componentModel = "spring")
public interface MemberResponseMapper {

    @Mapping(expression = "java(member.getMemberIdentifier().getMemberId())", target = "memberId")
    @Mapping(expression = "java(member.getAddress().getStreet())", target = "street")
    @Mapping(expression = "java(member.getAddress().getCity())", target = "city")
    @Mapping(expression = "java(member.getAddress().getProvince())", target = "province")
    @Mapping(expression = "java(member.getAddress().getCountry())", target = "country")
    @Mapping(expression = "java(member.getMembership().getMemberType().toString())", target = "memberType")
    @Mapping(expression = "java(member.getMembership().getBenefits())", target = "benefits")
    MemberResponseModel entityToResponseModel(Member member);

//    List<MemberResponseModel> entityListToResponseModelList(List<Member> members);

//    @AfterMapping
//    default void addLinks(@MappingTarget MemberResponseModel model, Member member) {
//        Link selfLink = linkTo(methodOn(MemberController.class).
//                getMemberByMemberId(model.getMemberId())).withSelfRel();
//        model.add(selfLink);
//    }
}
