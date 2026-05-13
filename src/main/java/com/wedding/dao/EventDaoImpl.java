package com.wedding.dao;

import com.wedding.config.DBConnection;
import com.wedding.model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventDaoImpl implements EventDao {
    private static final String SELECT_ALL = "SELECT id, name FROM event ORDER BY name";
    private static final String SELECT_BY_ID = "SELECT id, name FROM event WHERE id = ?";

    @Override
    public List<Event> findAll() {
        List<Event> items = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                items.add(new Event(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to retrieve events", ex);
        }
        return items;
    }

    @Override
    public Optional<Event> findById(int id) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Event(rs.getInt("id"), rs.getString("name")));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to retrieve event record", ex);
        }
        return Optional.empty();
    }
}
