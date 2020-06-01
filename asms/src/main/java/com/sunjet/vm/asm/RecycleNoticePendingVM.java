package com.sunjet.vm.asm;

import com.sunjet.model.asm.RecycleNoticeEntity;
import com.sunjet.model.asm.RecycleNoticeItemEntity;
import com.sunjet.model.base.DocEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.RecycleNoticeItemService;
import com.sunjet.service.asm.RecycleNoticeService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 待返回故障件列表 VM
 */
public class RecycleNoticePendingVM extends FlowListBaseVM {
    @WireVariable
    private UserService userService;
    @WireVariable
    private RecycleNoticeService recycleNoticeService;
    @WireVariable
    private RecycleNoticeItemService recycleNoticeItemService;
    @WireVariable
    private DealerService dealerService;

    private boolean choice;

    private Window window;
    private RecycleNoticeItemEntity recycleNoticePart = new RecycleNoticeItemEntity();
    private List<RecycleNoticeItemEntity> recycleNoticeItems = new ArrayList<RecycleNoticeItemEntity>();
    private Map<String, RecycleNoticeItemEntity> recycleNoticeItemEntityHashMap = new HashMap<>();

    @Init(superclass = true)
    public void init() {
        this.setBaseService(recycleNoticeItemService);
        this.setFormUrl("/views/asm/recycle_notice_form.zul");
        this.recycleNoticePart.setRecycleNotice(new RecycleNoticeEntity());
        this.setEnableAdd(false);
        if (this.hasPermission("RecycleEntity:create")) {
            this.setEnableAddRecycleDoc(true);
        }
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void search() {
        if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");
        } else {
            filterList();
        }
    }

