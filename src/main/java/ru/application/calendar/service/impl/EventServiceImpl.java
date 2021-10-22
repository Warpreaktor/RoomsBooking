package ru.application.calendar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import ru.application.calendar.domain.dto.EventDto;
import ru.application.calendar.domain.dto.UserDto;
import ru.application.calendar.domain.entity.EventEntity;
import ru.application.calendar.domain.entity.RoomEntity;
import ru.application.calendar.domain.entity.UserEntity;
import ru.application.calendar.repository.EventRepository;
import ru.application.calendar.service.EventService;
import ru.application.calendar.service.RoomService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final DateTimeFormatter fullDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;

    private final RoomService roomService;

    private final UserServiceImpl userService;

    @Override
    public List<EventEntity> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<EventEntity> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Optional<EventDto> findByIdDto(Long id) {
        Optional<EventEntity> eventEntity = eventRepository.findById(id);
        if (eventEntity.isPresent()) {
            EventEntity event = eventEntity.get();
            return Optional.of(
                    EventDto.builder()
                            .id(event.getId())
                            .title(event.getTitle())
                            .description(event.getDescription())
                            .dateFrom(event.getDateFrom().toLocalDate() + "T" + event.getDateFrom().toLocalTime())
                            .dateTo(event.getDateTo().toLocalDate() + "T" + event.getDateTo().toLocalTime())
                            .room(event.getRoom().getName())
                            .author(event.getAuthor().getUsername())
                            .build());
        }
        return Optional.empty();
    }


    @Override
    public List<EventEntity> findAllByDateFromAndDateTo(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return eventRepository.findAllByDateFromGreaterThanEqualAndDateToLessThanEqual(dateFrom, dateTo);
    }

    @Override
    public boolean save(EventDto dto) {
        //Check that the saved event does not overlap with another one by date and room.
        Optional<RoomEntity> roomEntity = roomService.findByName(dto.getRoom());
        List<EventEntity> events = eventRepository
                .findAllByDateFromLessThanEqualAndDateToGreaterThanEqualAndRoom
                        (LocalDateTime.parse(dto.getDateTo()), LocalDateTime.parse(dto.getDateFrom()), roomEntity.get());
        if (events.size() > 0 && dto.getId() == null) {
            return false;
        } else {
            Optional<UserDto> userDto = userService.findByUsername(dto.getAuthor());
            eventRepository.save(EventEntity.builder()
                    .id(dto.getId())
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .dateFrom(LocalDateTime.parse(dto.getDateFrom()))
                    .dateTo(LocalDateTime.parse(dto.getDateTo()))
                    .room(roomEntity.get())
                    .author(userService.findById(userDto.get().getId()).get())
                    .build());
            return true;
        }
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }
}
