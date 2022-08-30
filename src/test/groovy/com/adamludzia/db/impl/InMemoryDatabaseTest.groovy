package com.adamludzia.db.impl

import com.adamludzia.db.Database 

class InMemoryDatabaseTest extends AbstractDatabaseTest {
    @Override
    Database getDatabaseInstance() {

        return new InMemoryDatabase()
    }
}
