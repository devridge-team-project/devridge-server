package org.devridge.api.application.member;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.dto.response.OccupationInformation;
import org.devridge.api.infrastructure.member.OccupationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OccupationService {

    private final OccupationRepository occupationRepository;

    public List<OccupationInformation> getAllOccupations() {
        return occupationRepository.findAll()
                .stream()
                .map(occupation -> new OccupationInformation(
                        occupation.getId(), occupation.getOccupation())
                ).collect(Collectors.toList());
    }
}
