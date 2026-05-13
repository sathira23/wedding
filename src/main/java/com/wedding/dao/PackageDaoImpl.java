package com.wedding.dao;

import com.wedding.config.DBConnection;
import com.wedding.model.PackageStatus;
import com.wedding.model.PackageTier;
import com.wedding.model.WeddingPackage;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO layer in MVC. This class isolates all SQL access for wedding packages and keeps
 * package persistence separate from business rules and controller logic.
 */
public class PackageDaoImpl implements PackageDao {
    private static final String BASE_SELECT = "SELECT p.*, e.name AS linked_event_name, v.name AS linked_venue_name FROM wedding_package p LEFT JOIN event e ON p.linked_event_id = e.id LEFT JOIN venue v ON p.linked_venue_id = v.id";
    private static final String SELECT_ALL = BASE_SELECT + " ORDER BY p.updated_at DESC";
    private static final String SELECT_BY_ORGANIZER = BASE_SELECT + " WHERE p.organizer_id = ? ORDER BY p.updated_at DESC";
    private static final String SELECT_BY_ID = BASE_SELECT + " WHERE p.id = ?";
    private static final String SELECT_BY_NAME = BASE_SELECT + " WHERE LOWER(p.name) = LOWER(?)";
    private static final String INSERT_SQL = "INSERT INTO wedding_package (name, description, tier, price, inclusions, status, linked_event_id, linked_venue_id, organizer_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE wedding_package SET name = ?, description = ?, tier = ?, price = ?, inclusions = ?, status = ?, linked_event_id = ?, linked_venue_id = ?, updated_at = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM wedding_package WHERE id = ?";

    @Override
    public List<WeddingPackage> findAll() {
        List<WeddingPackage> items = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                items.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to retrieve packages", ex);
        }
        return items;
    }

    @Override
    public List<WeddingPackage> findByOrganizerId(int organizerId) {
        List<WeddingPackage> items = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ORGANIZER)) {
            stmt.setInt(1, organizerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to retrieve organizer packages", ex);
        }
        return items;
    }

    @Override
    public Optional<WeddingPackage> findById(int id) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to find package by id", ex);
        }
        return Optional.empty();
    }

    @Override
    public Optional<WeddingPackage> findByName(String name) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_NAME)) {
            stmt.setString(1, name.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to find package by name", ex);
        }
        return Optional.empty();
    }

    @Override
    public int insert(WeddingPackage weddingPackage) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                bindPackage(stmt, weddingPackage);
                LocalDateTime now = LocalDateTime.now();
                stmt.setTimestamp(10, Timestamp.valueOf(now));
                stmt.setTimestamp(11, Timestamp.valueOf(now));
                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    throw new SQLException("Insert failed, no rows affected.");
                }
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        weddingPackage.setId(generatedKeys.getInt(1));
                    }
                }
                conn.commit();
                return weddingPackage.getId();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to insert package", ex);
        }
    }

    @Override
    public boolean update(WeddingPackage weddingPackage) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
                bindUpdate(stmt, weddingPackage);
                stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
                stmt.setInt(10, weddingPackage.getId());
                boolean updated = stmt.executeUpdate() > 0;
                if (updated) {
                    conn.commit();
                } else {
                    conn.rollback();
                }
                return updated;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to update package", ex);
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
                stmt.setInt(1, id);
                boolean deleted = stmt.executeUpdate() > 0;
                if (deleted) {
                    conn.commit();
                } else {
                    conn.rollback();
                }
                return deleted;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to delete package", ex);
        }
    }

    private void bindPackage(PreparedStatement stmt, WeddingPackage weddingPackage) throws SQLException {
        stmt.setString(1, weddingPackage.getName());
        stmt.setString(2, weddingPackage.getDescription());
        stmt.setString(3, weddingPackage.getTier().name());
        stmt.setBigDecimal(4, weddingPackage.getPrice());
        stmt.setString(5, weddingPackage.getInclusions());
        stmt.setString(6, weddingPackage.getStatus().name());
        if (weddingPackage.getLinkedEventId() != null) {
            stmt.setInt(7, weddingPackage.getLinkedEventId());
        } else {
            stmt.setNull(7, Types.INTEGER);
        }
        if (weddingPackage.getLinkedVenueId() != null) {
            stmt.setInt(8, weddingPackage.getLinkedVenueId());
        } else {
            stmt.setNull(8, Types.INTEGER);
        }
        stmt.setInt(9, weddingPackage.getOrganizerId());
    }

    private void bindUpdate(PreparedStatement stmt, WeddingPackage weddingPackage) throws SQLException {
        stmt.setString(1, weddingPackage.getName());
        stmt.setString(2, weddingPackage.getDescription());
        stmt.setString(3, weddingPackage.getTier().name());
        stmt.setBigDecimal(4, weddingPackage.getPrice());
        stmt.setString(5, weddingPackage.getInclusions());
        stmt.setString(6, weddingPackage.getStatus().name());
        if (weddingPackage.getLinkedEventId() != null) {
            stmt.setInt(7, weddingPackage.getLinkedEventId());
        } else {
            stmt.setNull(7, Types.INTEGER);
        }
        if (weddingPackage.getLinkedVenueId() != null) {
            stmt.setInt(8, weddingPackage.getLinkedVenueId());
        } else {
            stmt.setNull(8, Types.INTEGER);
        }
    }

    private WeddingPackage mapRow(ResultSet rs) throws SQLException {
        WeddingPackage item = new WeddingPackage();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setTier(PackageTier.valueOf(rs.getString("tier")));
        item.setPrice(rs.getBigDecimal("price"));
        item.setInclusions(rs.getString("inclusions"));
        item.setStatus(PackageStatus.valueOf(rs.getString("status")));
        int eventId = rs.getInt("linked_event_id");
        item.setLinkedEventId(rs.wasNull() ? null : eventId);
        int venueId = rs.getInt("linked_venue_id");
        item.setLinkedVenueId(rs.wasNull() ? null : venueId);
        item.setLinkedEventName(rs.getString("linked_event_name"));
        item.setLinkedVenueName(rs.getString("linked_venue_name"));
        item.setOrganizerId(rs.getInt("organizer_id"));
        item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        item.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return item;
    }
}
