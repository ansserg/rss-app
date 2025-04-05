package com.nexign.dmf.rss.rssmb.repo;

import com.nexign.dmf.rss.rssmb.model.Regions;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegionsRepo extends CrudRepository<Regions,Long> {
    List<Regions> findByDelDateIsNull();
}
