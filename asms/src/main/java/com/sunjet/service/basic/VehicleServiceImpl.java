package com.sunjet.service.basic;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.repository.basic.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
@Transactional
@Service("vehicleService")
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private CacheManager customCacheManager;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return vehicleRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return vehicleRepository;
    }

    /**
     * @param keyword
     * @return
     */
    @Override
    public List<VehicleEntity> findAllByKeyword(String keyword) {
        return vehicleRepository.findAllByKeyword("%" + keyword + "%");
    }

    //非首保车辆不在查询
    @Override
    public List<VehicleEntity> findAllByKeywordAndFmDateIsNull(String keyWord) {
        return vehicleRepository.findAllByKeywordAndFmDateIsNull("%" + keyWord + "%");
    }

    @Override
    public VehicleEntity findOneByVin(String vin) {
        return vehicleRepository.findOneByVin(vin);
//        if (CacheManager.getVehicleList().size() <= 0) {
//            customCacheManager.initVehicleList();
//        }
//        Stream<VehicleEntity> partStream = CacheManager.getVehicleList().parallelStream().filter(new Predicate<VehicleEntity>() {
//            @Override
//            public boolean test(VehicleEntity vehicle) {
//                if (vehicle.getVin() == null || vehicle.getVin().equals(vin))
//                    return true;
//                return false;
//            }
//        });
//        return partStream.collect(Collectors.toList()).get(0);

    }
}

