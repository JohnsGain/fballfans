package com.john.couchdb.core;

import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-20 00:33
 * @since jdk1.8
 */
@ToString
public abstract class AbstractCouchDb implements ICouchDbPrimaryKey, Serializable {

    private static final long serialVersionUID = 9061437526884319267L;
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String setId(String id) {
        return this.id = id;
    }
}
