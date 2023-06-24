package com.example.application;


import lombok.*;
import org.springframework.data.annotation.Id;



@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Item {

    public Item() {}

    @NonNull
    private String name;

    private int price;
}
