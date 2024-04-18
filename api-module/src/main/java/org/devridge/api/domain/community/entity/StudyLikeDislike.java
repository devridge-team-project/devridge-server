package org.devridge.api.domain.community.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.common.entity.BaseTimeEntity;
import org.devridge.api.domain.community.entity.id.StudyLikeDislikeId;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE study_like_dislike SET is_deleted = true WHERE member_id = ? AND study_id = ?")
public class StudyLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private StudyLikeDislikeId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;


    @MapsId("studyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", insertable = false, updatable = false)
    private Study study;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public StudyLikeDislike(Member member, Study study, LikeStatus status) {
        this.id = new StudyLikeDislikeId(member.getId(), study.getId());
        this.member = member;
        this.study = study;
        this.status = status;
    }
}
