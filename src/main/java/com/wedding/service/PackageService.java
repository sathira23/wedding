package com.wedding.service;

import com.wedding.dao.EventDao;
import com.wedding.dao.PackageDao;
import com.wedding.dao.VenueDao;
import com.wedding.exception.DataAccessException;
import com.wedding.model.Event;
import com.wedding.model.PackageStatus;
import com.wedding.model.PackageTier;
import com.wedding.model.User;
import com.wedding.model.Venue;
import com.wedding.model.WeddingPackage;
import com.wedding.util.ValidationUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service layer in MVC. This class encapsulates business rules, validation logic,
 * and the coordination between controller servlets and DAO persistence.
 */
public class PackageService {
    private static final Logger LOGGER = Logger.getLogger(PackageService.class.getName());

    private final PackageDao packageDao;
    private final EventDao eventDao;
    private final VenueDao venueDao;

    public PackageService(PackageDao packageDao, EventDao eventDao, VenueDao venueDao) {
        this.packageDao = packageDao;
        this.eventDao = eventDao;
        this.venueDao = venueDao;
    }

    public List<WeddingPackage> getPackages(User user) {
        if (user.isAdmin()) {
            return packageDao.findAll();
        }
        return packageDao.findByOrganizerId(user.getId());
    }

    public Optional<WeddingPackage> findPackage(int id) {
        return packageDao.findById(id);
    }

    public List<Event> getEvents() {
        try {
            return eventDao.findAll();
        } catch (RuntimeException ex) {
            LOGGER.log(Level.WARNING, "Unable to load events", ex);
            return Collections.emptyList();
        }
    }

    public List<Venue> getVenues() {
        try {
            return venueDao.findAll();
        } catch (RuntimeException ex) {
            LOGGER.log(Level.WARNING, "Unable to load venues", ex);
            return Collections.emptyList();
        }
    }

    public Map<String, String> validateAndSave(User currentUser, WeddingPackage weddingPackage, String name, String description,
                                               String tier, String price, String inclusions, String status,
                                               String linkedEventId, String linkedVenueId) {
        Map<String, String> errors = ValidationUtil.validatePackageForm(name, description, tier, price, inclusions, status, linkedEventId, linkedVenueId);

        if (!errors.isEmpty()) {
            return errors;
        }

        Optional<WeddingPackage> existing = packageDao.findByName(name.trim());
        if (existing.isPresent() && existing.get().getId() != weddingPackage.getId()) {
            errors.put("name", "A package with that name already exists.");
            return errors;
        }

        Integer eventId = ValidationUtil.parseInt(linkedEventId);
        Integer venueId = ValidationUtil.parseInt(linkedVenueId);

        if (eventId != null && venueId != null) {
            errors.put("linkedTarget", "A package may only be linked to an event or a venue, not both.");
            return errors;
        }

        if (eventId != null) {
            if (eventDao.findById(eventId).isEmpty()) {
                errors.put("linkedEventId", "Selected event does not exist.");
                return errors;
            }
        }

        if (venueId != null) {
            if (venueDao.findById(venueId).isEmpty()) {
                errors.put("linkedVenueId", "Selected venue does not exist.");
                return errors;
            }
        }

        weddingPackage.setName(name.trim());
        weddingPackage.setDescription(description.trim());
        weddingPackage.setTier(PackageTier.valueOf(tier.toUpperCase()));
        weddingPackage.setPrice(new BigDecimal(price.trim()));
        weddingPackage.setInclusions(inclusions.trim());
        weddingPackage.setStatus(PackageStatus.valueOf(status.toUpperCase()));
        weddingPackage.setLinkedEventId(eventId);
        weddingPackage.setLinkedVenueId(venueId);

        if (eventId != null) {
            weddingPackage.setLinkedVenueId(null);
        } else if (venueId != null) {
            weddingPackage.setLinkedEventId(null);
        }

        try {
            if (weddingPackage.getId() == 0) {
                weddingPackage.setOrganizerId(currentUser.getId());
                weddingPackage.setCreatedAt(LocalDateTime.now());
                weddingPackage.setUpdatedAt(LocalDateTime.now());
                packageDao.insert(weddingPackage);
            } else {
                weddingPackage.setUpdatedAt(LocalDateTime.now());
                if (!currentUser.isAdmin() && weddingPackage.getOrganizerId() != currentUser.getId()) {
                    errors.put("auth", "You do not have permission to edit this package.");
                    return errors;
                }
                if (!packageDao.update(weddingPackage)) {
                    errors.put("general", "Package update failed. Please try again or contact support.");
                    return errors;
                }
            }
        } catch (RuntimeException ex) {
            LOGGER.log(Level.SEVERE, "Package persistence error", ex);
            throw new DataAccessException("Failed to save package details. Please verify the data and try again.", ex);
        }
        return errors;
    }

    public boolean deletePackage(User currentUser, int id) {
        Optional<WeddingPackage> optional = packageDao.findById(id);
        if (optional.isEmpty()) {
            return false;
        }
        WeddingPackage weddingPackage = optional.get();
        if (!currentUser.isAdmin() && weddingPackage.getOrganizerId() != currentUser.getId()) {
            return false;
        }
        return packageDao.delete(id);
    }

    public boolean canManage(User currentUser, WeddingPackage weddingPackage) {
        if (weddingPackage == null) {
            return false;
        }
        return currentUser.isAdmin() || weddingPackage.getOrganizerId() == currentUser.getId();
    }

    private Integer parseNullableInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
