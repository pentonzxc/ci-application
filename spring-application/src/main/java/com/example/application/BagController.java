package com.example.application;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.application.BagData.toEntity;


@RestController
@RequestMapping("/users")
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
    Mono<Void> add(@RequestBody BagData bag) {
        return bagService.create(toEntity(bag)).then();
    }


    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Mono<Void> update(@PathVariable("id") String id, @RequestBody BagData bag) {
        return Mono.defer(() -> Mono.just(toEntity(bag)))
                .doOnNext(b -> b.setId(id))
                .flatMap(bagService::update)
                .then();
    }
}
