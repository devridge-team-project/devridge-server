package org.devridge.api.domain.community.repository;

import java.util.Optional;
import org.devridge.api.domain.community.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByWord(String word);
}
