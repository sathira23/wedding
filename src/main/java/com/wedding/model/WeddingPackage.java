package com.wedding.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WeddingPackage {
    private int id;
    private String name;
    private String description;
    private PackageTier tier;
    private BigDecimal price;
    private String inclusions;
    private PackageStatus status;
    private Integer linkedEventId;
    private Integer linkedVenueId;
    private String linkedEventName;
    private String linkedVenueName;
    private int organizerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PackageTier getTier() {
        return tier;
    }

    public void setTier(PackageTier tier) {
        this.tier = tier;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public PackageStatus getStatus() {
        return status;
    }

    public void setStatus(PackageStatus status) {
        this.status = status;
    }

    public Integer getLinkedEventId() {
        return linkedEventId;
    }

    public void setLinkedEventId(Integer linkedEventId) {
        this.linkedEventId = linkedEventId;
    }

    public Integer getLinkedVenueId() {
        return linkedVenueId;
    }

    public void setLinkedVenueId(Integer linkedVenueId) {
        this.linkedVenueId = linkedVenueId;
    }

    public String getLinkedEventName() {
        return linkedEventName;
    }

    public void setLinkedEventName(String linkedEventName) {
        this.linkedEventName = linkedEventName;
    }

    public String getLinkedVenueName() {
        return linkedVenueName;
    }

    public void setLinkedVenueName(String linkedVenueName) {
        this.linkedVenueName = linkedVenueName;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
