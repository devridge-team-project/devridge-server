package org.devridge.api.application.note;

import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.note.dto.request.ProjectParticipationNoteRequest;
import org.devridge.api.domain.note.dto.request.StudyParticipationNoteRequest;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteDetailResponse;
import org.devridge.api.domain.note.dto.response.SentParticipationNoteDetailResponse;
import org.devridge.api.domain.note.entity.ParticipationNote;
import org.springframework.stereotype.Component;

@Component
public class ParticipationNoteMapper {

    public ParticipationNote toProjectParticipationNote(
        Project project,
        Member receiver,
        Member sender,
        ProjectParticipationNoteRequest participationNoteRequest
    ) {
        return ParticipationNote.builder()
            .project(project)
            .receiver(receiver)
            .sender(sender)
            .content(participationNoteRequest.getContent())
            .build();
    }

    public ReceivedParticipationNoteDetailResponse toReceivedParticipationNoteDetailResponse(
        UserInformation senderInfo,
        ParticipationNote participationNote,
        String category,
        Long categoryId
    ) {
        return ReceivedParticipationNoteDetailResponse.builder()
            .sendMember(senderInfo)
            .content(participationNote.getContent())
            .receiveTime(participationNote.getCreatedAt())
            .isApproved(participationNote.getIsApproved())
            .category(category)
            .categoryId(categoryId)
            .build();
    }

    public SentParticipationNoteDetailResponse toSentParticipationNoteDetailResponse(
        UserInformation receiverInfo,
        ParticipationNote participationNote,
        String category,
        Long categoryId
    ) {
        return SentParticipationNoteDetailResponse.builder()
            .receiveMember(receiverInfo)
            .content(participationNote.getContent())
            .sendTime(participationNote.getCreatedAt())
            .isApproved(participationNote.getIsApproved())
            .category(category)
            .categoryId(categoryId)
            .build();
    }

    public ParticipationNote toStudyParticipationNote(
        Study study,
        Member receiver,
        Member sender,
        StudyParticipationNoteRequest participationNoteRequest
    ) {
        return ParticipationNote.builder()
            .study(study)
            .receiver(receiver)
            .sender(sender)
            .content(participationNoteRequest.getContent())
            .build();
    }
}
