package com.sunjet.repository.asm;

import com.sunjet.model.admin.PermissionEntity;
import com.sunjet.model.asm.SupplyNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * 供货/发货 通知单
 * Created by lhj on 16/9/17.
 */
public interface SupplyNoticeRepository extends JpaRepository<SupplyNoticeEntity, String>, JpaSpecificationExecutor<SupplyNoticeEntity> {
    @Query("select p from SupplyNoticeEntity p where p.docNo like ?1")
    public List<SupplyNoticeEntity> getSupplyNoticeList(String docNo);

    @Query("select p from SupplyNoticeEntity p where p.createdTime >?1   order by p.createdTime desc")
    public List<SupplyNoticeEntity> getLastDocNo(Date currentdate);

    @Query("select rp from SupplyNoticeEntity rp left join fetch rp.items where rp.objId=?1")
    public SupplyNoticeEntity getSupplyNotice(String id);
}
