package com.example.rabbitmqdemo;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqdemoApplication.class, args);
    }


    @Test
    public void test() {
        String s = Integer.toBinaryString(100);
        System.out.println(s);
        System.out.println(Integer.toBinaryString(48));
    }

}
