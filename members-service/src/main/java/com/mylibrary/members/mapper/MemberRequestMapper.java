package com.mylibrary.members.mapper;


import com.mylibrary.members.dataacess.Address;
import com.mylibrary.members.dataacess.Member;
import com.mylibrary.members.dataacess.MemberIdentifier;
import com.mylibrary.members.dataacess.Membership;
import com.mylibrary.members.presentation.MemberRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberRequestMapper {

    @Mapping(target = "id", ignore = true)
    Member requestModelToEntity(MemberRequestModel memberRequestModel, MemberIdentifier memberIdentifier,
                                Address address, Membership membership);
}
