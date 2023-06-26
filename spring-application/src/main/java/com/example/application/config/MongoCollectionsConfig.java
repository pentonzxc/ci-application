package com.example.application.config;


import com.example.application.model.Bag;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Lazy
@RequiredArgsConstructor
public class MongoCollectionsConfig {

    final MongoClient mongoClient;

    @Bean
    MongoCollection<Bag> bagCollection() {
        return mongoClient.getDatabase("ci-app").getCollection("bags", Bag.class);
    }
}
