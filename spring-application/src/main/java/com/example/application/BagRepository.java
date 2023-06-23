package com.example.application;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BagRepository extends ReactiveCrudRepository<Bag, String> {
}
