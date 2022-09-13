package com.adamludzia.db;

import com.adamludzia.model.IdInterface;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface Database<T extends IdInterface>  {

    long save(T type);

    Optional<T> getById(long id) throws SQLException;

    List<T> getAll() throws SQLException;

    Optional<T> update(long id, T updatedType) throws SQLException;

    Optional<T> delete(long id) throws SQLException;
    
    default void reset() throws SQLException {
        getAll().forEach(invoice -> {
            try {
                delete(invoice.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
