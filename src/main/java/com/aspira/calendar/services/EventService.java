package com.aspira.calendar.services;

import com.aspira.calendar.bom.Event;
import com.aspira.calendar.convertors.EventConvertor;
import com.aspira.calendar.dto.EventDTO;
import com.aspira.calendar.exception.EventNotFoundException;
import com.aspira.calendar.exception.ForbiddenEventModificationException;
import com.aspira.calendar.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

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
        EventDTO eventDTO = getEventDTOById(id);
        Event event = new Event();
        eventConvertor.fromDTO(eventDTO, event);
        return event;
    }

    public Event update(UUID id, Event event) {
        EventDTO eventDTO = getEventDTOById(id);
        eventConvertor.toDTO(event, eventDTO);
        try {
            eventDTO = eventRepository.save(eventDTO);
        } catch (JpaSystemException e) {
            throw new ForbiddenEventModificationException(e.getMessage());
        }
        eventConvertor.fromDTO(eventDTO, event);
        return event;
    }

    public void delete(UUID id) {
        getEventDTOById(id);
        eventRepository.deleteById(id);
    }

    private EventDTO getEventDTOById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id.toString()));
    }
}
