package com.mylibrary.members.dataacess;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Membership {

    private String benefits;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    public Membership(String benefits, MemberType memberType) {
        this.benefits = benefits;
        this.memberType = memberType;
    }
}