    @Override
    protected void configSearchOrder(SearchDTO searchDTO) {
        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
        searchDTO.setSearchOrderList(Arrays.asList(
                new SearchOrder("createdTime", SearchOrder.OrderType.DESC, 1)
                //        new SearchOrder("name", SearchOrder.OrderType.ASC, 2)
        ));
    }

    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<RecycleNoticeItemEntity> specification = (root, query, cb) -> {
            Predicate p04 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p05 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            //状态--只加载审核后的返回通知单物料列表
            Predicate p06 = CustomRestrictions.eq("recycleNotice.status", DocStatus.CLOSED.getIndex(), true).toPredicate(root, query, cb);
            Predicate p = cb.and(p04, p05, p06);

            // 当前待发数量
            if (this.recycleNoticePart.getCurrentAmount() != null) {
                Predicate p_currentAmount = CustomRestrictions.gt("currentAmount", 0, true).toPredicate(root, query, cb);
                p = cb.and(p, p_currentAmount);
            }

            // 来源业务单号
            if (StringUtils.isNotBlank(this.recycleNoticePart.getRecycleNotice().getSrcDocNo())) {
                Predicate p01 = CustomRestrictions.like("recycleNotice.srcDocNo", this.recycleNoticePart.getRecycleNotice().getSrcDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p01);
            }
            // 单号
            if (StringUtils.isNotBlank(this.recycleNoticePart.getRecycleNotice().getDocNo())) {
                Predicate p02 = CustomRestrictions.like("recycleNotice.docNo", this.recycleNoticePart.getRecycleNotice().getDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p02);
            }
            //服务站编号
            if (this.getDealer() != null && StringUtils.isNotBlank(this.getDealer().getCode())) {
                Predicate p03 = CustomRestrictions.eq("recycleNotice.dealerCode", this.getDealer().getCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p03);
            }


            //料品编码
            if (StringUtils.isNotBlank(this.recycleNoticePart.getPartCode())) {
                Predicate p07 = CustomRestrictions.like("partCode", this.recycleNoticePart.getPartCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p07);
            }
            //料品名称
            if (StringUtils.isNotBlank(this.recycleNoticePart.getPartName())) {
                Predicate p08 = CustomRestrictions.like("partName", this.recycleNoticePart.getPartName().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p08);
            }

            return p;
        };
        searchDTO.setSpecification(specification);
    }


    @Command
    @NotifyChange("*")
    public void reset() {
        this.setSelectedStatus(DocStatus.ALL);
        this.setEndDate(new Date());
        this.setStartDate(DateHelper.getFirstOfMonth());
        recycleNoticePart.getRecycleNotice().setDocNo("");
        recycleNoticePart.getRecycleNotice().setSrcDocNo("");
        recycleNoticePart.setPartName("");
        recycleNoticePart.setPartCode("");
    }

    //选择待返清单
    @Command
    public void selectRecycleNoticePending(@BindingParam("model") RecycleNoticeItemEntity recycleNoticeItem, @BindingParam("check") boolean check) {
        boolean result = true;
        if (check == result && recycleNoticeItem != null) {
            //this.recycleNoticeItems.add(recycleNoticeItem);
            recycleNoticeItemEntityHashMap.put(recycleNoticeItem.getObjId(), recycleNoticeItem);
        } else {
            //this.recycleNoticeItems.remove(recycleNoticeItem);
            recycleNoticeItemEntityHashMap.remove(recycleNoticeItem.getObjId());
        }
    }

    /**
     * 全选
     *
     * @param chk
     */
    @Command
    @NotifyChange({"resultDTO", "choice"})
    public void checkAll(@BindingParam("chk") Boolean chk) {
        List<DocEntity> pageContent = this.getResultDTO().getPageContent();
        if (chk == true) {
            for (DocEntity docEntity : pageContent) {
                RecycleNoticeItemEntity recycleNoticeItem = (RecycleNoticeItemEntity) docEntity;
                if (this.recycleNoticeItemEntityHashMap.get(recycleNoticeItem.getObjId()) == null) {
                    //this.recycleNoticeItems.add(recycleNoticeItem);
                    this.recycleNoticeItemEntityHashMap.put(recycleNoticeItem.getObjId(), recycleNoticeItem);
                }

            }
        } else {
            if (pageContent != null) {
                for (DocEntity docEntity : pageContent) {
                    RecycleNoticeItemEntity recycleNoticeItem = (RecycleNoticeItemEntity) docEntity;
                    if (this.recycleNoticeItemEntityHashMap.get(recycleNoticeItem.getObjId()) != null) {
                        //this.recycleNoticeItems.remove(recycleNoticeItem);
                        this.recycleNoticeItemEntityHashMap.remove(recycleNoticeItem.getObjId());
                    }

                }
            }

        }
        this.choice = chk;
    }

    //判断是否存在
    public Boolean chkIsExist(RecycleNoticeItemEntity recycleNoticeItemEntity) {

        if (this.recycleNoticeItemEntityHashMap.get(recycleNoticeItemEntity.getObjId()) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Command
    @NotifyChange("resultDTO")
    public void createRecycleDoc() {
        if (recycleNoticeItemEntityHashMap.isEmpty()) {
            ZkUtils.showInformation("没有选择物料！", "提示");
        } else {

            Map<String, Object> map = new HashMap<>();
            //map.put("recycleNoticeItem", recycleNoticeItems);
            for (String key : recycleNoticeItemEntityHashMap.keySet()) {
                //map.keySet()返回的是所有key的值
                RecycleNoticeItemEntity recycleNoticeItemEntity = recycleNoticeItemEntityHashMap.get(key);
                this.recycleNoticeItems.add(recycleNoticeItemEntity);
            }
            map.put("recycleNoticeItem", this.recycleNoticeItems);
            window = (Window) ZkUtils.createComponents("/views/asm/recycle_form.zul", null, map);
            window.doModal();
        }
        if (this.recycleNoticeItems != null) {
            this.recycleNoticeItems.clear();

        }
        recycleNoticeItemEntityHashMap.clear();


    }

    @Override
    @Command
    @NotifyChange({"resultDTO", "choice"})
    public void gotoPageNo(@BindingParam("e") Event event) {
        super.gotoPageNo(event);
        List<DocEntity> pageContent = this.getResultDTO().getPageContent();
        for (DocEntity docEntity : pageContent) {
            RecycleNoticeItemEntity recycleNoticeItem = (RecycleNoticeItemEntity) docEntity;

            if (recycleNoticeItemEntityHashMap.get(recycleNoticeItem.getObjId()) != null) {
                this.choice = true;
                return;
            } else {
                this.choice = false;
            }
        }


    }

    /**
     * 设置每页数量
     *
     * @return
     */
    @Override
    protected Integer calculatePageSize() {
        return 100;
    }


    @GlobalCommand(GlobalCommandValues.REFRESH_RECYCLE_NOTICE_PENDING)
    @NotifyChange("*")
    public void callback_pending() {
        initList();

    }


    public RecycleNoticeItemEntity getRecycleNoticePart() {
        return recycleNoticePart;
    }

    public void setRecycleNoticePart(RecycleNoticeItemEntity recycleNoticePart) {
        this.recycleNoticePart = recycleNoticePart;
    }

    public List<RecycleNoticeItemEntity> getRecycleNoticeItems() {
        return recycleNoticeItems;
    }

    public void setRecycleNoticeItems(List<RecycleNoticeItemEntity> recycleNoticeItems) {
        this.recycleNoticeItems = recycleNoticeItems;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }


}
