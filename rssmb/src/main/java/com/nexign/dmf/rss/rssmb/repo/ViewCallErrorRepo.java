package com.nexign.dmf.rss.rssmb.repo;

import com.nexign.dmf.rss.rssmb.model.ViewCallError;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ViewCallErrorRepo extends CrudRepository<ViewCallError,Long> {
}
