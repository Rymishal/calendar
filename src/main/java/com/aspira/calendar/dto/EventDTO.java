package com.aspira.calendar.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity(name = "Event")
public class EventDTO {

    @Id
    @Column(name = "ID", updatable = false)
    private UUID id;

    private String title;
    private String description;

    @Column(name = "start_date_time")
    private Date startDateTime;

    @Column(name = "end_date_time")
    private Date endDateTime;

    private String location;
}
