package com.nexign.dmf.rss.rssmb.repo;

import com.nexign.dmf.rss.rssmb.model.Countries;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountriesRepo extends CrudRepository<Countries,Long> {
    public List<Countries> findByCouCodeIsNotNull();
}
