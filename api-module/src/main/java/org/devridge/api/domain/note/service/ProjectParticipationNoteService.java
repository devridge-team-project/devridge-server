package org.devridge.api.domain.note.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.repository.ProjectRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.exception.MemberNotFoundException;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.note.dto.request.ProjectParticipationNoteRequest;
import org.devridge.api.domain.note.entity.ProjectParticipationNote;
import org.devridge.api.domain.note.repository.ProjectParticipationNoteRepository;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectParticipationNoteService {

    private final ProjectParticipationNoteRepository projectParticipationNoteRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public Long createRequestNote(Long projectId, ProjectParticipationNoteRequest participationNoteRequest) { // 프로젝트 아이디 필요, 내용필요, 보낸는사람 필요, 받을 사람 필요,
        Member sender = SecurityContextHolderUtil.getMember();
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
        Member receiver = memberRepository.findById(project.getMember().getId())
                .orElseThrow(() -> new MemberNotFoundException(403, "탈퇴한 회원입니다."));

        ProjectParticipationNote projectParticipationNote = ProjectParticipationNote.builder()
            .project(project)
            .receiver(receiver)
            .sender(sender)
            .content(participationNoteRequest.getContent())
            .build();

        return projectParticipationNoteRepository.save(projectParticipationNote).getId();
    }

}
