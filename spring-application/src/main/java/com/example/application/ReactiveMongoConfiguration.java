package com.example.application;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.MongoDatabaseUtils;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class ReactiveMongoConfiguration extends AbstractReactiveMongoConfiguration {

    String database;

    String uri;

    @Bean
    public MongoClient mongoClient() {
            CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
            CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
            return MongoClients.create(
                    MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(uri))
                            .codecRegistry(codecRegistry)
                            .build()
            );
    }


    @Override
    protected String getDatabaseName() {
        return database;
    }


    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
