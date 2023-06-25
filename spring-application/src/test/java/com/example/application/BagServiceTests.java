package com.example.application;


import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;


@Testcontainers
@SpringBootTest
public class BagServiceTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    BagService bagService;

    @Autowired
    MongoCollection<Bag> collection;


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    @AfterEach
    void dropCollection() {
        Mono.from(collection.drop()).subscribe();
    }


    @Test
    void test() {
        StepVerifier.create(bagService.create(Bag.builder().owner("KOLYA").build()))
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        StepVerifier.create(bagService.findAll())
                .expectNextMatches(bag -> {
                    System.out.println(bag);
                    return bag.getOwner().equals("KOLYA");
                })
                .expectComplete()
                .verify();
    }


    @Test
    void whenCreateNewBag_thenReturnObjectIdString() {
        Bag newBag = new Bag();

        StepVerifier.create(bagService.create(newBag))
                .expectNextMatches(ObjectId::isValid)
                .expectComplete()
                .verify();
    }


    @Test
    void whenCreateNewBag_thenFindBagByReturnedId_expectReturnThatBag() {
        Bag newBag = new Bag();

        StepVerifier.create(bagService.create(newBag).doOnNext((id) -> newBag.setId(new ObjectId(id))).flatMap(bagService::find))
                .consumeNextWith(findedBag ->
                        Assertions.assertAll(
                                () -> Assertions.assertNotNull(findedBag),
                                () -> Assertions.assertEquals(newBag.getId(), findedBag.getId()),
                                () -> Assertions.assertEquals(newBag.getOwner(), findedBag.getOwner()),
                                () -> Assertions.assertEquals(newBag.getItems(), findedBag.getItems())
                        )
                )
                .verifyComplete();
    }

    //    TODO: create more tests...
}
