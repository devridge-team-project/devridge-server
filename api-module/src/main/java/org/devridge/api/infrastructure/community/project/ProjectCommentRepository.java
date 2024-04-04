package org.devridge.api.infrastructure.community.project;

import org.devridge.api.domain.community.entity.ProjectComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {

}
