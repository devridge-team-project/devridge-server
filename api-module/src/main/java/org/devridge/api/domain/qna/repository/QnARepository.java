package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.qna.entity.QnA;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnARepository extends JpaRepository<QnA, Long> { }
