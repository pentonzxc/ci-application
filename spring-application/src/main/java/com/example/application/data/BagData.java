package com.example.application.data;

import com.example.application.model.Bag;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;


public record BagData(@JsonProperty("owner") String owner, @JsonProperty("items") List<ItemData> items) {

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
