package com.mylibrary.members.presentation;

import com.mylibrary.members.business.MemberService;
import com.mylibrary.members.dataacess.Address;
import com.mylibrary.members.dataacess.MemberType;
import com.mylibrary.members.dataacess.Membership;
import com.mylibrary.members.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MemberController.class)
class MemberControllerUnitTest {

    private final String FOUND_MEMBER_ID = "m1";
    private final String NOT_FOUND_MEMBER_ID = "m0";

    @Autowired
    MemberController memberController;

    @MockBean
    private MemberService memberService;

    @Test
    public void whenNoMemberExists_thenReturnEmptyList() {
        //arrange
        when(memberService.getMembers()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<MemberResponseModel>> responseEntityList = memberController.getMembers();

        //assert
        assertNotNull(responseEntityList);
        assertEquals(HttpStatus.OK, responseEntityList.getStatusCode());
        assertTrue(responseEntityList.getBody().isEmpty());
        verify(memberService, times(1)).getMembers();
    }

    @Test
    public void whenMemberExists_thenReturnMember(){

        MemberRequestModel memberRequestModel = buildMemberRequestModel();
        MemberResponseModel memberResponseModel = buildMemberResponseModel();

        //arrange
        when(memberService.addMember(memberRequestModel)).thenReturn(memberResponseModel);

        //act
        ResponseEntity<MemberResponseModel> responseEntity = memberController.addMember(memberRequestModel);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(memberResponseModel, responseEntity.getBody());
        verify(memberService, times(1)).addMember(memberRequestModel);
    }

    @Test
    public void whenMemberExist_thenDeleteMember() throws NotFoundException {
        // Arrange
        doNothing().when(memberService).deleteMember(FOUND_MEMBER_ID);

        // Act
        ResponseEntity<Void> responseEntity = memberController.deleteMember(FOUND_MEMBER_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(memberService, times(1)).deleteMember(FOUND_MEMBER_ID);
    }

    @Test
    public void WhenMemberDoesNotExistOnDelete_thenThrowNotFoundException() throws NotFoundException {
        // Arrange
        String nonExistentMemberId = "m0";
        doThrow(NotFoundException.class).when(memberService).deleteMember(nonExistentMemberId);

        // Act and Assert
        try {
            memberController.deleteMember(nonExistentMemberId);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(memberService, times(1)).deleteMember(nonExistentMemberId);
        }
    }


    @Test
    public void whenMemberNotFoundOnGet_thenReturnNotFoundException() {
        // Arrange
        when(memberService.getMember(NOT_FOUND_MEMBER_ID)).thenThrow(NotFoundException.class);

        // Act and Assert
        try {
            memberController.getMemberByMemberId(NOT_FOUND_MEMBER_ID);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(memberService, times(1)).getMember(NOT_FOUND_MEMBER_ID);
        }
    }

    @Test
    public void whenMemberDoesNotExistOnUpdate_thenReturnNotFoundException() throws NotFoundException {
        // Arrange
        MemberRequestModel updatedMember = buildMemberRequestModel();
        when(memberService.updateMember(updatedMember, NOT_FOUND_MEMBER_ID)).thenThrow(NotFoundException.class);

        // Act and Assert
        try {
            memberController.updateMember(updatedMember, NOT_FOUND_MEMBER_ID);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(memberService, times(1)).updateMember(updatedMember, NOT_FOUND_MEMBER_ID);
        }
    }


    @Test
    public void whenMemberExist_thenReturnUpdatedMember() throws NotFoundException {
        // Arrange
        String memberId = "m1";
        MemberRequestModel updatedMember = buildMemberRequestModel();
        MemberResponseModel updatedMemberResponse = buildMemberResponseModel();

        when(memberService.updateMember(updatedMember, memberId)).thenReturn(updatedMemberResponse);

        // Act
        ResponseEntity<MemberResponseModel> responseEntity = memberController.updateMember(updatedMember, memberId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(updatedMemberResponse, responseEntity.getBody());
        verify(memberService, times(1)).updateMember(updatedMember, memberId);
    }

    private MemberRequestModel buildMemberRequestModel() {
        Address address = new Address("1234", "Montreal", "Qc", "Canada");

        Membership membership = new Membership("benefits", MemberType.SENIOR);

        return MemberRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email")
                .benefits("benefits")
                .memberType(MemberType.SENIOR)
                .street("1234")
                .city("Montreal")
                .province("Qc")
                .country("Canada")
                .build();
    }

    private MemberResponseModel buildMemberResponseModel() {
        return MemberResponseModel.builder()
                .memberId(FOUND_MEMBER_ID)
                .firstName("John")
                .lastName("Doe")
                .email("email")
                .benefits("benefits")
                .memberType("SENIOR")
                .street("1234")
                .city("Montreal")
                .province("Qc")
                .country("Canada")
                .build();
    }
}