package ru.application.calendar.service;

import ru.application.calendar.domain.entity.RoomEntity;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<RoomEntity> findAll();

    Optional<RoomEntity> findById(Long id);

    Optional<RoomEntity> findByName(String name);

}
