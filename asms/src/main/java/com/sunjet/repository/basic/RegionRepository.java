package com.sunjet.repository.basic;

import com.sunjet.model.basic.RegionEntity;
import com.sunjet.model.basic.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 区域（Region） dao
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public interface RegionRepository extends JpaRepository<RegionEntity, String>, JpaSpecificationExecutor<RegionEntity> {

}
