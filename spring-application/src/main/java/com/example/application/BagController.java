package com.example.application;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.application.BagData.toEntity;


@RestController
@RequiredArgsConstructor
public class BagController {

    final BagService bagService;


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Bag> bag(String id) {
        return bagService.find(id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Flux<Bag> bags() {
        return bagService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Mono<Void> add(BagData bag) {
        return bagService.create(toEntity(bag)).then();
    }


    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    Mono<Void> update(BagData bag) {
        return bagService.update(toEntity(bag)).then();
    }
}
