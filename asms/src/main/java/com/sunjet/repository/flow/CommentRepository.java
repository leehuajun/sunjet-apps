package com.sunjet.repository.flow;

import com.sunjet.model.flow.CommentEntity;
import com.sunjet.model.flow.LeaveBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by lhj on 16/9/18.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, String>, JpaSpecificationExecutor<CommentEntity> {
    @Query("select ce from CommentEntity ce where ce.flowInstanceId=?1 order by ce.doDate asc")
    List<CommentEntity> findAllByProcessInstanceId(String processInstanceId);
}
