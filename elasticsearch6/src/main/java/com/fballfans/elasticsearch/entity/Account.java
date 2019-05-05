package com.fballfans.elasticsearch.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * {@link PropertyNamingStrategy.PascalCaseStrategy}
 * 更改ES字段与实体属性的映射关系,user_name映射到实体里面的userName
 *
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
@Document(indexName = "bank", type = "_doc")
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Account implements Serializable {

    private static final long serialVersionUID = 5743473998244677494L;

    @Id
    private Long id;

    /**
     * 借助@JsonProperty更改ES字段与实体属性的映射关系
     */
//    @JsonProperty(value = "account_number")
//    private Long accountNumber;
    private Long account_number;

    @GeoPointField
    private GeoPoint geoPoint;

    private BigDecimal balance;

    private String firstname;
    private String lastname;
    private Integer age;
    private Character gender;
    private String address;
    private String employer;
    private String email;
    private String city;
    private String state;

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Long getAccount_number() {
        return account_number;
    }

    public void setAccount_number(Long account_number) {
        this.account_number = account_number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getAccountNumber() {
//        return accountNumber;
//    }
//
//    public void setAccountNumber(Long accountNumber) {
//        this.accountNumber = accountNumber;
//    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
//                ", accountNumber=" + accountNumber +
                ", balance=" + balance +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", employer='" + employer + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
