package dev.irontech.bookstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public enum Events {
    @JsonProperty("order_created")
    ORDER_CREATED("order_created")
    ;

    @Getter
    private final String name;

    Events(String name) {
        this.name = name;
    }

    @JsonCreator
    @Override
    public String toString() {
        return name;
    }
}
