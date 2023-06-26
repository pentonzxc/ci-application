package com.example.application.controller;


import com.example.application.service.BagService;
import com.example.application.data.BagData;
import com.example.application.model.Bag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.application.data.BagData.toEntity;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BagGraphqlController {


    final BagService bagService;


    @QueryMapping(value = "getBagById")
    public Mono<Bag> bag(@Argument String id) {
        return bagService.find(id);
    }


    @QueryMapping(value = "getAllBags")
    public Flux<Bag> bags() {
        return bagService.findAll();
    }


    @MutationMapping(value = "createBag")
    public Mono<String> create(@Argument(name = "bag") BagData bagData) {
        return bagService.create(toEntity(bagData));
    }


    @MutationMapping(value = "updateBag")
    public Mono<Bag> update(@Argument("id") String id, @Argument("bag") BagData bagData) {
        return Mono.defer(() -> Mono.just(toEntity(bagData))).doOnNext(b ->
                {
                    b.setId(new ObjectId(id));
                    log.info("Update bag - {}", b);
                })
                .flatMap(bagService::update)
                .doOnNext((b) -> log.info("Updated - {}", b));
    }

}
