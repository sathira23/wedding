package com.wedding.model;

public enum PackageStatus {
    DRAFT,
    ACTIVE,
    ARCHIVED;

    public static boolean contains(String value) {
        if (value == null) {
            return false;
        }
        try {
            PackageStatus.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
