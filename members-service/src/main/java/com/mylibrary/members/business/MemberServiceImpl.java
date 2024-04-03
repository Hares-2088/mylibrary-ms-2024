package com.mylibrary.members.business;

import com.mylibrary.members.dataacess.*;
import com.mylibrary.members.mapper.MemberRequestMapper;
import com.mylibrary.members.mapper.MemberResponseMapper;
import com.mylibrary.members.presentation.MemberRequestModel;
import com.mylibrary.members.presentation.MemberResponseModel;
import com.mylibrary.members.utils.exceptions.NotFoundException;
import com.mylibrary.members.utils.exceptions.NotInRegionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final MemberRequestMapper memberRequestMapper;
    private final MemberResponseMapper memberResponseMapper;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, MemberRequestMapper memberRequestMapper, MemberResponseMapper memberResponseMapper) {
        this.memberRepository = memberRepository;
        this.memberRequestMapper = memberRequestMapper;
        this.memberResponseMapper = memberResponseMapper;
    }


    @Override
    public List<MemberResponseModel> getMembers() {
        List<Member> memberList = memberRepository.findAll();

        List<MemberResponseModel> memberResponseModelList = new ArrayList<>();

        for (Member member : memberList) {
           memberResponseModelList.add(memberResponseMapper.entityToResponseModel(member));
        }
        return memberResponseModelList;
    }

    @Override
    public MemberResponseModel getMember(String memberId) {
        Member member = memberRepository.findByMemberIdentifier_MemberId(memberId);

        if (member == null) {
            throw new NotFoundException("Unknown memberId: " + memberId);
        }

        return memberResponseMapper.entityToResponseModel(member);
    }

    @Override
    public MemberResponseModel addMember(MemberRequestModel memberRequestModel) {

        Membership membership = new Membership(memberRequestModel.getBenefits(), memberRequestModel.getMemberType());

        Member member = memberRequestMapper.requestModelToEntity(memberRequestModel, new MemberIdentifier(),
                new Address(memberRequestModel.getStreet(), memberRequestModel.getCity(), memberRequestModel.getProvince(), memberRequestModel.getCountry()), membership);

        //exception handling if th member is not from Quebec
        if (!Objects.equals(member.getAddress().getProvince(), "Quebec")) {
            throw new NotInRegionException("Member must be from Quebec to be accepted in this library.");
        }
        return memberResponseMapper.entityToResponseModel(memberRepository.save(member));
    }

    @Override
    public MemberResponseModel updateMember(MemberRequestModel memberRequestModel, String memberId) {

        //exception handling if the updated member is not from Quebec
        if (!Objects.equals(memberRequestModel.getProvince(), "Quebec")) {
            throw new NotInRegionException("Member must be from Quebec to be accepted in this library.");
        }

        Member member = memberRepository.findByMemberIdentifier_MemberId(memberId);
        if (member == null) {
            throw new NotFoundException("Unknown memberId: " + memberId);
        }

        Member updatedMember = memberRequestMapper.requestModelToEntity(memberRequestModel, member.getMemberIdentifier(),
                new Address(memberRequestModel.getStreet(), memberRequestModel.getCity(), memberRequestModel.getProvince(), memberRequestModel.getCountry()), new Membership(memberRequestModel.getBenefits(), memberRequestModel.getMemberType()));
        updatedMember.setId(member.getId());

        return memberResponseMapper.entityToResponseModel(memberRepository.save(updatedMember));
    }

    @Override
    public void deleteMember(String memberId) {
        Member member = memberRepository.findByMemberIdentifier_MemberId(memberId);

        if (member == null) {
            throw new NotFoundException("Unknown memberId: " + memberId);
        }
        memberRepository.delete(member);
    }
}
