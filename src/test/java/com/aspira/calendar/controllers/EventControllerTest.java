package com.aspira.calendar.controllers;

import com.aspira.calendar.bom.Event;
import com.aspira.calendar.exception.EventNotFoundException;
import com.aspira.calendar.exception.ForbiddenEventModificationException;
import com.aspira.calendar.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @MockitoBean
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");

    private final String url = "http://localhost:8081/api";

    @Test
    public void shouldCreateEvent() throws Exception {
        Event event = createEvent(UUID.randomUUID());
        when(eventService.create(eq(event))).thenReturn(event);
        mockMvc.perform(MockMvcRequestBuilders.post(url + "/events").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))).andExpect(status().isOk());
    }

    @Test
    void shouldFindAll() throws Exception {
        List<Event> eventList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            UUID eventId = UUID.randomUUID();
            Event event = createEvent(eventId);
            eventList.add(event);
        }
        when(eventService.findAll()).thenReturn(eventList);
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/events").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindById() throws Exception {
        UUID eventId = UUID.randomUUID();
        Event event = createEvent(eventId);
        when(eventService.findById(eq(eventId))).thenReturn(event);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/events/" + eventId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404CodeWhileFindById() throws Exception {
        UUID eventId = UUID.randomUUID();
        when(eventService.findById(eq(eventId)))
                .thenThrow(new EventNotFoundException("Event with id: " + eventId + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/events/" + eventId))
                .andExpect(status().is(404));
    }

    @Test
    void shouldUpdate() throws Exception {
        UUID eventId = UUID.randomUUID();
        Event event = createEvent(eventId);
        when(eventService.update(eq(eventId), eq(event))).thenReturn(event);
        mockMvc.perform(MockMvcRequestBuilders.put(url + "/events/" + eventId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUpdate() throws Exception {
        UUID eventId = UUID.randomUUID();
        Event event = createEvent(eventId);
        when(eventService.update(any(), eq(event))).thenThrow(new ForbiddenEventModificationException(""));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "/events/" + eventId).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().is(403));
    }

    @Test
    void shouldDelete() throws Exception {
        UUID eventId = UUID.randomUUID();
        doNothing().when(eventService).delete(eq(eventId));
        mockMvc.perform(MockMvcRequestBuilders.delete(url + "/events/" + eventId))
                .andExpect(status().isOk());
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
}