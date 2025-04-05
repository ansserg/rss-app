package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.DefData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefDataRepo extends CrudRepository<DefData,Long> {

    @Procedure(name="defRu")
    void defRu(@Param("p_task") Integer task);
    @Modifying
    @Transactional
    @Query(value = "truncate table wrk_prefix", nativeQuery = true)
    void truncateTable();

    @Query(value = "select * from wrk_prefix where status is not null",nativeQuery = true)
    List<DefData> findStatusIsNotNull();
}
