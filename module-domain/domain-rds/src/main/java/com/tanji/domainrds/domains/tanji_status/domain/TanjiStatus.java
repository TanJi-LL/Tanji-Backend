package com.tanji.domainrds.domains.tanji_status.domain;

import com.tanji.domainrds.domains.auditing.BaseTimeEntity;
import com.tanji.domainrds.domains.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tanji_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TanjiStatus extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    private int thirsty = 100;

    private int hunger = 100;

    @Builder
    public TanjiStatus(Member member) {
        this.member = member;
    }
}
