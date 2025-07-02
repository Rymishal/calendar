package com.aspira.calendar.services;

import com.aspira.calendar.bom.Event;
import com.aspira.calendar.convertors.EventConvertor;
import com.aspira.calendar.dto.EventDTO;
import com.aspira.calendar.exception.EventNotFoundException;
import com.aspira.calendar.exception.ForbiddenEventModificationException;
import com.aspira.calendar.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.jpa.JpaSystemException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Spy
    private EventConvertor convertor;

    @InjectMocks
    private EventService eventService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");

    @Test
    public void shouldCreate() throws ParseException {
        UUID eventId = UUID.randomUUID();
        EventDTO eventDTO = createEventDTO(eventId);
        when(eventRepository.save(any(EventDTO.class))).thenReturn(eventDTO);

        Event event = createEvent(eventId);
        Event result = eventService.create(event);
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getDescription(), result.getDescription());
        assertEquals(event.getStartDateTime(), result.getStartDateTime());
        assertEquals(event.getEndDateTime(), result.getEndDateTime());
        assertEquals(event.getLocation(), result.getLocation());
    }

    @Test
    public void shouldFindAll() throws ParseException {
        List<EventDTO> eventDTOList = new ArrayList<>();
        List<Event> eventList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            UUID eventId = UUID.randomUUID();
            EventDTO eventDTO = createEventDTO(eventId);
            eventDTOList.add(eventDTO);
            Event event = createEvent(eventId);
            eventList.add(event);
        }
        when(eventRepository.findAll()).thenReturn(eventDTOList);
        List<Event> result = eventService.findAll();
        assertNotNull(result);
        assertEquals(eventDTOList.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(eventList.get(i).getId(), result.get(i).getId());
            assertEquals(eventList.get(i).getTitle(), result.get(i).getTitle());
            assertEquals(eventList.get(i).getDescription(), result.get(i).getDescription());
            assertEquals(eventList.get(i).getStartDateTime(), result.get(i).getStartDateTime());
            assertEquals(eventList.get(i).getEndDateTime(), result.get(i).getEndDateTime());
            assertEquals(eventList.get(i).getLocation(), result.get(i).getLocation());
        }
    }

    @Test
    public void shouldFindById() throws ParseException {
        UUID eventId = UUID.randomUUID();
        EventDTO eventDTO = createEventDTO(eventId);
        when(eventRepository.findById(eq(eventId))).thenReturn(Optional.of(eventDTO));

        Event event = createEvent(eventId);
        Event result = eventService.findById(eventId);
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getDescription(), result.getDescription());
        assertEquals(event.getStartDateTime(), result.getStartDateTime());
        assertEquals(event.getEndDateTime(), result.getEndDateTime());
        assertEquals(event.getLocation(), result.getLocation());
    }

    @Test
    public void shouldThrowEventNotFoundExceptionWhileFindById() {
        UUID eventId = UUID.randomUUID();
        when(eventRepository.findById(eq(eventId))).thenReturn(Optional.empty());

        EventNotFoundException eventNotFoundException = assertThrows(EventNotFoundException.class, () -> eventService.findById(eventId));

        assertNotNull(eventNotFoundException);
        assertEquals("Event with id: " + eventId + " not found", eventNotFoundException.getMessage());
    }

    @Test
    public void shouldUpdate() throws ParseException {
        UUID eventId = UUID.randomUUID();
        EventDTO eventDTO = createEventDTO(eventId);
        when(eventRepository.findById(eq(eventId))).thenReturn(Optional.of(eventDTO));

        EventDTO newEventDTO = createEventDTO(eventDTO);
        newEventDTO.setTitle("New Title");

        when(eventRepository.save(eq(newEventDTO))).thenReturn(eventDTO);

        Event event = createEvent(eventId);
        event.setTitle("New Title");

        Event result = eventService.update(eventId, event);
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getDescription(), result.getDescription());
        assertEquals(event.getStartDateTime(), result.getStartDateTime());
        assertEquals(event.getEndDateTime(), result.getEndDateTime());
        assertEquals(event.getLocation(), result.getLocation());
    }

    @Test
    public void shouldThrowForbiddenEventModificationExceptionWhileUpdate() throws ParseException {
        UUID eventId = UUID.randomUUID();
        EventDTO eventDTO = createEventDTO(eventId);
        when(eventRepository.findById(eq(eventId))).thenReturn(Optional.of(eventDTO));
        UUID newId = UUID.randomUUID();
        when(eventRepository.save(eq(eventDTO)))
                .thenThrow(
                        new JpaSystemException(new RuntimeException("identifier of an instance of com.aspira.calendar.dto.EventDTO was altered from " +
                                eventId +  " to " + newId)));

        Event event = createEvent(newId);

        ForbiddenEventModificationException forbiddenEventModificationException
                = assertThrows(ForbiddenEventModificationException.class, () -> eventService.update(eventId, event));

        assertNotNull(forbiddenEventModificationException);
        assertEquals("identifier of an instance of com.aspira.calendar.dto.EventDTO was altered from " +
                eventId +  " to " + newId, forbiddenEventModificationException.getMessage());
    }

    @Test
    public void shouldDelete() throws ParseException {
        UUID eventId = UUID.randomUUID();
        EventDTO eventDTO = createEventDTO(eventId);
        when(eventRepository.findById(eq(eventId))).thenReturn(Optional.of(eventDTO));

        eventService.delete(eventId);

        verify(eventRepository).deleteById(eq(eventId));
    }

    private EventDTO createEventDTO(UUID id) throws ParseException {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(id);
        eventDTO.setTitle("Event Title");
        eventDTO.setDescription("Event Description");
        eventDTO.setStartDateTime(dateFormat.parse("2025-07-04T14:00:00.000"));
        eventDTO.setEndDateTime(dateFormat.parse("2025-07-05T14:00:00.000"));
        eventDTO.setLocation("Event Location");

        return eventDTO;
    }

    private Event createEvent(UUID id) throws ParseException {
        Event event = new Event();
        event.setId(id);
        event.setTitle("Event Title");
        event.setDescription("Event Description");
        event.setStartDateTime(dateFormat.parse("2025-07-04T14:00:00.000"));
        event.setEndDateTime(dateFormat.parse("2025-07-05T14:00:00.000"));
        event.setLocation("Event Location");

        return event;
    }

    private EventDTO createEventDTO(EventDTO eventDTO) {
        EventDTO newEventDTO = new EventDTO();
        newEventDTO.setId(eventDTO.getId());
        newEventDTO.setTitle(eventDTO.getTitle());
        newEventDTO.setDescription(eventDTO.getDescription());
        newEventDTO.setStartDateTime(eventDTO.getStartDateTime());
        newEventDTO.setEndDateTime(eventDTO.getEndDateTime());
        newEventDTO.setLocation(eventDTO.getLocation());

        return newEventDTO;
    }
}