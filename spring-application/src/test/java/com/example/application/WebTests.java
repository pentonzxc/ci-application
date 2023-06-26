package com.example.application;


import com.example.application.config.MongoCollectionsConfig;
import com.example.application.config.ReactiveMongoConfiguration;
import com.example.application.model.Bag;
import com.example.application.service.BagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.types.ObjectId;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.*;

@WebFluxTest(properties = "server.port=8081")
//@SpringBootTest
@Import({BagService.class, MongoCollectionsConfig.class, ReactiveMongoConfiguration.class})
@AutoConfigureRestDocs

public class WebTests {


    @Autowired
    BagService bagService;


    @Autowired
    MongoCollection<Bag> mongoCollection;


    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    WebTestClient client;


//    @Autowired
//    ApplicationContext applicationContext;

//    @Rule
//    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");


    @BeforeEach
    void setUp() {
        client = client.mutate()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl("http://localhost:8081/bags").build();
//                .entityExchangeResultConsumer(document("{method-name}/{step}")).build();


//        client = WebTestClient.bindToApplicationContext(applicationContext)
//                .configureClient()
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .baseUrl("http://localhost:8081/bags")
//                .filter(documentationConfiguration(restDocumentation))
//                .build();
    }


    @AfterEach
    void tearUp() {
        Mono.from(mongoCollection.drop()).subscribe();
    }


    @Test
    public void whenGetExistBag_thenReturn200() {
        Bag expectedBag = new Bag();
        String id = bagService.create(expectedBag).block();
        expectedBag.setId(new ObjectId(id));

        client.get()
                .uri("/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("bag",
                                responseFields(
                                        fieldWithPath("id").type(STRING).description("The id of bag document"),
                                        fieldWithPath("owner").type(STRING).optional().description("The bag's owner"),
                                        fieldWithPath("items").type(ARRAY).optional().description("The bag's items")
                                ),
                                pathParameters(
                                        parameterWithName("id").description("The bag document id")
                                )
                        )
                ).json(toJson(expectedBag));
    }


    @Test
    public void whenGetAbsentBag_thenReturn200() {
        client.get()
                .uri("/test")
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    public void whenCreateBag_thenReturn201() {
        String bagJson = "{\"owner\": null}";

        client.post().uri("/add")
                .accept(MediaType.TEXT_PLAIN)
                .bodyValue(bagJson)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(String.class)
                .getResponseBody()
                .doOnNext((id) ->
                        Assertions.assertTrue(ObjectId.isValid(id))
                );
    }


    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

