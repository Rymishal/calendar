package com.aspira.calendar.services;

import com.aspira.calendar.bom.Event;
import com.aspira.calendar.convertors.EventConvertor;
import com.aspira.calendar.dto.EventDTO;
import com.aspira.calendar.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    //TODO: Add exceptions

    private final EventRepository eventRepository;

    private final EventConvertor eventConvertor = new EventConvertor();

    public Event create(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventConvertor.toDTO(event, eventDTO);
        eventDTO.setId(UUID.randomUUID());
        eventDTO = eventRepository.save(eventDTO);
        eventConvertor.fromDTO(eventDTO, event);
        return event;
    }

    public List<Event> findAll() {
        List<EventDTO> eventDTOS = eventRepository.findAll();
        List<Event> events = new ArrayList<>();
        eventDTOS.forEach(eventDTO -> {
            Event event = new Event();
            eventConvertor.fromDTO(eventDTO, event);
            events.add(event);
        });
        return events;
    }

    public Event findById(UUID id) {
        EventDTO eventDTO = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no event with id: " + id));
        Event event = new Event();
        eventConvertor.fromDTO(eventDTO, event);
        return event;
    }

    public Event update(UUID id, Event event) {
        EventDTO eventDTO = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no event with id: " + id));
        eventConvertor.toDTO(event, eventDTO);
        eventDTO = eventRepository.save(eventDTO);
        eventConvertor.fromDTO(eventDTO, event);
        return event;
    }

    public void delete(UUID id) {
        EventDTO eventDTO = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no event with id: " + id));
        eventRepository.deleteById(id);
    }
}
