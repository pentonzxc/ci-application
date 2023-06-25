package com.example.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.application.BagData.toEntity;


@RestController
@RequestMapping("/bags")
@Slf4j
@RequiredArgsConstructor
public class BagController {

    final BagService bagService;

    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    Mono<Bag> bag(@PathVariable String id) {
        return bagService.find(id);
    }


    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    Flux<Bag> bags() {
        return bagService.findAll();
    }


    @RequestMapping(
            path = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Mono<String> add(@RequestBody BagData bag) {
        return bagService.create(toEntity(bag));
    }


    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Mono<Bag> update(@PathVariable("id") String id, @RequestBody BagData bag) {
        return Mono.defer(() -> Mono.just(toEntity(bag)))
                .log()
                .doOnNext(b ->
                {
                    b.setId(new ObjectId(id));
                    log.info("Update bag - {}", b);
                })
                .flatMap(bagService::update)
                .doOnNext((b) -> log.info("Updated - {}" , b));
    }
}
