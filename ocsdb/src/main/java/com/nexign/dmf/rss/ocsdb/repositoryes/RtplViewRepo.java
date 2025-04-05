package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.RtplView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RtplViewRepo extends CrudRepository<RtplView,Long> {
    List<RtplView> findByRtplIdAndEndDateGreaterThanEqual(Long rtplId, Date startDate);
 //   findByAgeGreaterThanEqual
}
