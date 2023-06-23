package com.example.application;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "items")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Item {

    @Id
    private String id;

    @NonNull
    private String name;

    private int price;
}
