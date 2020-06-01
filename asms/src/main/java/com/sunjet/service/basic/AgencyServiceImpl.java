package com.sunjet.service.basic;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.repository.basic.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("agencyService")
public class AgencyServiceImpl implements AgencyService {
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private CacheManager customCacheManager;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return agencyRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return agencyRepository;
    }

    @Override
    public AgencyEntity saveAgency(AgencyEntity agencyEntity) {
        AgencyEntity agency = agencyRepository.save(agencyEntity);
//        customCacheManager.initAgencyList();
        return agency;
    }

    @Override
    public List<AgencyEntity> findAllByKeyword(String keyword) {
        return agencyRepository.findAllByKeyword("%" + keyword + "%");
//        if (CacheManager.getAgencyList().size() <= 0) {
//            customCacheManager.initAgencyList();
//        }
//
//        String lowerFilter = keyword.toLowerCase();
//        Stream<AgencyEntity> stream = CacheManager.getAgencyList().parallelStream().filter(new Predicate<AgencyEntity>() {
//            @Override
//            public boolean test(AgencyEntity agency) {
//                if(agency.getEnabled()==false)
//                    return false;
//                if (agency.getCode() == null || agency.getName() == null)
//                    return false;
//                if (agency.getCode().toLowerCase().contains(lowerFilter)
//                        || agency.getName().toLowerCase().contains(lowerFilter)) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        return stream.collect(Collectors.toList());
    }

    @Override
    public AgencyEntity findOneByCode(String code) {
        return agencyRepository.findOneByCode(code);
//
    }

    /**
     * 检查code是否存在
     *
     * @param code
     * @return
     */
    @Override
    public Boolean checkCodeExists(String code) {
        AgencyEntity agencyEntity = agencyRepository.findOneByCode(code);
        if (agencyEntity != null)
            return true;
        else
            return false;

//        if (CacheManager.getAgencyList().size() <= 0) {
//            customCacheManager.initAgencyList();
//        }
//        Stream<AgencyEntity> stream = CacheManager.getAgencyList().parallelStream().filter(new Predicate<AgencyEntity>() {
//            @Override
//            public boolean test(AgencyEntity agency) {
//                if (agency.getCode() == null || agency.getName() == null)
//                    return false;
//                if (agency.getCode().equals(code)) {
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        if(stream.collect(Collectors.toList()).size()>0)
//            return true;
//        else
//            return false;
    }
}
