package com.aspira.calendar.repositories;

import com.aspira.calendar.dto.EventDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventDTO, UUID> {
}
