package com.example.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BagService {

    final BagRepository bagRepository;

    public Mono<Bag> create(Bag bag) {
        return bagRepository.save(bag);
    }


    public Mono<Bag> update(Bag bag) {
        return bagRepository
                .findById(bag.getId())
                .switchIfEmpty(Mono.error(EntityNotFoundException::new));
    }


    public Mono<Void> delete(String id) {
        return bagRepository.deleteById(id);
    }


    public Mono<Bag> find(String id) {
        return bagRepository.findById(id);
    }


    public Flux<Bag> findAll() {
        return bagRepository.findAll();
    }
}
