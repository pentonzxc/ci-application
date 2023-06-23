package com.example.application;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseUtils;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class ReactiveMongoConfiguration extends AbstractReactiveMongoConfiguration {

    String database;

    String uri;

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connection = new ConnectionString(uri);
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(connection)
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
