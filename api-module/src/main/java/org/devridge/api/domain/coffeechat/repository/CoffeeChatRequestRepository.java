package org.devridge.api.domain.coffeechat.repository;

import org.devridge.api.domain.coffeechat.entity.CoffeeChatRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeChatRequestRepository extends JpaRepository<CoffeeChatRequest, Long> {

}
