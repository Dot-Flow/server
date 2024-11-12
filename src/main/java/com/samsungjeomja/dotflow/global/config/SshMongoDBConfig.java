package com.samsungjeomja.dotflow.global.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "com.samsungjeomja.dotflow.domain")
public class SshMongoDBConfig {

    private final SshTunnelConfig initializer;

    @Value("${server}")
    private String isServer;

    @Value("${cloud.aws.ec2.database_endpoint}")
    private String databaseEndpoint;

    @Value("${cloud.aws.ec2.database_port}")
    private int databasePort;

    @Value("${spring.data.mongodb.username}")
    private String mongoUser;
    @Value("${spring.data.mongodb.password}")
    private String mongoPassword;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient() {
        String host = databaseEndpoint;
        int port = databasePort;

        if (isServer.equals("false")) {
            Integer forwardedPort = initializer.buildSshConnection(databaseEndpoint, databasePort);
            host = "localhost";
            port = forwardedPort;
        }

        try {
            ConnectionString connectionString = new ConnectionString(String.format(
                    "mongodb://%s:%s@%s:%s/%s?retryWrites=false",
                    mongoUser,
                    mongoPassword,
                    host,
                    port,
                    databaseName
            ));

            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            log.info("mongo connection through SSH: host={}, port={}", host, port);


            return MongoClients.create(mongoClientSettings);
        } catch (Exception e) {
            log.error("Failed to create MongoClient: {}", e.getMessage(), e);
            throw e; // 예외를 던져서 초기화가 실패하도록 함
        }
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        MongoDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(mongoClient, databaseName);
        return new MongoTemplate(mongoDbFactory);
    }

}

