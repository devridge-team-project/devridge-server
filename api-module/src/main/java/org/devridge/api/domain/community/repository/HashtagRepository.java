package org.devridge.api.domain.community.repository;

import java.util.Optional;
import org.devridge.api.domain.community.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByWord(String word);

    @Modifying
    @Query(
        value = "UPDATE Hashtag h " +
                "SET h.count = h.hashtags.size " +
                "WHERE h.id = :hashtagId"
    )
    void updateCountByHashtagId(@Param("hashtagId") Long hashtagId);
}
