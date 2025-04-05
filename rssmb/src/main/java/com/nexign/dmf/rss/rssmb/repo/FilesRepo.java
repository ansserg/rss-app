package com.nexign.dmf.rss.rssmb.repo;


import com.nexign.dmf.rss.rssmb.model.FilesData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface FilesRepo extends CrudRepository<FilesData,Long> {
    @Modifying
    @Transactional
    @Query(value = "update files set file_charge=:charge,file_tax=:tax where file_id=:id",nativeQuery = true)
    public int updateFileChargeOld(@Param("id") Long id, @Param("charge") BigDecimal charge, @Param("tax") BigDecimal tax);

    @Modifying
    @Transactional
    @Query(value = "update files set (file_charge,file_tax)=(select sum(out_charge),sum(out_tax) from calls where hpmn_file_id=:id and del_date is null) where file_id=:id",nativeQuery = true)
    public int updateFileCharge(@Param("id") Long id);
}
