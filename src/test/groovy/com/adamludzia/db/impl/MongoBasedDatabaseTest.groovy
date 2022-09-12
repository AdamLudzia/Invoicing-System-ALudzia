package com.adamludzia.db.impl

import com.adamludzia.db.Database
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "mongo")
class MongoDatabaseTest extends AbstractDatabaseTest {

    @Autowired
    private MongoBasedDatabase mongoDatabase

    @Override
    Database getDatabaseInstance() {
        assert mongoDatabase != null
        mongoDatabase
    }
}
