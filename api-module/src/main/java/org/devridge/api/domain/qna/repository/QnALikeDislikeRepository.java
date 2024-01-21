package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.qna.entity.QnALikeDislike;
import org.devridge.api.domain.qna.entity.id.QnALikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnALikeDislikeRepository extends JpaRepository<QnALikeDislike, QnALikeDislikeId> { }