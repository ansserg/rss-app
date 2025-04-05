package com.nexign.dmf.rss.rssmb.repo;


import com.nexign.dmf.rss.rssmb.model.BillCallsRap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillCallsRapRepo extends CrudRepository<BillCallsRap,Long> {
}
