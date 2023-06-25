package com.example.application;

import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;


public record BagData(String owner, List<ItemData> items) {

    public static Bag toEntity(BagData bagData) {
//  @formatter:off
        return Bag.builder()
                .owner(bagData.owner)
                .items(bagData.items != null ? bagData.items.stream()
                        .map((ItemData::toEntity))
                        .collect(Collectors.toList()) : null)
                .build();
//  @formatter:on
    }
}
