package com.adamludzia.model;

import javax.persistence.Id;

public interface IdInterface {

    @Id
    long getId();

    @Id
    void setId(long id);
}
