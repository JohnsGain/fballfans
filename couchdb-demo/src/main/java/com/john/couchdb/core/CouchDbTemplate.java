package com.john.couchdb.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Base64Utils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 封装访问CouchDb的一些简单方法
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-19
 * @since jdk1.8
 */
@Slf4j
public class CouchDbTemplate<V extends ICouchDbPrimaryKey> {

    private String host = "localhost";
    private int port = 5984;
    private String COUCHDB_SERVER = "http://" + host + ":" + port + SEPARATOR;

    public static final String SEPARATOR = "/";

    public String username = "admin";
    public String password = "123456";

    String auth = Base64Utils.encodeToString((username + ":" + password).getBytes(Charset.forName("utf-8")));


    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 添加文档
     *
     * @param v            文档对象
     * @param databaseName 数据库名称
     * @return 返回的文档包含唯一主键
     * @throws Exception
     */
    public V add(V v, String databaseName) throws Exception {
        Map<String, String> headers = new HashMap<>(16);
        String url = COUCHDB_SERVER + databaseName;
        HttpClientResult result = HttpClientUtils.doPost(url, headers, objectMapper.writeValueAsString(v), auth);
        ObjectNode tree = (ObjectNode) objectMapper.readTree(result.getContent());
        String id = tree.get("id").asText();
        v.setId(id);
        return v;
    }

    /**
     * 添加文档
     *
     * @param v            文档对象
     * @param databaseName 数据库名称
     * @return 返回的文档包含唯一主键
     * @throws Exception
     */
    public V update(V v, String databaseName) throws Exception {
        Objects.requireNonNull(v.getId(), "id不能为空");
        String url = COUCHDB_SERVER + databaseName + SEPARATOR + v.getId();
        HttpClientResult result = HttpClientUtils.doPost(url, null, objectMapper.writeValueAsString(v), auth);
        log.info("更新={}", result);
        return v;
    }

    /**
     * 通过文档主键ID获取文档
     *
     * @param id           文档主键ID
     * @param databaseName 数据库名称
     * @return 返回的文档包含唯一主键
     * @throws Exception
     */
    public V get(String id, String databaseName, Class<V> vClass) throws Exception {
        Objects.requireNonNull(id, "id不能为空");
        String url = COUCHDB_SERVER + databaseName + SEPARATOR + id;
        HttpClientResult result = HttpClientUtils.doGet(url, null, null, auth);
        return objectMapper.readValue(result.getContent(), vClass);
    }

    /**
     * 通过文档主键ID和 rev值删除文档
     *
     * @param id           文档主键ID
     * @param databaseName 数据库名称
     * @return true:删除成功
     * @throws Exception
     */
    public boolean delete(String id, String rev, String databaseName) throws Exception {
        Objects.requireNonNull(id, "id不能为空");
        Objects.requireNonNull(rev, "_rev不能为空");
        String url = COUCHDB_SERVER + databaseName + SEPARATOR + id + "?rev=" + rev;
        HttpClientResult result = HttpClientUtils.doDelete(url, null, null, auth);
        ObjectNode tree = (ObjectNode) objectMapper.readTree(result.getContent());
        Boolean delete = tree.get("ok").asBoolean();
        return BooleanUtils.isTrue(delete);
    }


}
