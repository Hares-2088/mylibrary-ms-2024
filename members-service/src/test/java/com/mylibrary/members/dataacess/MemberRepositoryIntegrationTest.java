package com.mylibrary.members.dataacess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setupDb(){
        memberRepository.deleteAll();
    }

    @Test
    public void whenMemberExists_ReturnMemberByMemberId() {
        //arrange
        Membership membership = new Membership("benefits", MemberType.SENIOR);
        //create a new member with attributes
        Member member = new Member("Adem", "Bessam", "adem@gmmial.com",
                membership, new Address("1234", "Toronto", "Quebec", "Canada"));
        memberRepository.save(member);

        //act
        Member savedMember = memberRepository.findByMemberIdentifier_MemberId(member.getMemberIdentifier().getMemberId());

        //assert
        assertNotNull(savedMember);
        assertEquals(member.getMemberIdentifier(), savedMember.getMemberIdentifier());
        assertEquals(member.getFirstName(), savedMember.getFirstName());
        assertEquals(member.getLastName(), savedMember.getLastName());
        assertEquals(member.getEmail(), savedMember.getEmail());
    }

    @Test
    public void whenMemberDoesNotExist_ReturnNull() {
        //act
        Member savedMember = memberRepository.findByMemberIdentifier_MemberId("1234");

        //assert
        assertNull(savedMember);
    }

}