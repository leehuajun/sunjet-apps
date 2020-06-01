package com.sunjet.service.admin;


import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * @author lhj
 * @create 2015-12-18 下午2:47
 */
public interface DictionaryService extends BaseService {
    /**
     * @return
     */
    public List<DictionaryEntity> findDictionaryList();

    /**
     * 保存
     *
     * @param dictionaryEntity
     * @return
     */
    DictionaryEntity save(DictionaryEntity dictionaryEntity);

    void delete(DictionaryEntity model);

    List<DictionaryEntity> findAll();

    DictionaryEntity findOne(String objId);

    List<DictionaryEntity> findAllEnabled();

    List<DictionaryEntity> findTypes();
}
