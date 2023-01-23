package ru.practicum.shareit.common.model;

import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class FromSizeRequest extends PageRequest {

    protected FromSizeRequest(int from, int size, Sort sort) {
        super(from / size, size, sort);
    }

    public static FromSizeRequest of(int from, int size, @NonNull Sort sort) {
        return new FromSizeRequest(from, size, sort);
    }
}
