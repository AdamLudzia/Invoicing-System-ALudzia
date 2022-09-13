package com.adamludzia.configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.adamludzia.db.Database;
import com.adamludzia.db.impl.InvoiceRepository;
import com.adamludzia.db.impl.JpaDatabase;
import com.adamludzia.db.impl.MongoBasedDatabase;
import com.adamludzia.db.impl.MongoIdService;
import com.adamludzia.db.impl.SqlDatabase;
import com.adamludzia.model.Invoice;
import com.adamludzia.service.FileService;
import com.adamludzia.service.IdService;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;



@Configuration
@Slf4j
public class DatabaseConfiguration {

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    public IdService idService(
        @Value("${invoicing-system.database.id.file}") String idFile
    ) throws IOException {
        FileService fileService = new FileService(idFile);
        return new IdService(fileService);
    }
    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    public FileService fileService(
        @Value("${invoicing-system.database.invoices.file}") String invoicesFile
    ) throws IOException {
        return new FileService(invoicesFile);
    }
    @Bean
    @@ -50,4 +68,53 @@
        return new InMemoryDatabase();
}

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
    public Database sqlDatabase(JdbcTemplate jdbcTemplate) {
        return new SqlDatabase(jdbcTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
    public Database jpaDatabase(InvoiceRepository invoiceRepository) {
        return new JpaDatabase(invoiceRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
    public MongoDatabase mongoDb(
        @Value("${invoicing-system.database.name}") String databaseName
    ) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .build();

        MongoClient client = MongoClients.create(settings);
        return client.getDatabase(databaseName);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
    public MongoIdService mongoIdService(
        @Value("${invoicing-system.database.counter.collection}") String collectionName,
        MongoDatabase mongoDb
    ) {
        MongoCollection<Document> collection = mongoDb.getCollection(collectionName);
        return new MongoIdService(collection);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
    public Database mongoDatabase(
        @Value("${invoicing-system.database.collection}") String collectionName,
        MongoDatabase mongoDb,
        MongoIdService mongoIdService
    ) {
        MongoCollection<Invoice> collection = mongoDb.getCollection(collectionName, Invoice.class);
        return new MongoBasedDatabase(collection, mongoIdService);
    }

}
