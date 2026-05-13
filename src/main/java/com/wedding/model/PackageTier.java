package com.wedding.model;

public enum PackageTier {
    BASIC,
    PREMIUM,
    LUXURY;

    public static boolean contains(String value) {
        if (value == null) {
            return false;
        }
        try {
            PackageTier.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
