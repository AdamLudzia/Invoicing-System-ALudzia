package com.adamludzia.db.impl;

import com.adamludzia.db.Database;
import com.adamludzia.model.IdInterface;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;

@AllArgsConstructor
public class JpaDatabase<T extends IdInterface> implements Database<T> {

    private final CrudRepository<T, Long> repository;

    @Override
    public long save(T type) {
        return repository.save(type).getId();
    }

    @Override
    public Optional<T> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<T> getAll() {
        return StreamSupport
            .stream(repository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<T> update(long id, T updatedType) {
        Optional<T> typeOptional = getById(id);

        if (typeOptional.isPresent()) {
            repository.save(updatedType);
        }

        return typeOptional;
    }

    @Override
    public Optional<T> delete(long id) {
        Optional<T> type = getById(id);

        type.ifPresent(repository::delete);

        return type;
    }
}
