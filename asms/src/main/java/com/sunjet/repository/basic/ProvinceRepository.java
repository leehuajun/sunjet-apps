package com.sunjet.repository.basic;

import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.model.basic.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 省/直辖市（Region） dao
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public interface ProvinceRepository extends JpaRepository<ProvinceEntity, String>, JpaSpecificationExecutor<ProvinceEntity> {

}
