package com.mylibrary.members.presentation;

import com.mylibrary.members.business.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<MemberResponseModel>> getMembers() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMembers());
    }

    @GetMapping(value = "/{memberId}", produces = "application/json")
    public ResponseEntity<MemberResponseModel> getMemberByMemberId(@PathVariable String memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMember(memberId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<MemberResponseModel> addMember(@RequestBody MemberRequestModel memberRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.addMember(memberRequestModel));
    }
    @PutMapping(value = "/{memberId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<MemberResponseModel> updateMember(@RequestBody MemberRequestModel memberRequestModel, @PathVariable String memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMember(memberRequestModel, memberId));
    }

    @PatchMapping(value = "/{memberId}/addReservation/{reservationId}", produces = "application/json")
    public ResponseEntity<MemberResponseModel> patchMemberWithReservationId(@PathVariable String memberId, @PathVariable String reservationId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.patchMemberWithReservationId(memberId, reservationId));
    }

    @DeleteMapping(value = "/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable String memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
