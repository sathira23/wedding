package com.wedding.dao;

import com.wedding.model.Venue;

import java.util.List;
import java.util.Optional;

public interface VenueDao {
    List<Venue> findAll();
    Optional<Venue> findById(int id);
}
