package ru.application.calendar.domain.converter;

import ru.application.calendar.domain.dto.EventDto;
import ru.application.calendar.domain.dto.UserDto;
import ru.application.calendar.domain.entity.EventEntity;
import ru.application.calendar.service.RoomService;
import ru.application.calendar.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EventConverter {

    public static Set<EventDto> convertToDto(Set<EventEntity> entities) {
        return entities.stream()
                .map(EventConverter::convertToDto)
                .collect(Collectors.toSet());
    }

    public static EventDto convertToDto(EventEntity entity) {
        return EventDto
                .builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .dateFrom(entity.getDateFrom().toString())
                .dateTo(entity.getDateTo().toString())
                .room(entity.getRoom().toString())
                .author(entity.getAuthor().getUsername())
                .build();
    }

}
