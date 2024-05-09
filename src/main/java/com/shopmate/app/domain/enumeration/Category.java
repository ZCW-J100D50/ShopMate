package com.shopmate.app.domain.enumeration;

/**
 * The Category enumeration.
 */
public enum Category {
    FOOD("Food"),
    SUPPLIES("Supplies"),
    OTHER("Other");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
