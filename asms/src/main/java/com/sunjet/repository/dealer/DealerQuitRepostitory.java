package com.sunjet.repository.dealer;

import com.sunjet.model.dealer.DealerQuitRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface DealerQuitRepostitory extends JpaRepository<DealerQuitRequestEntity, String>, JpaSpecificationExecutor<DealerQuitRequestEntity> {

}
