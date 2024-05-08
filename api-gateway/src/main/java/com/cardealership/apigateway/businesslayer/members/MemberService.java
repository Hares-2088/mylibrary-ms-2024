package com.cardealership.apigateway.businesslayer.members;

import com.cardealership.apigateway.presentationlayer.members.MemberRequestModel;
import com.cardealership.apigateway.presentationlayer.members.MemberResponseModel;

import java.util.List;

public interface MemberService {
    List<MemberResponseModel> getAllMembers();
    MemberResponseModel getMemberByMemberId(String memberId);
    MemberResponseModel createMember(MemberRequestModel memberRequestModel);
    void updateMember(String customerId, MemberRequestModel memberRequestModel);
    void deleteMember(String customerId);
}
