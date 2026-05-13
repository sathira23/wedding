package com.wedding.dao;

import com.wedding.model.WeddingPackage;

import java.util.List;
import java.util.Optional;

public interface PackageDao {
    List<WeddingPackage> findAll();
    List<WeddingPackage> findByOrganizerId(int organizerId);
    Optional<WeddingPackage> findById(int id);
    Optional<WeddingPackage> findByName(String name);
    int insert(WeddingPackage weddingPackage);
    boolean update(WeddingPackage weddingPackage);
    boolean delete(int id);
}
