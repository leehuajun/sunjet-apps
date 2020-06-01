package com.sunjet.service.basic;

import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.repository.basic.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lhj on 16/11/3.
 */
@Service("provinceService")
public class ProvinceServiceImpl implements ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return provinceRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return provinceRepository;
    }

    @Override
    public List<ProvinceEntity> findAllSortCold() {
        return provinceRepository.findAll(new Sort(Sort.Direction.DESC, Arrays.asList("cold")));
    }

    @Override
    public ProvinceEntity saveProvince(ProvinceEntity normalProvince) {
        return provinceRepository.save(normalProvince);
    }
}
