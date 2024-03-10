package org.devridge.api.domain.note.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.mapper.MemberInfoMapper;
import org.devridge.api.domain.community.repository.ProjectRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.exception.MemberNotFoundException;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.note.dto.request.ProjectParticipationNoteRequest;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteDetailResponse;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteListResponse;
import org.devridge.api.domain.note.entity.ProjectParticipationNote;
import org.devridge.api.domain.note.repository.ParticipationNoteQuerydslRepository;
import org.devridge.api.domain.note.repository.ProjectParticipationNoteRepository;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectParticipationNoteService {

    private final ProjectParticipationNoteRepository projectParticipationNoteRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoMapper memberInfoMapper;
    private final ParticipationNoteQuerydslRepository participationNoteQuerydslRepository;

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

    @Transactional
    public ReceivedParticipationNoteDetailResponse getReceivedParticipationNoteDetail(Long participationNoteId) { // 맴버 정보, 보낸시간, 내용
        Member receiver = SecurityContextHolderUtil.getMember();
        ProjectParticipationNote participationNote = projectParticipationNoteRepository.findById(participationNoteId)
                .orElseThrow(() -> new DataNotFoundException());
        Project project = projectRepository.findById(participationNote.getProject().getId()).orElseThrow(() -> new DataNotFoundException());
        Member sender = memberRepository.findById(project.getMember().getId()).orElseGet(
            () -> Member.builder()
                    .nickname("탈퇴 회원")
                    .introduction("탈퇴한 회원입니다.")
                    .profileImageUrl("default.png").build()
        ); // 임시 방편 탈퇴회원

        // 알아야 하는것 = 프로젝트 아이디를 알아야 함

        // 반환할 것 = 맴버 정보 , 내용, projectId?

        participationNote.updateReadAt();
        MemberInfoResponse senderInfo = memberInfoMapper.toMemberInfoResponse(sender);

        return ReceivedParticipationNoteDetailResponse.builder()
                .sendMember(senderInfo)
                .content(participationNote.getContent())
                .receiveTime(participationNote.getCreatedAt())
                .build();
    }

    public Slice<ReceivedParticipationNoteListResponse> getAllReceivedParticipationNote(Pageable pageable, Long lastId) {
        Member receiver = SecurityContextHolderUtil.getMember();
        List<ProjectParticipationNote> projectParticipationNotes =
                participationNoteQuerydslRepository.searchByProjectParticipationNote(lastId, receiver.getId(), pageable);
        List<ReceivedParticipationNoteListResponse> receivedParticipationNoteListResponses = new ArrayList<>();

        for (ProjectParticipationNote projectParticipationNote : projectParticipationNotes) {
            MemberInfoResponse sendMember = memberInfoMapper.toMemberInfoResponse(projectParticipationNote.getSender());
            receivedParticipationNoteListResponses.add(
                ReceivedParticipationNoteListResponse.builder()
                    .sendMember(sendMember)
                    .receivedTime(projectParticipationNote.getCreatedAt())
                    .build()
            );
        }
        return checkLastPage(pageable, receivedParticipationNoteListResponses);
    }

    private Slice<ReceivedParticipationNoteListResponse> checkLastPage(Pageable pageable, List<ReceivedParticipationNoteListResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
    @Transactional
    public void deleteParticipationNoteByReceiver(Long participationNoteId) {
        Member receiver = SecurityContextHolderUtil.getMember();
        ProjectParticipationNote projectParticipationNote =
                projectParticipationNoteRepository.findById(participationNoteId).orElseThrow();

        if (receiver.getId().equals(projectParticipationNote.getReceiver().getId())) {
            projectParticipationNote.deleteByReceiver();
            if (projectParticipationNote.isDeleted()) {
                projectParticipationNoteRepository.delete(projectParticipationNote);
            }
        }
    }

    @Transactional
    public void deleteParticipationNoteBySender(Long participationNoteId) {
        Member sender = SecurityContextHolderUtil.getMember();
        ProjectParticipationNote projectParticipationNote =
            projectParticipationNoteRepository.findById(participationNoteId).orElseThrow();

        if (sender.getId().equals(projectParticipationNote.getSender().getId())) {
            projectParticipationNote.deleteBySender();
            if (projectParticipationNote.isDeleted()) {
                projectParticipationNoteRepository.delete(projectParticipationNote);
            }
        }
    }
}
