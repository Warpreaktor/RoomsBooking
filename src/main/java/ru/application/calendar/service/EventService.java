package ru.application.calendar.service;

import ru.application.calendar.domain.dto.EventDto;
import ru.application.calendar.domain.entity.EventEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventService {

    List<EventEntity> findAll();

    Optional<EventEntity> findById(Long id);

    Optional<EventDto> findByIdDto(Long id);

    List<EventEntity> findAllByDateFromAndDateTo(LocalDateTime dateFrom, LocalDateTime dateTo);

    boolean save(EventDto eventDto);

    void deleteById(Long id);

}
