package dev.irontech.bookstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    @JsonProperty("created")
    CREATED("created"),
    UNKNOWN("unknown")
    ;

    private final String name;
    OrderStatus(String name) {
        this.name = name;
    }

    @JsonCreator
    @Override
    public String toString() {
        return name;
    }
}
