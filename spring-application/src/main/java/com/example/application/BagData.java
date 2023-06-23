package com.example.application;

import java.util.List;
import java.util.stream.Collectors;

public record BagData(String owner, List<ItemData> items) {

    public static Bag toEntity(BagData bagData) {
//  @formatter:off
        return Bag.builder()
                .owner(bagData.owner)
                .items(bagData.items.stream()
                        .map((ItemData::toEntity))
                        .collect(Collectors.toList()))
                .build();
//  @formatter:on
    }
}
