package com.wedding.util;

import com.wedding.model.PackageStatus;
import com.wedding.model.PackageTier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ValidationUtil {
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static Integer parseInt(String value) {
        if (isEmpty(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal parsePositiveBigDecimal(String value) {
        if (isEmpty(value)) {
            return null;
        }
        try {
            BigDecimal parsed = new BigDecimal(value.trim());
            return parsed.compareTo(BigDecimal.ZERO) > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Map<String, String> validatePackageForm(String name, String description, String tier, String price,
                                                            String inclusions, String status, String linkedEventId,
                                                            String linkedVenueId) {
        Map<String, String> errors = new HashMap<>();

        if (isEmpty(name)) {
            errors.put("name", "Package name is required.");
        } else if (name.trim().length() > 120) {
            errors.put("name", "Package name must be 120 characters or fewer.");
        }

        if (isEmpty(description)) {
            errors.put("description", "Description is required.");
        }

        if (!PackageTier.contains(tier)) {
            errors.put("tier", "Please select a valid package tier.");
        }

        if (isEmpty(price)) {
            errors.put("price", "Price is required.");
        } else if (parsePositiveBigDecimal(price) == null) {
            errors.put("price", "Price must be a valid number greater than zero.");
        }

        if (isEmpty(inclusions)) {
            errors.put("inclusions", "At least one inclusion is required.");
        }

        if (!PackageStatus.contains(status)) {
            errors.put("status", "Please select a valid status.");
        }

        boolean hasLinkedEvent = !isEmpty(linkedEventId);
        boolean hasLinkedVenue = !isEmpty(linkedVenueId);
        if (!hasLinkedEvent && !hasLinkedVenue) {
            errors.put("linkedTarget", "A linked event or venue is required.");
        } else if (hasLinkedEvent && hasLinkedVenue) {
            errors.put("linkedTarget", "Only one linking target may be selected: event or venue.");
        }

        return errors;
    }
}
