package com.aspira.calendar.bom;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class Event {
    @JsonView(EventListView.class)
    private UUID id;
    @JsonView(EventListView.class)
    private String title;
    private String description;
    @JsonView(Event.EventListView.class)
    private Date startDateTime;
    @JsonView(Event.EventListView.class)
    private Date endDateTime;
    private String location;

    public static class EventListView {

    }
}
