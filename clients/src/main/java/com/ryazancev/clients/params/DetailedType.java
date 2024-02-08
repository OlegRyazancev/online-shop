package com.ryazancev.clients.params;


import lombok.Getter;

@Getter
public enum DetailedType {

    DETAILED("detailed"),
    SIMPLE("simple");

    private final String type;

    DetailedType(String type) {
        this.type = type;
    }

    public static DetailedType fromString(String text) {
        for (DetailedType value : DetailedType.values()) {
            if (value.type.equalsIgnoreCase(text)) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "No constant with type " + text + " found");
    }
}
