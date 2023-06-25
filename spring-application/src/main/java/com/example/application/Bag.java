package com.example.application;


import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "bags")
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Bag {

    public Bag() {
    }

    @BsonProperty("_id")
    private ObjectId id;

    private String owner;

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

