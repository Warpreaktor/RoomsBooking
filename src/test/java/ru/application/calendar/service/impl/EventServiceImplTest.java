package ru.application.calendar.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.application.calendar.CalendarApplicationTests;
import ru.application.calendar.domain.dto.EventDto;
import ru.application.calendar.domain.entity.EventEntity;
import ru.application.calendar.domain.entity.RoomEntity;
import ru.application.calendar.domain.entity.UserEntity;
import ru.application.calendar.repository.EventRepository;
import ru.application.calendar.repository.RoomRepository;
import ru.application.calendar.repository.UserRepository;
import ru.application.calendar.service.EventService;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceImplTest extends CalendarApplicationTests {

    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoomRepository roomService;

    private RoomEntity room1;

    private UserEntity user1;

    private EventDto eventDto1;

    private List events;

    String dateFrom1;
    String dateTo1;

    @BeforeEach()
    void setUp() {
        events = getEvents1();

        String dateFrom1 = "2021-10-22T00:00:00";
        String dateTo1 = "2021-10-22T04:00:00";
        String dateFrom2 = "2021-10-22T05:00:00";
        String dateTo2 = "2021-10-22T06:00:00";

        room1 = new RoomEntity(
                1L,
                "Комната1",
                new HashSet<>());

        user1 = new UserEntity(
                1L,
                "user1",
                "$2a$12$KOp5TtlWiRvv2xz90F9C2Oj8SyfDIk45ZcA7rY7ChCZXr/sWbKSUe",
                "$2a$12$KOp5TtlWiRvv2xz90F9C2Oj8SyfDIk45ZcA7rY7ChCZXr/sWbKSUe",
                "user@mail.ru",
                new HashSet<>(),
                new ArrayList<>());

        eventDto1 = new EventDto(1L,
                "TestTitle",
                "TestDescription",
                dateFrom1,
                dateTo1,
                "room1",
                "user1");

        Mockito.when(roomService.findByName("room1")).thenReturn(Optional.of(room1));
        Mockito.when(eventRepository.findAllByDateFromLessThanEqualAndDateToGreaterThanEqualAndRoom
                        (LocalDateTime.parse(dateFrom1), LocalDateTime.parse(dateTo1), room1))
                .thenReturn(events);
        Mockito.when(eventRepository.findAllByDateFromLessThanEqualAndDateToGreaterThanEqualAndRoom
                        (LocalDateTime.parse(dateFrom2), LocalDateTime.parse(dateTo2), room1))
                .thenReturn(new ArrayList<>());

        Mockito.when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));

    }

    List<EventEntity> getEvents1(){
        List<EventEntity> events = new ArrayList<>();

        EventEntity event1 = new EventEntity(1L,
                "TestTitle",
                "TestDescription",
                LocalDateTime.parse("2021-10-22T00:00:00"),
                LocalDateTime.parse("2021-10-22T04:00:00"),
                user1,
                room1);

        events.add(event1);
        return events;
    }

    @Test
    void save() {
        EventDto event1 = new EventDto(2L,
                "TestTitle",
                "TestDescription",
                "2021-10-22T00:00:00",
                "2021-10-22T04:00:00",
                "room1",
                "user1");
        EventDto event2 = new EventDto(2L,
                "TestTitle",
                "TestDescription",
                "2021-10-22T05:00:00",
                "2021-10-22T06:00:00",
                "room1",
                "user1");
        EventDto event3 = new EventDto(null,
                "TestTitle",
                "TestDescription",
                "2021-10-22T05:00:00",
                "2021-10-22T06:00:00",
                "room1",
                "user1");


        Assertions.assertTrue(eventService.save(event2));
    }
}