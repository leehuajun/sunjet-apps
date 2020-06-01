package com.sunjet.repository.basic;

import com.sunjet.model.basic.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface VehicleRepository extends JpaRepository<VehicleEntity, String>, JpaSpecificationExecutor<VehicleEntity> {

    @Query("select v from VehicleEntity v  where v.vin like ?1  or v.vsn like ?1")
    List<VehicleEntity> findAllByKeyword(String keyword);

    @Query("select v from VehicleEntity v  where v.vin=?1")
    VehicleEntity findOneByVin(String vin);

    @Query("select v from VehicleEntity v  where (v.vin like ?1  or v.vsn like ?1) and v.fmDate IS NULL ")
    List<VehicleEntity> findAllByKeywordAndFmDateIsNull(String s);
}
