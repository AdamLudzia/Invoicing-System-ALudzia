package com.adamludzia.db.impl

import com.adamludzia.db.Database
import org.flywaydb.core.Flyway
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

import javax.sql.DataSource

class SqlDatabaseTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance()  {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations( "db/migration")
                .load()

        flyway.clean()
        flyway.migrate()

        new SqlDatabase(jdbcTemplate)
    }
}

