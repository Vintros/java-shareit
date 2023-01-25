package ru.practicum.shareitg.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentDto {
    @NotBlank
    private String text;
}
