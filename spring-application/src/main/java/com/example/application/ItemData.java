package com.example.application;

public record ItemData(String name, int price) {

    public static Item toEntity(ItemData itemData) {
        return Item.builder()
                .price(itemData.price)
                .name(itemData.name)
                .build();
    }
}
