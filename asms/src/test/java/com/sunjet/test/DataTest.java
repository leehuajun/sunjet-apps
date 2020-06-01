package com.sunjet.test;

import com.sunjet.model.agency.AgencyAdmitRequestEntity;
import com.sunjet.model.basic.*;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.repository.basic.RegionRepository;
import com.sunjet.repository.basic.VehicleRepository;
import com.sunjet.service.agency.AgencyAdmitService;
import com.sunjet.service.admin.DictionaryService;
import com.sunjet.service.basic.DocumentNoService;
import com.sunjet.service.dealer.DealerAdmitService;
import com.sunjet.utils.common.StringHelper;
import com.sunjet.vm.base.DocStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by lhj on 16/6/30.
 */

public class DataTest {
    private ApplicationContext context = null;

    @Before
    public void init() {
        context = new ClassPathXmlApplicationContext("classpath:spring/spring-config.xml");
    }

    @After
    public void destroy() {
        context = null;
    }

    @Test
    public void testDocNo() {
        DocumentNoService documentNoService = context.getBean(DocumentNoService.class);
        String docNo = documentNoService.getDocumentNo("AgencyAdmitRequestEntity");
        System.out.println("单据号:" + docNo);
    }


    @Test
    public void addVehicle() {
        VehicleRepository vehicleRepository = context.getBean(VehicleRepository.class);
        for (int i = 10; i < 50; i++) {
            VehicleEntity vehicleEntity = new VehicleEntity("vin" + i, "vsn" + i);
            vehicleRepository.save(vehicleEntity);
        }
    }

    @Test
    public void addProvinceByRegionRepository() {
        RegionRepository regionRepository = context.getBean(RegionRepository.class);
        RegionEntity regionEntity = regionRepository.save(new ProvinceEntity("test", "test"));
        System.out.println(regionEntity instanceof ProvinceEntity ? "ProvinceEntity" : "RegionEntity");
    }

    @Test
    public void addAgencyAdmit() {

        DictionaryService dictionaryService = context.getBean(DictionaryService.class);
        AgencyAdmitService agencyAdmitService = context.getBean(AgencyAdmitService.class);
//        DictionaryEntity statusObj = dictionaryService.findOne("402883a8572e5d3801572e5d45d60001");
        for (int i = 5; i <= 100; i++) {
            AgencyAdmitRequestEntity entity = new AgencyAdmitRequestEntity();
            entity.setStatus(DocStatus.DRAFT.getIndex());    // 流程状态：草稿

            AgencyEntity agency = new AgencyEntity();
            agency.setName("合作商" + StringHelper.genFixedString(i, 4));
//            agency.setApproved(false);      // 未通过
            entity.setAgency(agency);

//        entity.setCreaterId(CommonHelper.getActiveUser().getUserId());
//        entity.setCreaterName(CommonHelper.getActiveUser().getUsername());

            entity.setCreaterId("4028839455a1c0940155a1c0a0b60006");
            entity.setCreaterName("model");
            agencyAdmitService.save(entity);
        }

    }

    @Test
    public void addDealerAdmit() {
        DictionaryService dictionaryService = context.getBean(DictionaryService.class);

        DealerAdmitService dealerAdmitService = context.getBean(DealerAdmitService.class);


//        DictionaryEntity statusObj = dictionaryService.findOne("402883a8572e5d3801572e5d45d60001");

        for (int i = 5; i <= 100; i++) {
            DealerAdmitRequestEntity entity = new DealerAdmitRequestEntity();
            entity.setStatus(DocStatus.DRAFT.getIndex());    // 流程状态：草稿
//        entity.setStatus("1000010");    // 流程状态：草稿

            DealerEntity dealer = new DealerEntity();
            dealer.setName("服务站" + StringHelper.genFixedString(i, 4));
//            dealer.setApproved(false);      // 未通过

            entity.setDealer(dealer);

            entity.setCreaterId("4028839455a1c0940155a1c0a0b60006");
            entity.setCreaterName("model");
            dealerAdmitService.save(entity);
        }


    }
}
