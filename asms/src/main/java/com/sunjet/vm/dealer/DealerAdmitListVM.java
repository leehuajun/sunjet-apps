package com.sunjet.vm.dealer;

import com.sunjet.cache.CacheManager;
import com.sunjet.service.basic.RegionService;
import com.sunjet.service.dealer.DealerAdmitService;
import com.sunjet.vm.base.DealerListBaseVM;
import com.sunjet.vm.base.DocStatus;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DealerAdmitListVM extends DealerListBaseVM {

    @WireVariable
    private DealerAdmitService dealerAdmitService;
    @WireVariable
    private RegionService regionService;


    @Init(superclass = true)
    public void init() {
        this.setBaseService(dealerAdmitService);
        this.setFormUrl("/views/dealer/dealer_admit_form.zul");

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

    public String getCurrentUserOrGroup(String processInstanceId) {
//        processService
        return null;
    }
}
