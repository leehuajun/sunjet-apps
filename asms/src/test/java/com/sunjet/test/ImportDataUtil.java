package com.sunjet.test;

import com.sunjet.model.basic.*;
import com.sunjet.repository.basic.AgencyRepository;
import com.sunjet.repository.basic.PartRepository;
import com.sunjet.repository.basic.VehicleRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lhj on 16/6/30.
 */

public class ImportDataUtil {
    private ApplicationContext context = null;
    private List<ProvinceEntity> provinceEntities;
    private List<CityEntity> cityEntities;
    private List<CountyEntity> countyEntities;

    @Before
    public void init() {
        context = new ClassPathXmlApplicationContext("classpath:spring/spring-config.xml");
//        ProvinceRepository provinceRepository = context.getBean(ProvinceRepository.class);
//        CityRepository cityRepository = context.getBean(CityRepository.class);
//        CountyRepository countyRepository = context.getBean(CountyRepository.class);
//
//        provinceEntities = provinceRepository.findAll();
//        cityEntities = cityRepository.findAll();
//        countyEntities = countyRepository.findAll();
    }

    @After
    public void destroy() {
        context = null;
    }

    /**
     * 更新经销商信息
     */
    @Test
    public void updateAgecies() {
        AgencyRepository agencyRepository = context.getBean(AgencyRepository.class);
        List<AgencyEntity> agencyEntities = agencyRepository.findAll();
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_agency_20170427");
        Integer count = 0;
        for (Map<String, Object> map : mapList) {
            for (AgencyEntity entity : agencyEntities) {
                if (entity.getCode().equals(map.get("合作商编号").toString())) {
//                    entity.setCode(map.get("code").toString());
                    entity.setName(map.get("合作商名称").toString());
                    entity.setAddress(map.get("合作商地址").toString());
                    entity.setPhone(map.get("电话").toString());
                    entity.setFax(map.get("传真").toString());
                    entity.setLegalPerson(map.get("法人代表").toString());
                    entity.setLegalPersonPhone(map.get("法人电话").toString());
                    entity.setShopManager(map.get("店长").toString());
                    entity.setShopManagerPhone(map.get("店长电话").toString());
                    entity.setProductsOfSupply(map.get("拟供产品系列").toString());
                    entity.setTaxpayerCode(map.get("税号").toString());
                    entity.setBank(map.get("开户银行").toString());
                    entity.setBankAccount(map.get("银行账户").toString());
                    entity.setProductsOfSupply(map.get("拟供产品系列").toString());
                    entity.setProvince(getProvice(map.get("所在省").toString()));
                    entity.setCity(getCity(map.get("所在市").toString()));
                    entity.setCounty(getCounty(map.get("所在区").toString()));
                    agencyRepository.save(entity);
                    count++;
                    break;
                }
            }
        }
        System.out.println("共修改了记录数：" + count);
    }

    private ProvinceEntity getProvice(String name) {
        ProvinceEntity entity = null;
        for (ProvinceEntity provinceEntity : this.provinceEntities) {
            if (provinceEntity.getName().contains(name)) {
                entity = provinceEntity;
            }
        }
        return entity;
    }

    private CityEntity getCity(String name) {
        CityEntity entity = null;
        for (CityEntity cityEntity : this.cityEntities) {
            if (cityEntity.getName().contains(name)) {
                entity = cityEntity;
            }
        }
        return entity;
    }

    private CountyEntity getCounty(String name) {
        CountyEntity entity = null;
        for (CountyEntity countyEntity : this.countyEntities) {
            if (countyEntity.getName().contains(name)) {
                entity = countyEntity;
            }
        }
        return entity;
    }

