package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.qna.entity.QnAComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnACommentRepository extends JpaRepository<QnAComment, Long> { }
