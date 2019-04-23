package com.fballfans.elasticsearch.controller;

import com.fballfans.elasticsearch.entity.Country;
import com.fballfans.elasticsearch.service.impl.CountryServiceImpl;
import com.john.Pageable;
import com.john.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangjuwa
 * @date 2019/4/23
 * @since jdk1.8
 **/
@RestController
@RequestMapping("country")
public class CountryController {

    @Autowired
    private CountryServiceImpl countryServiceImpl;

    @GetMapping("/{id}")
    public Result<Country> get(@PathVariable("id") Long id) {
        Country byId = countryServiceImpl.findById(id);
        return new Result<>(byId);
    }

    @PostMapping()
    public Result<Country> add(@RequestParam("name") String name, @RequestParam("id") Long id) {
        Country country = new Country();
        country.setName(name);
        country.setId(id);
        country = countryServiceImpl.save(country);
        return new Result<>(country);
    }

    @GetMapping("/list")
    public Result<List<Country>> list(@RequestParam(value = "name", required = false) String name) {
        List<Country> list = countryServiceImpl.findByName(name);
        return new Result<>(list);
    }

    @GetMapping("page")
    public Result<Page<Country>> page(Pageable pageable,@RequestParam(value = "name", required = false) String name) {
        Page<Country> page = countryServiceImpl.page(pageable, name);
        return new Result<>(page);
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id) {
        countryServiceImpl.deleteById(id);
        return new Result<>("success");
    }

    @PutMapping()
    public Result<Country> update(Country country) {
        country = countryServiceImpl.update(country);
        return new Result<>(country);
    }
}
