package com.mylibrary.members.dataacess;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "MEMBERS")
@NoArgsConstructor
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private MemberIdentifier memberIdentifier;

    private String firstName;

    private String lastName;

    private String email;

    @Embedded
    private Membership membership;

    @Embedded
    private Address address;

    public Member(String firstName, String lastName, String email, Membership membership, Address address) {
        this.memberIdentifier = new MemberIdentifier();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.membership = membership;
        this.address = address;
    }

}
