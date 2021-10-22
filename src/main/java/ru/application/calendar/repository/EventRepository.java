package ru.application.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.application.calendar.domain.entity.EventEntity;
import ru.application.calendar.domain.entity.RoomEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    List<EventEntity> findAllByDateFromGreaterThanEqualAndDateToLessThanEqual(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<EventEntity> findAllByDateFromLessThanEqualAndDateToGreaterThanEqualAndRoom(LocalDateTime dateFrom, LocalDateTime dateTo, RoomEntity room);
}
