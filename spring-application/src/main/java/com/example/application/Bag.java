package com.example.application;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Document(collection = "bags")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Bag {

    @Id
    private String id;

    @NonNull
    private String owner;

    @NonNull
    private List<Item> items;

    public int itemsCount() {
        return items.size();
    }

    public int itemsPrice() {
        return items.stream()
                .map(Item::getPrice)
                .reduce(Integer::sum)
                .orElse(0);
    }

}

