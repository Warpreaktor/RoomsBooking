package ru.application.calendar.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.application.calendar.domain.dto.EventDto;
import ru.application.calendar.domain.entity.EventEntity;
import ru.application.calendar.service.EventService;
import ru.application.calendar.service.RoomService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/calendar")
public class EventController {

    private final EventService eventService;

    private final RoomService roomService;

    private final DateTimeFormatter fullDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter dayTime = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //TODO Создание списка событий на один день находится в разработке.
    @GetMapping("/today/date")
    public String getEventsOnDate(Model model,
                                  @RequestParam(name = "dateFrom") String dateFrom,
                                  @RequestParam(name = "dateTo") String dateTo) {
        LocalDateTime currentTime = LocalDateTime.of(
                LocalDateTime.parse(dateFrom, fullDate).toLocalDate(),
                LocalDateTime.parse(dateTo, fullDate).toLocalTime());
        List<LocalTime> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 31; j += 30) {
                times.add(LocalTime.of(i, j));
            }
        }
        List<EventEntity> events = eventService.findAllByDateFromAndDateTo(
                LocalDateTime.parse(dateFrom, fullDate),
                LocalDateTime.parse(dateTo, fullDate));
        model.addAttribute("currentTime", currentTime);
        model.addAttribute("events", events);
        model.addAttribute("times", times);
        model.addAttribute("fullDate", fullDate);
        model.addAttribute("dayTime", dayTime);
        model.addAttribute("date", date);

        return "/calendar/oneday-full";
    }

    //TODO в разработке
    @GetMapping("/today")
    public String getEventsToday() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(today.toLocalDate(), LocalTime.MIDNIGHT);
        LocalDateTime endOfDay = LocalDateTime.of(today.toLocalDate(), LocalTime.MIDNIGHT.minusSeconds(1));

        return "redirect:/calendar/today/date?dateFrom=" + startOfDay.format(fullDate)
                + "&dateTo=" + endOfDay.format(fullDate);
    }

    /**
     * Determines the date of the current week and redirects the request
     */
    @GetMapping()
    public String getEventsOnCurrentWeek() {
        LocalDateTime startOfDay = LocalDate.now().atTime(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        LocalDateTime startOfWeek = startOfDay;
        for (int i = 0; i < 7; i++) {
            DayOfWeek currentDay = startOfDay.minusDays(i).getDayOfWeek();
            if (currentDay != DayOfWeek.MONDAY){
                continue;
            }
            startOfWeek = startOfDay.minusDays(i);
            break;
        }
        LocalDateTime endOfWeek = startOfWeek.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        List<LocalTime> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 31; j += 30) {
                times.add(LocalTime.of(i, j));
            }
        }

        return "redirect:/calendar/week/date?dateFrom=" + startOfWeek.format(fullDate)
                + "&dateTo=" + endOfWeek.format(fullDate);
    }

    @GetMapping("/week/date")
    public String getEventsOnWeek(Model model,
                                  @RequestParam(name = "dateFrom") String dateFrom,
                                  @RequestParam(name = "dateTo") String dateTo) {
        LocalDateTime startWeek = LocalDateTime.parse(dateFrom, fullDate);
        LocalDateTime endWeek = LocalDateTime.parse(dateTo, fullDate);

        List<LocalTime> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 31; j += 30) {
                times.add(LocalTime.of(i, j));
            }
        }
        List<EventEntity> events = eventService.findAllByDateFromAndDateTo(
                startWeek,
                endWeek);
        model.addAttribute("currentDate", LocalDate.now());
        model.addAttribute("startWeek", startWeek);
        model.addAttribute("endWeek", endWeek);
        model.addAttribute("events", events);
        model.addAttribute("times", times);
        model.addAttribute("fullDate", fullDate);
        model.addAttribute("dayTime", dayTime);
        model.addAttribute("date", date);
        model.addAttribute("eventDto", new EventDto());

        return "/calendar/week";
    }

    @GetMapping("/event")
    public String getEventForm(Model model, @RequestParam(name = "id") Long id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<EventDto> event = eventService.findByIdDto(id);
        model.addAttribute("eventDto", event.get());
        model.addAttribute("userName", userName);
        model.addAttribute("rooms", roomService.findAll());

        return "/events/event-form";
    }

    @GetMapping("/event/delete")
    public String deleteEvent(@RequestParam(name = "id") Long id) {
        eventService.deleteById(id);
        return "redirect:/calendar";
    }

    @PostMapping("/save")
    public String saveEvent(Model model, EventDto eventDto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        eventDto.setAuthor(userName);
        LocalDateTime dateFrom = LocalDateTime.parse(eventDto.getDateFrom());
        LocalDateTime dateTo = LocalDateTime.parse(eventDto.getDateTo());

        if (dateFrom.isAfter(dateTo) ){
            model.addAttribute("dateFromGreaterThanError", true);
            model.addAttribute("eventDto", eventDto);
            model.addAttribute("userName", userName);
            model.addAttribute("rooms", roomService.findAll());
            return "/events/event-form";
        }
        if (dateFrom.isBefore(dateTo.plusDays(1).plusSeconds(1)) ){
            model.addAttribute("greaterThan24hError", true);
            model.addAttribute("eventDto", eventDto);
            model.addAttribute("userName", userName);
            model.addAttribute("rooms", roomService.findAll());
            return "/events/event-form";
        }
        if (eventService.save(eventDto)) {
            return "redirect:/calendar";
        } else {
            model.addAttribute("dateError", true);
            model.addAttribute("eventDto", eventDto);
            model.addAttribute("userName", userName);
            model.addAttribute("rooms", roomService.findAll());
            return "/events/event-form";
        }
    }

}
