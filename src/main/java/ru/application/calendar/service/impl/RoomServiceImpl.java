package ru.application.calendar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.calendar.domain.entity.RoomEntity;
import ru.application.calendar.repository.RoomRepository;
import ru.application.calendar.service.RoomService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl  implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<RoomEntity> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<RoomEntity> findById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public Optional<RoomEntity> findByName(String name) {
        return roomRepository.findByName(name);
    }
}
