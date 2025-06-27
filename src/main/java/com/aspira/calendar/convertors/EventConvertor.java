package com.aspira.calendar.convertors;

import com.aspira.calendar.bom.Event;
import com.aspira.calendar.dto.EventDTO;

public class EventConvertor {
    public void toDTO(Event source, EventDTO destination){
        destination.setId(source.getId());
        destination.setTitle(source.getTitle());
        destination.setDescription(source.getDescription());
        destination.setStartDateTime(source.getStartDateTime());
        destination.setEndDateTime(source.getEndDateTime());
        destination.setLocation(source.getLocation());
    }

    public void fromDTO(EventDTO source, Event destination){
        destination.setId(source.getId());
        destination.setTitle(source.getTitle());
        destination.setDescription(source.getDescription());
        destination.setStartDateTime(source.getStartDateTime());
        destination.setEndDateTime(source.getEndDateTime());
        destination.setLocation(source.getLocation());
    }
}
