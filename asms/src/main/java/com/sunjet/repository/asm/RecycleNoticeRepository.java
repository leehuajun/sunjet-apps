package com.sunjet.repository.asm;

import com.sunjet.model.asm.RecycleNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 故障件返回通知单
 * Created by lhj on 16/9/17.
 */
public interface RecycleNoticeRepository extends JpaRepository<RecycleNoticeEntity, String>, JpaSpecificationExecutor<RecycleNoticeEntity> {
    @Query("select rn from RecycleNoticeEntity rn left join fetch rn.items where rn.objId=?1")
    RecycleNoticeEntity findOneWithOthersId(String objId);

}
