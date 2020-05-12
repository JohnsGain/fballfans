package com.john.couchdb.test;

import com.google.common.collect.Lists;
import com.john.couchdb.core.CouchDbTemplate;
import com.john.couchdb.core.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Random;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-19
 * @since jdk1.8
 */
public class CouchDbClient {

    CouchDbTemplate<User> couchDbClient = new CouchDbTemplate<>();

    @Test
    public void get() throws Exception {
        User addDoc = couchDbClient.get("23a9ac685c3a170cf9fd5d6b4a0135fa", "firstdb", User.class);
        System.out.println(addDoc);
    }

    @Test
    public void add() throws Exception {

        User user = new User();
        user.setLike(Lists.newArrayList("sdf", "sdfq23"));
        String random = RandomStringUtils.random(5, "asdghjkl12345678");
        user.setName(random);
        user.setAge(new Random().nextInt(100));
        User addDoc = couchDbClient.add(user, "firstdb");
        System.out.println(addDoc);
    }

    @Test
    public void update() throws Exception {

        User user = new User();
        user.setLike(Lists.newArrayList("sdf", "sdfq23"));
        String random = RandomStringUtils.random(5, "asdghjkl12345678");
        user.setName(random);
        user.setAge(new Random().nextInt(100));
        User addDoc = couchDbClient.update(user, "firstdb");
        System.out.println(addDoc);
    }

    @Test
    public void test() throws Exception {
        boolean firstdb = couchDbClient.delete("23a9ac685c3a170cf9fd5d6b4a00a1d2", "1-2fc1d70532433c39c9f61480607e3681", "firstdb");
        System.out.println(firstdb);
    }

}
