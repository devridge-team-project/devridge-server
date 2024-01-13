package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.qna.entity.QnA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnARepository extends JpaRepository<QnA, Long> {

    @Query(
        value = "SELECT Q " +
                "FROM QnA Q " +
                "ORDER BY Q.views DESC"
    )
    List<QnA> findAllQnASortByViews();

    @Query(
        value = "SELECT Q " +
                "FROM QnA Q " +
                "ORDER BY Q.createdAt DESC"
    )
    List<QnA> findAllQnASortByCreatedAt();
}
