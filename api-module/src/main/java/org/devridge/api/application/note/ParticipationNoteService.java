package org.devridge.api.application.note;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.MemberUtil;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.exception.MemberNotFoundException;
import org.devridge.api.domain.note.dto.request.ProjectParticipationNoteRequest;
import org.devridge.api.domain.note.dto.request.StudyParticipationNoteRequest;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteDetailResponse;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteListResponse;
import org.devridge.api.domain.note.dto.response.SentParticipationNoteDetailResponse;
import org.devridge.api.domain.note.dto.response.SentParticipationNoteListResponse;
import org.devridge.api.domain.note.entity.ParticipationNote;
import org.devridge.api.domain.note.exception.ParticipationNoteForbiddenException;
import org.devridge.api.infrastructure.community.project.ProjectRepository;
import org.devridge.api.infrastructure.community.study.StudyRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.devridge.api.infrastructure.note.ParticipationNoteQuerydslRepository;
import org.devridge.api.infrastructure.note.ParticipationNoteRepository;
import org.devridge.api.infrastructure.note.ProjectParticipationNoteRepository;
import org.devridge.api.infrastructure.note.StudyParticipationNoteRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipationNoteService {

    private final ProjectParticipationNoteRepository projectParticipationNoteRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ParticipationNoteQuerydslRepository participationNoteQuerydslRepository;
    private final StudyParticipationNoteRepository studyParticipationNoteRepository;
    private final StudyRepository studyRepository;
    private final ParticipationNoteRepository participationNoteRepository;

    public Long createProjectParticipationNote(Long projectId, ProjectParticipationNoteRequest participationNoteRequest) {
        Member sender = SecurityContextHolderUtil.getMember();
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
        Member receiver = memberRepository.findById(project.getMember().getId())
                .orElseThrow(() -> new MemberNotFoundException(403, "탈퇴한 회원입니다."));

        if (receiver.getId().equals(project.getMember().getId())) {
            throw new ParticipationNoteForbiddenException(403, "본인이 모집한 프로젝트에 신청할 수 없습니다.");
        }

        ParticipationNote participationNote = ParticipationNote.builder()
                .project(project)
                .receiver(receiver)
                .sender(sender)
                .content(participationNoteRequest.getContent())
                .build();

        return participationNoteRepository.save(participationNote).getId();
    }

    @Transactional
    public ReceivedParticipationNoteDetailResponse getReceivedProjectParticipationNoteDetail(Long projectId, Long participationNoteId) {
        Member receiver = SecurityContextHolderUtil.getMember();
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
        ParticipationNote participationNote = participationNoteRepository.findById(participationNoteId)
                .orElseThrow(() -> new DataNotFoundException());

        if (!receiver.getId().equals(participationNote.getReceiver().getId())) {
            throw new ParticipationNoteForbiddenException(403, "회원님이 받은 요청이 아닙니다.");
        }

        UserInformation senderInfo = MemberUtil.toMember(participationNote.getSender());
        participationNote.updateReadAt();

        return ReceivedParticipationNoteDetailResponse.builder()
                .sendMember(senderInfo)
                .content(participationNote.getContent())
                .receiveTime(participationNote.getCreatedAt())
                .build();
    }

    public Slice<ReceivedParticipationNoteListResponse> getAllReceivedParticipationNote(Pageable pageable, Long lastId) {
        Member receiver = SecurityContextHolderUtil.getMember();
        List<ReceivedParticipationNoteListResponse> participationNotes =
                participationNoteQuerydslRepository.findAllReceivedParticipationNoteListResponses(lastId, receiver.getId(), pageable);
        return checkLastPage(pageable, participationNotes);
    }

    private <T> Slice<T> checkLastPage(Pageable pageable, List<T> results) {

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
        ParticipationNote participationNote =
                participationNoteRepository.findById(participationNoteId).orElseThrow(() -> new DataNotFoundException());

        if (receiver.getId().equals(participationNote.getReceiver().getId())) {
            participationNote.deleteByReceiver();
            if (participationNote.isDeleted()) {
                participationNoteRepository.delete(participationNote);
            }
        }
    }

    @Transactional
    public void deleteParticipationNoteBySender(Long participationNoteId) {
        Member sender = SecurityContextHolderUtil.getMember();
        ParticipationNote participationNote =
                participationNoteRepository.findById(participationNoteId).orElseThrow(() -> new DataNotFoundException());

        if (sender.getId().equals(participationNote.getSender().getId())) {
            participationNote.deleteBySender();
            if (participationNote.isDeleted()) {
                participationNoteRepository.delete(participationNote);
            }
        }
    }

    public SentParticipationNoteDetailResponse getSentProjectParticipationNoteDetail(Long projectId, Long participationNoteId) {
        Member sender = SecurityContextHolderUtil.getMember();
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
        ParticipationNote participationNote = participationNoteRepository.findById(participationNoteId)
                .orElseThrow(() -> new DataNotFoundException());

        if (!sender.getId().equals(participationNote.getSender().getId())) {
            throw new ParticipationNoteForbiddenException(403, "회원님이 보낸 요청이 아닙니다.");
        }

        UserInformation receiverInfo = MemberUtil.toMember(participationNote.getReceiver());

        return SentParticipationNoteDetailResponse.builder()
                .receiveMember(receiverInfo)
                .content(participationNote.getContent())
                .sendTime(participationNote.getCreatedAt())
                .isApproved(participationNote.getIsApproved())
                .build();
    }

    public Slice<SentParticipationNoteListResponse> getAllSentParticipationNote(Pageable pageable, Long lastId) {
        Member sender = SecurityContextHolderUtil.getMember();
        List<ProjectParticipationNote> projectParticipationNotes =
            participationNoteQuerydslRepository.findSenderSortByProjectParticipationNote(lastId, sender.getId(), pageable);
        List<SentParticipationNoteListResponse> sentParticipationNoteListResponses = new ArrayList<>();

        for (ProjectParticipationNote projectParticipationNote : projectParticipationNotes) {
            UserInformation receiveMember = MemberUtil.toMember(projectParticipationNote.getReceiver());
            sentParticipationNoteListResponses.add(
                SentParticipationNoteListResponse.builder()
                    .receiveMember(receiveMember)
                    .sentTime(projectParticipationNote.getCreatedAt())
                    .isApproved(projectParticipationNote.getIsApproved())
                    .build()
            );
        }
        return checkLastPage(pageable, sentParticipationNoteListResponses);
    }

    @Transactional
    public void participationApproval(Long participationNoteId, Boolean approve) {
        Member receiver = SecurityContextHolderUtil.getMember();
        ParticipationNote participationNote = participationNoteRepository.findById(participationNoteId)
                .orElseThrow(() -> new DataNotFoundException());

        if (!receiver.getId().equals(participationNote.getReceiver().getId())) {
            throw new ParticipationNoteForbiddenException(403, "본인에게 온 요청이 아닙니다.");
        }

        participationNote.updateIsApproved(approve);
    }

    public Long createStudyParticipationNote(Long studyId, StudyParticipationNoteRequest participationNoteRequest) {
        Member sender = SecurityContextHolderUtil.getMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException());
        Member receiver = memberRepository.findById(study.getMember().getId())
                .orElseThrow(() -> new MemberNotFoundException(403, "탈퇴한 회원입니다."));

        ParticipationNote participationNote = ParticipationNote.builder()
                .study(study)
                .receiver(receiver)
                .sender(sender)
                .content(participationNoteRequest.getContent())
                .build();

        return participationNoteRepository.save(participationNote).getId();
    }

    @Transactional
    public ReceivedParticipationNoteDetailResponse getReceivedStudyParticipationNoteDetail(Long studyId, Long participationNoteId) {
        Member receiver = SecurityContextHolderUtil.getMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException());
        ParticipationNote participationNote = participationNoteRepository.findById(participationNoteId)
                .orElseThrow(() -> new DataNotFoundException());

        if (!receiver.getId().equals(participationNote.getReceiver().getId())) {
            throw new ParticipationNoteForbiddenException(403, "회원님이 받은 요청이 아닙니다.");
        }

        UserInformation senderInfo = MemberUtil.toMember(participationNote.getSender());
        participationNote.updateReadAt();

        return ReceivedParticipationNoteDetailResponse.builder()
                .sendMember(senderInfo)
                .content(participationNote.getContent())
                .receiveTime(participationNote.getCreatedAt())
                .build();
    }

    @Transactional
    public void deleteStudyParticipationNoteByReceiver(Long participationNoteId) {
        Member receiver = SecurityContextHolderUtil.getMember();
        StudyParticipationNote studyParticipationNote =
            studyParticipationNoteRepository.findById(participationNoteId).orElseThrow(() -> new DataNotFoundException());

        if (receiver.getId().equals(studyParticipationNote.getReceiver().getId())) {
            studyParticipationNote.deleteByReceiver();
            if (studyParticipationNote.isDeleted()) {
                studyParticipationNoteRepository.delete(studyParticipationNote);
            }
        }
    }

    @Transactional
    public void deleteStudyParticipationNoteBySender(Long participationNoteId) {
        Member sender = SecurityContextHolderUtil.getMember();
        StudyParticipationNote studyParticipationNote =
            studyParticipationNoteRepository.findById(participationNoteId).orElseThrow(() -> new DataNotFoundException());

        if (sender.getId().equals(studyParticipationNote.getSender().getId())) {
            studyParticipationNote.deleteBySender();
            if (studyParticipationNote.isDeleted()) {
                studyParticipationNoteRepository.delete(studyParticipationNote);
            }
        }
    }

    public SentParticipationNoteDetailResponse getSentStudyParticipationNoteDetail(Long studyId, Long participationNoteId) {
        Member sender = SecurityContextHolderUtil.getMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException());
        ParticipationNote participationNote = participationNoteRepository.findById(participationNoteId)
                .orElseThrow(() -> new DataNotFoundException());

        if (!sender.getId().equals(participationNote.getSender().getId())) {
            throw new ParticipationNoteForbiddenException(403, "회원님이 보낸 요청이 아닙니다.");
        }

        UserInformation receiverInfo = MemberUtil.toMember(participationNote.getReceiver());

        return SentParticipationNoteDetailResponse.builder()
                .receiveMember(receiverInfo)
                .content(participationNote.getContent())
                .sendTime(participationNote.getCreatedAt())
                .isApproved(participationNote.getIsApproved())
                .build();
    }
}
