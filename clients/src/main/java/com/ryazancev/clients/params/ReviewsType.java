package com.ryazancev.clients.params;


import lombok.Getter;

@Getter
public enum ReviewsType {

    WITH_REVIEWS("with_reviews"),

    NO_REVIEWS("no_reviews");


    private final String type;

    ReviewsType(String type) {
        this.type = type;
    }

    public static ReviewsType fromString(String text) {
        for (ReviewsType value : ReviewsType.values()) {
            if (value.type.equalsIgnoreCase(text)) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "No constant with type " + text + " found");
    }
}
