package com.example.application.service;

import com.example.application.model.Bag;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.*;
import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BagService {

    final MongoCollection<Bag> bagCollection;

    final ObjectMapper objectMapper;


    public Mono<String> create(Bag bag) {
        return Mono.from(bagCollection.insertOne(bag))
                .flatMap(result -> {
                    Optional<String> idOpt = Optional.ofNullable(result.getInsertedId()).map(id -> id.asObjectId().getValue().toString());
                    return idOpt.map(Mono::just).orElseGet(() -> Mono.error(new IllegalArgumentException()));
                });
    }


    public Mono<Bag> update(Bag bag) {
        return Mono.just(bag)
                .map((newBag) -> {
                    List<Bson> updates = new ArrayList<>();
                    if (newBag.getItems() != null) {
                        updates.add(Updates.pushEach("items", newBag.getItems()));
                    }
                    if (newBag.getOwner() != null) {
                        updates.add(Updates.set("owner", newBag.getOwner()));
                    }
                    return Updates.combine(updates);
                })
                .flatMap(updates -> Mono.from(bagCollection.findOneAndUpdate(Filters.eq("_id", bag.getId()), updates)));
    }


    public Mono<Void> delete(String id) {
        return Mono.from(bagCollection.deleteOne(Filters.eq("_id", new ObjectId(id))))
                .then();
    }


    public Mono<Bag> find(String id) {
        return Mono.from(bagCollection.find(Filters.eq("_id", new ObjectId(id))).first());
    }


    public Flux<Bag> findAll() {
        return Flux.from(bagCollection.find());
    }


}
