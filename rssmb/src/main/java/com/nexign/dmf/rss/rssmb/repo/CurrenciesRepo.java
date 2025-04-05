package com.nexign.dmf.rss.rssmb.repo;

import com.nexign.dmf.rss.rssmb.model.Currencies;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrenciesRepo extends CrudRepository<Currencies,Long> {
}
