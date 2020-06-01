package com.sunjet.vm.basic;

import com.sunjet.model.admin.ResourceEntity;
import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.NoticeEntity;
import com.sunjet.service.basic.NoticeService;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.ListBaseVM;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class NoticeListVM extends ListBaseVM {
    @WireVariable
    private NoticeService noticeService;

    @Init
    public void init() {
        this.setEnableAdd(hasPermission(NoticeEntity.class.getSimpleName() + ":create"));
        this.setBaseService(noticeService);
        this.setKeyword("");
        this.setFormUrl("/views/basic/notice_form.zul");
//    this.modelName = ResourceEntity.class.getSimpleName();
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
/*        pagingUser.addEventListener("onPaging", new EventListener() {
            public void onEvent(Event event) throws Exception {
                PagingEvent pagingEvt=(PagingEvent) event;//转化成PaingEvent事件



                Page<User> pu=new Page<User>();//这个是我自己写的工具类
                //其实就是将传入参数pageSize，pageNo的封装

                pu.setAutoCount(true);
                pu.setPageNo( pagingEvt.getActivePage());
                pu.setPageSize(pageSize);
                Page<User>  pageUser=    service.pagedUser(pu,user);//后台biz层的数据访问
                userList=  pageUser.getResult();//result返回一个集合对象全部数据
                binder.loadComponent(userLbx);//这个不加阐述了
            }
        });*/
//        logger.info("DIV的长宽值:" + this.mainDiv.getWidth() + " ---  " + this.mainDiv.getAttribute("height"));
        //this.mainGrid.setHeight(this.mainDiv.getHeight());

        //  Clients.clearBusy();
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     */
    protected void configSearchOrder(SearchDTO searchDTO) {
        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
        searchDTO.setSearchOrderList(Arrays.asList(
                new SearchOrder("publishDate", SearchOrder.OrderType.DESC, 1)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     */
    protected void configSpecification(SearchDTO searchDTO) {
        if (!this.getKeyword().equals("")) {
            Specification<ResourceEntity> specification = new Specification<ResourceEntity>() {
                @Override
                public Predicate toPredicate(Root<ResourceEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate p02 = CustomRestrictions.like("title", getKeyword(), true).toPredicate(root, query, cb);
                    return p02;
                }
            };
            searchDTO.setSpecification(specification);
        }
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_NOTICE_LIST)
    @NotifyChange("resultDTO")
    public void refreshNoticeList() {
        this.refreshList();
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_NOTICE_ENTITY)
    @NotifyChange("*")
    public void refreshNoticeEntity(@BindingParam("entity") DocEntity entity) {
        this.refreshEntity(entity);
    }

    /**
     * 表单提交,保存用户信息
     */
    @GlobalCommand(GlobalCommandValues.SAVE_NOTICE)
    @NotifyChange({"noticeEntity", "resultDTO"})
    public void saveNoticeEntity(@BindingParam("model") NoticeEntity noticeEntity, @BindingParam("action") String action) {
//        System.out.println("保存资源信息");
//        resourceEntity.setOptDesc(resourceEntity.getOperationEntityList().toString().replace("[", "").replace("]", ""));
//
//
////    List<PermissionEntity> permissionEntities = permissionService.findAllByResourceCode(((ResourceEntity) resourceEntity).getCode());
////    if (permissionEntities.size() > 0) {
////      for (PermissionEntity permissionEntity : permissionEntities) {
//////        systemService.deleteEntity(permissionEntity);
////        permissionService.delete(permissionEntity);
////      }
////    }
//        permissionService.deleteAllByResourceCode(resourceEntity.getCode());
//
//        for (OperationEntity operationEntity : resourceEntity.getOperationEntityList()) {
//            permissionService.save(new PermissionEntity(operationEntity.getOptName(), resourceEntity.getName(), resourceEntity.getCode() + ":" + operationEntity.getOptCode(), operationEntity.getSeq()));
//
//        }
//        resourceService.save(resourceEntity, action);
//        this.getFormDialog().detach();
//        initList();
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    @Command
    @NotifyChange("resultDTO")
    public void deleteNotice(@BindingParam("entity") NoticeEntity entity) {
        ZkUtils.showQuestion("是否确定删除?", "询问", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
//        ZkUtils.showInformation(event.getName(),"test");

                if (event.getName().equals("onOK")) {
                    noticeService.delete(entity);
//                    Events.postEvent("onClick", btnRefreshResource, null);
                }
            }
        });
    }

    @Command
    public void print() {
        Clients.print();
    }

}
