package com.example.application;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Setter
@Getter
public class Item {

    @Id String id;

    @NonNull String name;

    int price;
}
