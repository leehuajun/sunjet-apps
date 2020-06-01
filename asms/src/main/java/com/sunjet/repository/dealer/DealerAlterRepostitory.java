package com.sunjet.repository.dealer;

import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface DealerAlterRepostitory extends JpaRepository<DealerAlterRequestEntity, String>, JpaSpecificationExecutor<DealerAlterRequestEntity> {

    @Query("select ae from DealerAlterRequestEntity ae where code like ?1 or name like ?1")
    List<DealerEntity> findAllByKeyword(String keyword);
}
