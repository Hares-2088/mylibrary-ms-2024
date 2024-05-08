package com.cardealership.apigateway.businesslayer.members;

import com.cardealership.apigateway.domainclientlayer.member.MemberServiceClient;
import com.cardealership.apigateway.mapper.MemberResponseMapper;
import com.cardealership.apigateway.presentationlayer.members.MemberRequestModel;
import com.cardealership.apigateway.presentationlayer.members.MemberResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberServiceClient memberServiceClient;
    private final MemberResponseMapper memberResponseMapper;

    public MemberServiceImpl(MemberServiceClient memberServiceClient, MemberResponseMapper memberResponseMapper) {
        this.memberServiceClient = memberServiceClient;
        this.memberResponseMapper = memberResponseMapper;
    }


    @Override
    public List<MemberResponseModel> getAllMembers() {
        return memberResponseMapper.responseModelListToResponseModelList(memberServiceClient.getAllMembers());
    }

    @Override
    public MemberResponseModel getMemberByMemberId(String memberId) {
        return memberResponseMapper.responseModelToResponseModel(memberServiceClient.getMemberByMemberId(memberId));
    }

    @Override
    public MemberResponseModel createMember(MemberRequestModel memberRequestModel) {
        return memberResponseMapper.responseModelToResponseModel(memberServiceClient.createMember(memberRequestModel));
    }

    @Override
    public void updateMember(String customerId, MemberRequestModel memberRequestModel) {
        memberServiceClient.updateMember(customerId, memberRequestModel);
    }

    @Override
    public void deleteMember(String customerId) {
        memberServiceClient.deleteMember(customerId);
    }
}
