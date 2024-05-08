package com.cardealership.apigateway.presentationlayer.members;

import com.cardealership.apigateway.businesslayer.members.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<MemberResponseModel>> getMembers() {
        return ResponseEntity.status(HttpStatus.FOUND).body(memberService.getAllMembers());
    }

    @GetMapping(value = "/{memberId}", produces = "application/json")
    public ResponseEntity<MemberResponseModel> getMemberByMemberId(@PathVariable String memberId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(memberService.getMemberByMemberId(memberId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<MemberResponseModel> addMember(@RequestBody MemberRequestModel memberRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(memberRequestModel));
    }
    @PutMapping(value = "/{memberId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Void> updateMember(@RequestBody MemberRequestModel memberRequestModel, @PathVariable String memberId) {
        memberService.updateMember(memberId, memberRequestModel);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping(value = "/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable String memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
