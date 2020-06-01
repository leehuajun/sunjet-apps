package com.sunjet.vm.agency;

import com.sunjet.model.agency.AgencyQuitRequestEntity;
import com.sunjet.service.agency.AgencyQuitService;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.vm.base.AgencyListBaseVM;
import com.sunjet.vm.base.DocStatus;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;


/**
 * Created by Administrator on 2016/9/8.
 */
public class AgencyQuitListVM extends AgencyListBaseVM {

    @WireVariable
    private AgencyQuitService agencyQuitService;

    @Init(superclass = true)
    public void init() {
        this.setBaseService(agencyQuitService);
        this.setFormUrl("/views/agency/agency_quit_form.zul");
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        Selectors.wireEventListeners(view, this);
        initList();
    }

    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<AgencyQuitRequestEntity> specification = (root, query, cb) -> {
            Predicate p04 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p05 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p07 = CustomRestrictions.ne("status", DocStatus.OBSOLETE.getIndex(), true).toPredicate(root, query, cb);
            Predicate p = cb.and(p04, p05, p07);
            // 状态
            if (this.getSelectedStatus() != DocStatus.ALL) {
                Predicate p03 = CustomRestrictions.eq("status", this.getSelectedStatus().getIndex(), true).toPredicate(root, query, cb);
                p = cb.and(p, p03);
            }
            return p;
        };

        searchDTO.setSpecification(specification);
    }
}
