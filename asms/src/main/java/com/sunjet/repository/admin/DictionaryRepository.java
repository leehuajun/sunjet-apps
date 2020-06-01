package com.sunjet.repository.admin;

import com.sunjet.model.admin.DictionaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据字典 Dao
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface DictionaryRepository extends JpaRepository<DictionaryEntity, String>, JpaSpecificationExecutor<DictionaryEntity> {
//    @Query("select dm from DictionaryEntity dm order by dm.value asc")
//    public List<DictionaryEntity> findAllSort();

    @Query("select dm from DictionaryEntity dm where dm.parent=(select dd from DictionaryEntity dd where dd.code=?1) order by dm.seq asc")
    public List<DictionaryEntity> findByType(String typeCode);

    @Query("select de from DictionaryEntity de where de.parent=null order by de.code desc")
    List<DictionaryEntity> findAllType();
}
