package com.wedding.dao;

import com.wedding.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventDao {
    List<Event> findAll();
    Optional<Event> findById(int id);
}
