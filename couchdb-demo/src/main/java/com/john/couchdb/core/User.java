package com.john.couchdb.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-19 23:13
 * @since jdk1.8
 */
@Data()
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements ICouchDbPrimaryKey {

    @JsonProperty("_id")
    private String id;
    private String _rev;


    private String name;

    private Integer age;

    private List<String> like;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String setId(String id) {
        return this.id = id;
    }

}
