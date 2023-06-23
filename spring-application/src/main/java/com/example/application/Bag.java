package com.example.application;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "bags")
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bag {


    @Id
    String id;

    @Getter
    @NonNull String owner;

    @NonNull List<Item> items;

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

