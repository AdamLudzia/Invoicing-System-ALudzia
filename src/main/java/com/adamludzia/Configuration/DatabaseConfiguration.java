package com.adamludzia.Configuration;

import com.adamludzia.db.Database;
import com.adamludzia.db.impl.FileBasedDatabase;
import com.adamludzia.service.FileService;
import com.adamludzia.service.IdService;
import com.adamludzia.service.JsonService;
import java.io.IOException;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

    private static final String ID_FILE_NAME = "ids.txt";
    private static final String INVOICES_FILE_NAME = "dbFile.txt";

    @Bean
    public IdService idService() throws IOException {
        FileService fileService = new FileService(ID_FILE_NAME);
        return new IdService(fileService);
    }

    @Bean
    public FileService fileService() throws IOException {
        return new FileService(INVOICES_FILE_NAME);
    }

    @SneakyThrows
    @Bean
    public Database fileBasedDatabase() {
        IdService idService = new IdService(new FileService(ID_FILE_NAME));
        return new FileBasedDatabase(idService, new FileService(INVOICES_FILE_NAME), new FileService(ID_FILE_NAME), new JsonService());
    }

}