    /**
     * 导入车辆
     */
    @Test
    public void importVehicles() {
        VehicleRepository vehicleRepository = context.getBean(VehicleRepository.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_vehicles_add_20170708 where id>=4000 and id<5000");
        Integer count = 0;
        for (Map<String, Object> map : mapList) {
            VehicleEntity vehicle = new VehicleEntity();
            vehicle.setVin(map.get("VIN").toString());
            vehicle.setVehicleModel(map.get("车型型号").toString());
            vehicle.setEngineModel(map.get("发动机型号").toString());
            vehicle.setEngineNo(map.get("发动机号").toString());
            vehicle.setVsn(map.get("VSN").toString());
//      private Date manufactureDate;               // 生产日期
//      private Date productDate;                   // 出厂日期
//      private Date purchaseDate;                  // 购买日期
            vehicle.setManufactureDate(convertStringToDate(map.get("生产日期").toString()));
            vehicle.setProductDate(convertStringToDate(map.get("出厂日期").toString()));
            vehicle.setSeller(map.get("经销商名称").toString());
            vehicle.setPurchaseDate(convertStringToDate(map.get("车辆购买时间").toString()));
            vehicle.setOwnerName(map.get("车主姓名").toString());
            vehicle.setAddress(map.get("详细地址").toString());
//            vehicle.setPostcode(map.get("邮政编码").toString());
            vehicle.setPhone(map.get("固定电话").toString());
            vehicle.setMobile(map.get("手机").toString());
//            vehicle.setEmail(map.get("电子邮件").toString());
            vehicle.setVmt("0");
            vehicleRepository.save(vehicle);
            count++;
        }
        System.out.println("共导入了记录数：" + count);
    }

    @Test
    public void updateVehicle() {
        VehicleRepository vehicleRepository = context.getBean(VehicleRepository.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_vehicle_update_20170707 where id<10000");
        Integer count = 0;
        for (Map<String, Object> map : mapList) {
            VehicleEntity entity = vehicleRepository.findOneByVin(map.get("VIN").toString());
            if (entity != null) {
                entity.setManufactureDate(convertStringToDate(map.get("生产日期").toString()));
                entity.setProductDate(convertStringToDate(map.get("出厂日期").toString()));
                entity.setPurchaseDate(convertStringToDate(map.get("车辆购买时间").toString()));
                vehicleRepository.save(entity);
                count++;
            }
        }
        System.out.println("共修改了记录数：" + count);
    }

    /**
     * 修改车辆
     */
    @Test
    public void modifyVehicles() {
        VehicleRepository vehicleRepository = context.getBean(VehicleRepository.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_vehicles_update_20170708 where  id>=3000 and id<4000");
        List<VehicleEntity> vehicleEntities = vehicleRepository.findAll();
        Integer count = 0;
//        for (Map<String, Object> map : mapList) {
//            VehicleEntity vehicle = vehicleRepository.findOneByVin(map.get("VIN").toString().trim());
//            if(vehicle==null)
//                continue;
//            vehicle.setVin(map.get("VIN").toString());
//            vehicle.setVehicleModel(map.get("车型型号").toString());
//            vehicle.setEngineModel(map.get("发动机型号").toString());
//            vehicle.setEngineNo(map.get("发动机号").toString());
//            vehicle.setVsn(map.get("VSN").toString());
////      private Date manufactureDate;               // 生产日期
////      private Date productDate;                   // 出厂日期
////      private Date purchaseDate;                  // 购买日期
//            vehicle.setManufactureDate(convertStringToDate(map.get("生产日期").toString()));
//            vehicle.setProductDate(convertStringToDate(map.get("出厂日期").toString()));
//            vehicle.setSeller(map.get("经销商名称").toString());
//            vehicle.setPurchaseDate(convertStringToDate(map.get("车辆购买时间").toString()));
//            vehicle.setOwnerName(map.get("车主姓名").toString());
//            vehicle.setAddress(map.get("详细地址").toString());
////            vehicle.setPostcode(map.get("邮政编码").toString());
//            vehicle.setPhone(map.get("固定电话").toString());
//            vehicle.setMobile(map.get("手机").toString());
////            vehicle.setEmail(map.get("电子邮件").toString());
////                    vehicle.setVmt("0");
//            vehicleRepository.save(vehicle);
//            count++;
//
//        }

        for (VehicleEntity vehicle : vehicleEntities) {
            for (Map<String, Object> map : mapList) {
                if (vehicle.getVin().equals(map.get("VIN").toString().trim())) {
//                    vehicle.setVin(map.get("VIN").toString());
                    vehicle.setVehicleModel(map.get("车型型号").toString());
                    vehicle.setEngineModel(map.get("发动机型号").toString());
                    vehicle.setEngineNo(map.get("发动机号").toString());
                    vehicle.setVsn(map.get("VSN").toString());
//      private Date manufactureDate;               // 生产日期
//      private Date productDate;                   // 出厂日期
//      private Date purchaseDate;                  // 购买日期
                    vehicle.setManufactureDate(convertStringToDate(map.get("生产日期").toString()));
                    vehicle.setProductDate(convertStringToDate(map.get("出厂日期").toString()));
                    vehicle.setSeller(map.get("经销商名称").toString());
                    vehicle.setPurchaseDate(convertStringToDate(map.get("车辆购买时间").toString()));
                    vehicle.setOwnerName(map.get("车主姓名").toString());
                    vehicle.setAddress(map.get("详细地址").toString());
//            vehicle.setPostcode(map.get("邮政编码").toString());
                    vehicle.setPhone(map.get("固定电话").toString());
                    vehicle.setMobile(map.get("手机").toString());
//                    vehicle.setEmail(map.get("电子邮件").toString());
//                    vehicle.setVmt("0");
                    vehicleRepository.save(vehicle);
                    count++;
                    break;
                }
            }
        }
        System.out.println("共修改了记录数：" + count);
    }

    @Test
    public void testStringToDate() {
        String str = "2016-04-25";
//    String str = "7/15/16";
        Date date = convertStringToDate(str);
        System.out.println(date);
    }

    private Date convertStringToDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            if (StringUtils.isNotBlank(strDate)) {
                // 如果是16/25/3 这种日期格式使用下面代码转换
                String[] dds = strDate.split("/");
                String strdd = "20" + dds[2] + "-" + dds[0] + "-" + dds[1];
                date = format.parse(strdd);
                // 如果是 2016-04-06 这种日期格式，则不需要转换
//                date = format.parse(strDate);
            }
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return date;
    }

    /**
     * 修改零件
     */
    //@Test
    //public void updatePartPrice() {
    //    PartRepository partRepository = context.getBean(PartRepository.class);
    //    JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
    //    List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_part_mod02_20170608");
    //
    //    Integer count = 0;
    //    for (Map map : mapList) {
    //        PartEntity partEntity = partRepository.findOneByVin(map.get("零部件号").toString());
    //        if (partEntity == null)
    //            continue;
    //
    //        partEntity.setWarrantyTime(map.get("三包时间").toString() + "月");
    //        partEntity.setWarrantyMileage(map.get("三包里程").toString() + "公里");
    //        partEntity.setPrice(Double.parseDouble(map.get("结算价").toString()));
    //        partRepository.save(partEntity);
    //        count++;
    //    }
    //    System.out.println("共修改了记录数：" + count);
    //}

    /**
     * 导入零部件
     */
    @Test
    public void importPart() {
        PartRepository partRepository = context.getBean(PartRepository.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_part_add02_20170608");
        Integer count = 0;
        for (Map<String, Object> map : mapList) {
            PartEntity part = new PartEntity();
            part.setCode(map.get("零部件号").toString());
            part.setName(map.get("零部件名称").toString());
//            part.setPartType(map.get("辅料").toString().equals("是") ? "辅料" : "配件");
//            part.setPartType(map.get("配件类型").toString());
            part.setPartType("配件");
            part.setPrice(Double.parseDouble(map.get("结算价").toString()));
            part.setWarrantyTime(map.get("三包时间").toString());
            part.setWarrantyMileage(map.get("三包里程").toString());
//            part.setComment(map.get("备注").toString());

            partRepository.save(part);
            count++;
        }
        System.out.println("共修改了记录数：" + count);
    }
}
