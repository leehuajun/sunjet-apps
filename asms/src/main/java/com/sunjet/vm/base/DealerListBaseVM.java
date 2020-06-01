package com.sunjet.vm.base;

import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.ZkUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by lhj on 16/11/21.
 */
public class DealerListBaseVM extends FlowListBaseVM {

    private List<ProvinceEntity> provinceEntities;
    private String star = "";
    private String qualification = "";
    private List<DictionaryEntity> stars = new ArrayList<>();
    private List<DictionaryEntity> qualifications = new ArrayList<>();

    public List<DictionaryEntity> getStars() {
        return stars;
    }

    public List<DictionaryEntity> getQualifications() {
        return qualifications;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }

    public void setProvinceEntities(List<ProvinceEntity> provinceEntities) {
        this.provinceEntities = provinceEntities;
    }

    public void setStars(List<DictionaryEntity> stars) {
        this.stars = stars;
    }

    public void setQualifications(List<DictionaryEntity> qualifications) {
        this.qualifications = qualifications;
    }

    @Init(superclass = true)
    public void dealerListInit() {
        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
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

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     * @param searchDTO
     */
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
        Specification<DealerAdmitRequestEntity> specification = (root, query, cb) -> {
            Predicate p04 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p05 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            //Predicate p09 = CustomRestrictions.eq("createrName", CommonHelper.getActiveUser().getUsername(), true).toPredicate(root, query, cb);
            Predicate p09 = CustomRestrictions.ne("status", DocStatus.OBSOLETE.getIndex(), true).toPredicate(root, query, cb);
            logger.info("开始日期:" + DateHelper.getStartDate(this.getStartDate()));
            logger.info("结束日期:" + DateHelper.getEndDate(this.getEndDate()));

            Predicate p = cb.and(p04, p05, p09);


            // 状态
            if (this.getSelectedStatus() != DocStatus.ALL) {
                Predicate p03 = CustomRestrictions.eq("status", this.getSelectedStatus().getIndex(), true).toPredicate(root, query, cb);
                p = cb.and(p, p03);
            }
            //申请等级
            if (StringUtils.isNotBlank(this.star)) {
                Predicate p06 = CustomRestrictions.eq("dealer.star", this.star, true).toPredicate(root, query, cb);
                p = cb.and(p, p06);
            }
            // 维修资质
            if (StringUtils.isNotBlank(this.qualification)) {
                Predicate p07 = CustomRestrictions.eq("dealer.qualification", this.qualification, true).toPredicate(root, query, cb);
                p = cb.and(p, p07);
            }
            //省份
            if (this.getSelectedProvince() != null) {
                Predicate p08 = CustomRestrictions.eq("dealer.province", this.getSelectedProvince(), true).toPredicate(root, query, cb);
                p = cb.and(p, p08);
            }

            // 服务站编号
            if (this.getDealer() != null) {
                Predicate p01 = CustomRestrictions.eq("dealer.code", this.getDealer().getCode(), true).toPredicate(root, query, cb);
                Predicate p02 = CustomRestrictions.eq("dealer.parent", this.getDealer(), true).toPredicate(root, query, cb);
                Predicate p03 = cb.or(p01, p02);
                p = cb.and(p, p03);
            }


            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @Override
    @Command
    @NotifyChange("*")
    public void reset() {
        this.getDealers().clear();
        this.setDealer(CommonHelper.getActiveUser().getDealer());
        this.setSelectedProvince(null);
        this.setSelectedStatus(DocStatus.ALL);
        this.setEndDate(new Date());
        this.setStartDate(DateHelper.getFirstOfMonth());
        this.star = "";
        this.qualification = "";
    }
}
