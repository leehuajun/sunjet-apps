package com.sunjet.service.basic;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.repository.basic.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */

@Transactional
@Service("partService")
public class PartServiceImpl implements PartService {
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private CacheManager customCacheManager;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return partRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return partRepository;
    }


    /**
     * 根据关键字，查询配件编号或者配件名称包含关键字的列表
     *
     * @param filter
     * @return
     */
    @Override
    public List<PartEntity> findAllByKeyword(String filter) {
        if (filter.length() == 2) {
            return partRepository.findAllByKeyword(filter);
        } else {
            return partRepository.findAllByKeyword("%" + filter + "%");
        }

//        if (CacheManager.getPartList().size() <= 0) {
//            customCacheManager.initPartList();
//        }
//
//        String lowerFilter = filter.toLowerCase();
//        Stream<PartEntity> partStream = CacheManager.getPartList().parallelStream().filter(new Predicate<PartEntity>() {
//            @Override
//            public boolean test(PartEntity part) {
//                if (part.getCode() == null || part.getName() == null || part.getPrice().equals(0.0) )
//                    return false;
//
//                if ((part.getCode().toLowerCase().contains(lowerFilter)
//                        || part.getName().toLowerCase().contains(lowerFilter)) && part.getEnabled().equals(true)) {
//                    return true;
//                }
//
//                return false;
//            }
//        });
//        return partStream.collect(Collectors.toList());
    }

    /**
     * 根据关键字，查询配件编号或者配件名称包含关键字的列表
     *
     * @param code
     * @return
     */
    @Override
    public List<PartEntity> findAllByCode(String code) {
        return partRepository.findAllByCode("%" + code + "%");
//        if (CacheManager.getPartList().size() <= 0) {
//            customCacheManager.initPartList();
//        }
//
//        String lowerFilter = code.toLowerCase();
//        Stream<PartEntity> partStream = CacheManager.getPartList().parallelStream().filter(new Predicate<PartEntity>() {
//            @Override
//            public boolean test(PartEntity part) {
//                if (part.getCode() == null || part.getName() == null)
//                    return false;
//
//                if (part.getCode().toLowerCase().equals(lowerFilter)) {
//                    return true;
//                }
//
//                return false;
//            }
//        });
//        return partStream.collect(Collectors.toList());
    }
}
