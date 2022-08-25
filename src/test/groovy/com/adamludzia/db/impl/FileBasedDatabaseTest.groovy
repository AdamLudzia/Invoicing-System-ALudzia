package com.adamludzia.db.impl

import com.adamludzia.db.Database
import com.adamludzia.service.FileService
import com.adamludzia.service.IdService
import com.adamludzia.service.JsonService 

class FileBasedDatabaseTest extends AbstractDatabaseTest {
    @Override
    Database getDatabaseInstance() {

        String idFilePath = "./ids.txt"
        String dbPath = "./dbFile.txt"
        def idService = new IdService(new FileService(idFilePath))
        return new FileBasedDatabase(idService, new FileService(dbPath), new FileService(idFilePath), new JsonService())
    }

    def "file based database writes invoices to correct file"() {
        given:
        String dbPath = "./dbFile.txt"
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(4))

        then:
        1 == Files.readAllLines(Path.of(dbPath)).size()

        when:
        db.save(TestHelpers.invoice(5))

        then:
        2 == Files.readAllLines(Path.of(dbPath)).size()
    }
}
