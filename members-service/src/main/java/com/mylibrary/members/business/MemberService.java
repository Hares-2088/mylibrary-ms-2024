package com.mylibrary.members.business;

import com.mylibrary.members.presentation.MemberRequestModel;
import com.mylibrary.members.presentation.MemberResponseModel;

import java.util.List;

public interface MemberService {
    List<MemberResponseModel> getMembers();
    MemberResponseModel getMember(String memberId);
    MemberResponseModel addMember(MemberRequestModel memberRequestModel);
    MemberResponseModel updateMember(MemberRequestModel updatedMember, String memberId);
    MemberResponseModel patchMemberWithReservationId(String memberId, String reservationId);
    void deleteMember(String memberId);
}
