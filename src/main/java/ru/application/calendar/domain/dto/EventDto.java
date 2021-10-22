package ru.application.calendar.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    Long  id;
    String title;
    String description;
    String dateFrom;
    String dateTo;
    String room;
    String author;
}
