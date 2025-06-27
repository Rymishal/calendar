package com.aspira.calendar.bom;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class Event {
    private UUID id;
    private String title;
    private String description;
    private Date startDateTime;
    private Date endDateTime;
    private String location;
}
