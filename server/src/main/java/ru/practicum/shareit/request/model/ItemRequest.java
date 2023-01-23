package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime created;

    @OneToMany
    @JoinColumn(name = "request_id")
    private final List<Item> items = new ArrayList<>();
}
