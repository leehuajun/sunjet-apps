package com.sunjet.vm.dealer;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.model.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.model.helper.ActiveUser;
import com.sunjet.model.helper.EntityWrapper;
import com.sunjet.service.basic.RegionService;
import com.sunjet.service.dealer.DealerLevelChangeService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DealerListBaseVM;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2016/9/8.
 */
public class DealerLevelChangeListVM extends DealerListBaseVM {
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DealerLevelChangeService dealerLevelChangeService;

    @Init(superclass = true)
    public void init() {
        this.setBaseService(dealerLevelChangeService);
        this.setFormUrl("/views/dealer/dealer_level_change_form.zul");

        this.setProvinceEntities(regionService.findAllProvince());
        this.setStars(CacheManager.getDictionariesByParentCode("10010"));
        this.setQualifications(CacheManager.getDictionariesByParentCode("10020"));
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        Selectors.wireEventListeners(view, this);
        initList();
    }

}
