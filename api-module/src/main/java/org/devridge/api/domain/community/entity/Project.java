package org.devridge.api.domain.community.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.skill.entity.ProjectSkill;
import org.devridge.api.common.entity.BaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE project SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Project extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    private String title;

    private String content;

    @ColumnDefault("0")
    private Long views;

    @ColumnDefault("0")
    private Long likes;

    @ColumnDefault("0")
    private Long dislikes;

    private String roles;

    private String images;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectSkill> projectSkills = new ArrayList<>();

    private String meeting;

    @Column(columnDefinition = "TINYINT(1)")
    @ColumnDefault("true")
    private Boolean isRecruiting;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectScrap> scraps = new ArrayList<>();

    @Builder
    public Project(Member member, String title, String content, String roles, String images, String meeting, Boolean isRecruiting) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.roles = roles;
        this.images = images;
        this.meeting = meeting;
        this.isRecruiting = isRecruiting;
    }



    public void updateProject(String title, String content, String roles, String images, String meeting, Boolean isRecruiting) {
        this.title = title;
        this.content = content;
        this.roles = roles;
        this.images = images;
        this.meeting = meeting;
        this.isRecruiting = isRecruiting;
    }
}
