package com.mylibrary.members.dataacess;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class MemberIdentifier {

    private String memberId;

    public MemberIdentifier() {
        this.memberId = UUID.randomUUID().toString();
    }
}
