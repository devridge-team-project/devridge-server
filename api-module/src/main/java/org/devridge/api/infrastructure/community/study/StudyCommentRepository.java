package org.devridge.api.infrastructure.community.study;

import org.devridge.api.domain.community.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {

}
