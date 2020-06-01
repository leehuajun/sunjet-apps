package com.sunjet.service.admin;

import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.repository.admin.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lhj
 * @create 2015-12-18 下午2:50
 */
@Service("dictionaryService")
@Transactional
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dictionaryRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return dictionaryRepository;
    }

    @Override
    public List<DictionaryEntity> findDictionaryList() {
        return dictionaryRepository.findAll();
    }

    @Override
    public DictionaryEntity save(DictionaryEntity dictionaryEntity) {
        return dictionaryRepository.save(dictionaryEntity);
    }

    @Override
    public void delete(DictionaryEntity model) {
        dictionaryRepository.delete(model);
    }

    @Override
    public List<DictionaryEntity> findAll() {
        return dictionaryRepository.findAll();
    }

    @Override
    public DictionaryEntity findOne(String objId) {
        return dictionaryRepository.findOne(objId);
    }

    @Override
    public List<DictionaryEntity> findAllEnabled() {
        return dictionaryRepository.findAll();
    }

    @Override
    public List<DictionaryEntity> findTypes() {
        return dictionaryRepository.findAllType();
    }
}
