package com.sunjet.vm.admin;

import com.sunjet.model.admin.ScheduleJobEntity;
import com.sunjet.service.admin.ScheduleJobService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.vm.base.ListBaseVM;
import org.quartz.SchedulerException;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ScheduleJobListVM extends ListBaseVM {
    @WireVariable
    private ScheduleJobService scheduleJobService;


    @Init
    public void init() {
        this.setBaseService(scheduleJobService);
        this.setKeyword("");
        this.setFormUrl("/views/admin/schedulejob_form.zul");
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     */
//  protected void configSearchOrder(SearchDTO searchDTO) {
//    // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
//    searchDTO.setSearchOrderList(Arrays.asList(
//        new SearchOrder("logId", SearchOrder.OrderType.ASC, 1),
//        new SearchOrder("name", SearchOrder.OrderType.ASC, 2)
//    ));
//  }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     */
    protected void configSpecification(SearchDTO searchDTO) {
        if (!this.getKeyword().equals("")) {
            Specification<ScheduleJobEntity> specification = new Specification<ScheduleJobEntity>() {
                @Override
                public Predicate toPredicate(Root<ScheduleJobEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate p01 = CustomRestrictions.like("jobName", getKeyword(), true).toPredicate(root, query, cb);
                    Predicate p02 = CustomRestrictions.like("jobGroup", getKeyword(), true).toPredicate(root, query, cb);
                    Predicate p = cb.or(p01, p02);
                    return p;
                }
            };
            searchDTO.setSpecification(specification);
        }
    }

    @Command
    public void runAll() throws SchedulerException, ClassNotFoundException {
        scheduleJobService.runAll();
    }

    @Command
    public void run(@BindingParam("job") ScheduleJobEntity job) throws ClassNotFoundException, SchedulerException {
        scheduleJobService.run(job);
    }

    @Command
    public void pauseAll() throws SchedulerException {
        scheduleJobService.pauseAll();
    }

    @Command
    public void resumeAll() throws SchedulerException {
        scheduleJobService.resumeAll();
    }

    @Command
    public void pause(@BindingParam("job") ScheduleJobEntity job) {
        scheduleJobService.pause(job);
    }

    @Command
    public void edit(@BindingParam("job") ScheduleJobEntity job) {
        scheduleJobService.edit(job);
    }

    @Command
    public void delete(@BindingParam("job") ScheduleJobEntity job) {
        scheduleJobService.delete(job);
    }

    @Command
    public void runSingle(@BindingParam("job") ScheduleJobEntity job) {
        scheduleJobService.runSingle(job);
    }
}
