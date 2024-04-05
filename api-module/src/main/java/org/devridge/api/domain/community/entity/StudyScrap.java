package org.devridge.api.domain.community.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.common.entity.BaseTimeEntity;
import org.devridge.api.domain.community.entity.id.StudyScrapId;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE study_scrap SET is_deleted = true WHERE member_id = ? AND study_id = ?")
public class StudyScrap extends BaseTimeEntity {

    @EmbeddedId
    private StudyScrapId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @MapsId("studyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", insertable = false, updatable = false)
    private Study study;

    @Builder
    public StudyScrap(StudyScrapId id, Member member, Study study) {
        this.id = new StudyScrapId(member.getId(), study.getId());
        this.member = member;
        this.study = study;
    }
}
