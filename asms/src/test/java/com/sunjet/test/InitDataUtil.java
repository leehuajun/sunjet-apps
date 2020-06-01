package com.sunjet.test;

import com.sunjet.model.admin.*;
import com.sunjet.model.basic.*;
import com.sunjet.repository.admin.*;
import com.sunjet.repository.basic.*;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.utils.common.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lhj on 16/6/30.
 */

public class InitDataUtil {
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
    public void initData() {
        initConfig();
        initUser();
        initMenu();
        initOperation();
//        initIcon();
//        initProvince();
        initDictionary();
        initDocumentNo();
    }

    @Test
    public void initRoles() {
        List<String> roleNames = Arrays.asList("服务经理", "服务网络专员", "服务网络经理", "销售总监", "配件主管", "结算主管",
                "配件渠道专员", "售后工程师", "售后技术主管", "服务保障科科长", "回访员", "客户管理主管", "服务网络科科长", "售后工程主管",
                "审单员", "配件分配专员", "二次分配专员", "故障件管理员", "结算员", "结算经理", "网络专员", "一级服务站业务员", "二级服务站业务员", "合作商业务员", "运维工程师");
        RoleRepository roleRepository = context.getBean(RoleRepository.class);
        for (int i = 0; i < roleNames.size(); i++) {
            roleRepository.save(new RoleEntity(roleNames.get(i), "role" + StringHelper.genFixedString(i, 3), true));
        }
//        roleRepository.save(new RoleEntity("运维工程师", "role024", true));
    }

    public void initUser() {
        UserRepository userRepository = context.getBean(UserRepository.class);
        // 默认密码是:111111

    }

    @Test
    public void initUsers() {
        UserRepository userRepository = context.getBean(UserRepository.class);
        userRepository.save(new UserEntity("管理员", "admin", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("梁玮", "liangwei", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("林国鑫", "linguoxin", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("欧培", "oupei", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("陈敏婵", "chenminchan", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("莫绍民", "moshaomin", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("谭华敏", "tanhuamin", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("杜文理", "duwenli", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("韦劲松", "weijinsong", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("刘佳伟", "liujiawei", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("张传强", "zhangchuanqiang", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("蒲正树", "puzhengshu", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("邹燕", "zouyan", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("周裕峰", "zhouyufeng", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("韦越仕", "weiyueshi", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("罗红正", "luohongzheng", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("姜建文", "jiangjianwen", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("谭娟", "tanjuan", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("盘巧莲", "panqiaolian", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("闭瑜", "biyu", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("钟海航", "zhonghaihang", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("韦睿", "weirui", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("张广生", "zhangguangsheng", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("苏永桂", "suyonggui", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("李达军", "lidajun", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("覃熊", "qinxiong", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("熊志杰", "xiongzhijie", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("谢柳明", "xieliuming", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("韦券波", "weiquanbo", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("温柳徳", "wenliude", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("刘始贵", "liushigui", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("巫勇霖", "wuyonglin", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("韦幻", "weihuan", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("黄宝恨", "huangbaohen", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("邓再生", "dengzaisheng", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("樊建明", "fanjianming", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("黄书勇", "huangshuyong", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("莫绍生", "moshaosheng", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("温鹏", "wenpeng", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("黄永斌", "huangyongbin", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("吴宇祥", "wuyuxiang", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("杨伟作", "yangweizuo", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
        userRepository.save(new UserEntity("运维工程师", "oe01", "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
    }

    @Test
    public void importMaintains() {
        MaintainRepository maintainRepository = context.getBean(MaintainRepository.class);

        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_maintains");
        for (Map<String, Object> map : mapList) {
            MaintainEntity maintainEntity = new MaintainEntity();
            maintainEntity.setCode(map.get("code").toString());
            maintainEntity.setName(map.get("name").toString());
            maintainEntity.setClaim(map.get("claim").toString().equals("是") ? true : false);
            maintainEntity.setWorkTime(Double.parseDouble(map.get("hours").toString()));
            maintainEntity.setComment(map.get("comment").toString());
            maintainRepository.save(maintainEntity);
        }
    }

    @Test
    public void importAgecies() {
        AgencyService agencyService = context.getBean(AgencyService.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_agencies");
        for (Map<String, Object> map : mapList) {
            AgencyEntity agencyEntity = new AgencyEntity();
            agencyEntity.setCode(map.get("code").toString());
            agencyEntity.setName(map.get("name").toString());
            agencyEntity.setAddress(map.get("address").toString());
            agencyEntity.setPhone(map.get("phone").toString());
            agencyEntity.setFax(map.get("fax").toString());
            agencyEntity.setLegalPerson(map.get("legal_person").toString());
            agencyEntity.setLegalPersonPhone(map.get("legal_person_phone").toString());
            agencyEntity.setShopManager(map.get("shop_manager").toString());
            agencyEntity.setShopManagerPhone(map.get("shop_manager_phone").toString());
            agencyEntity.setProductsOfSupply(map.get("products_of_supply").toString());
            agencyEntity.setTaxpayerCode(map.get("taxpayer_code").toString());
            agencyEntity.setBank(map.get("bank").toString());
            agencyEntity.setBankAccount(map.get("bank_account").toString());
            agencyService.saveAgency(agencyEntity);
        }
    }

    /**
     * 导入车辆、经销商、车主信息
     */
    @Test
    public void importVehicles() {
        VehicleRepository vehicleRepository = context.getBean(VehicleRepository.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_vehicles2");
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
            vehicle.setMobile(map.get("手机").toString());
//            vehicle.setEmail(map.get("电子邮件").toString());
            vehicle.setVmt("0");
            vehicleRepository.save(vehicle);
        }
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
//        String[] dds = strDate.split("/");
//        String strdd = "20" + dds[2] + "-" + dds[0] + "-" + dds[1];
//          date = format.parse(strdd);
                // 如果是 2016-04-06 这种日期格式，则不需要转换
                date = format.parse(strDate);
            }
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return date;
    }

    /**
     * 导入零部件
     */
    @Test
    public void importPart() {
        PartRepository partRepository = context.getBean(PartRepository.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_parts");
        for (Map<String, Object> map : mapList) {
            PartEntity part = new PartEntity();
            part.setCode(map.get("配件件号").toString());
            part.setName(map.get("配件名称").toString());
//            part.setPartType(map.get("辅料").toString().equals("是") ? "辅料" : "配件");
            part.setPartType(map.get("配件类型").toString());
            part.setPrice(Double.parseDouble(map.get("配件单价").toString()));
            part.setWarrantyTime(map.get("三包时间").toString());
            part.setWarrantyMileage(map.get("三包里程").toString());
//            part.setComment(map.get("备注").toString());

            partRepository.save(part);
        }

    }

    @Test
    public void initDealerUsers() {
        DealerRepository dealerRepository = context.getBean(DealerRepository.class);
        List<DealerEntity> dealers = dealerRepository.findAll();

        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_users");

        UserRepository userRepository = context.getBean(UserRepository.class);
        List<UserEntity> userEntities = userRepository.findAll();

        for (Map<String, Object> map : mapList) {

            boolean exists = false;
            for (UserEntity ue : userEntities) {
                if (ue.getLogId().equals(map.get("用户登录名"))) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                for (DealerEntity dealerEntity : dealers) {
                    // 存在
                    if (dealerEntity.getCode() != null && dealerEntity.getCode().equals(map.get("服务站编号").toString())) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setDealer(dealerEntity);
                        userEntity.setName(map.get("用户姓名").toString());
                        userEntity.setLogId(map.get("用户登录名").toString());
                        userEntity.setPassword("5dfdd6a7ec1924ca2ac2898a570587fc");
                        userEntity.setSalt("dacfw");
                        userEntity.setUserType("dealer");
                        userEntity.setEnabled(true);

                        userRepository.save(userEntity);
//                    userRepository.save(new UserEntity(map.get("用户姓名").toString(), map.get("用户登录名").toString(), "5dfdd6a7ec1924ca2ac2898a570587fc", "dacfw", true));
                        break;
                    }
                }
            }
        }
    }

    /**
     * 导入服务站
     */
    @Test
    public void importDealers() {
        DealerRepository dealerRepository = context.getBean(DealerRepository.class);
        ProvinceRepository provinceRepository = context.getBean(ProvinceRepository.class);
        List<ProvinceEntity> provinceEntities = provinceRepository.findAll();
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from tmp_dealers");
        UserRepository userRepository = context.getBean(UserRepository.class);
        List<UserEntity> userEntities = userRepository.findAll();
        List<DealerEntity> dealers = dealerRepository.findAll();
        for (Map<String, Object> map : mapList) {
            boolean exists = false;
            for (DealerEntity dealerEntity : dealers) {

                // 存在，就修改
                if (dealerEntity.getCode() != null && dealerEntity.getCode().equals(map.get("服务站编号").toString())) {
                    exists = true;

                    dealerEntity.setCode(map.get("服务站编号").toString());
                    dealerEntity.setName(map.get("服务站名称").toString());
//      dealer.setStar(getStar(map.get("星级")));
                    dealerEntity.setStar(map.get("星级").toString());
                    dealerEntity.setAddress(map.get("地址").toString());
                    dealerEntity.setPhone(map.get("电话").toString());
                    dealerEntity.setFax(map.get("传真").toString());
                    dealerEntity.setSgmwSystem(map.get("SGMW体系").toString() == "SGMW" ? true : false);
                    dealerEntity.setQualification(map.get("维修资质").toString());

                    for (ProvinceEntity provice : provinceEntities) {
                        if (provice.getName().contains(map.get("所在省").toString())) {
                            dealerEntity.setProvince(provice);
                            break;
                        }
                    }

                    for (UserEntity user : userEntities) {
                        if (user.getName().equals(map.get("服务经理").toString())) {
                            dealerEntity.setServiceManager(user);
                        }
                    }
                    dealerRepository.save(dealerEntity);
                    break;
                }
            }
            // 存在，即修改
            if (!exists) {


                DealerEntity dealer = new DealerEntity();
                dealer.setCode(map.get("服务站编号").toString());
                dealer.setName(map.get("服务站名称").toString());
//      dealer.setStar(getStar(map.get("星级")));
                dealer.setStar(map.get("星级").toString());
                dealer.setAddress(map.get("地址").toString());
                dealer.setPhone(map.get("电话").toString());
                dealer.setFax(map.get("传真").toString());
//      dealer.setLegalPerson(map.get("法人代表").toString());
//      dealer.setLegalPersonPhone(map.get("法人联系方式").toString());
//      dealer.setStationMaster(map.get("站长").toString());
//      dealer.setStationMasterPhone(map.get("站长联系方式").toString());
//      dealer.setTechnicalDirector(map.get("技术主管").toString());
//      dealer.setTechnicalDirectorPhone(map.get("技术主管联系方式").toString());
//      dealer.setClaimDirector(map.get("索赔主管").toString());
//      dealer.setClaimDirectorPhone(map.get("索赔主管联系方式").toString());
//      dealer.setPartDirector(map.get("配件主管").toString());
//      dealer.setPartDirectorPhone(map.get("配件主管联系方式").toString());
//      dealer.setFinanceDirector(map.get("财务经理").toString());
//      dealer.setFinanceDirectorPhone(map.get("财务经理联系方式").toString());
//      dealer.setEmployeeCount(map.get("员工总数").toString());
//      dealer.setReceptionistCount(map.get("接待员").toString());
//      dealer.setPartKeeyperCount(map.get("配件员").toString());
//      dealer.setMaintainerCount(map.get("维修工").toString());
//      dealer.setQcInspectorCount(map.get("质检员").toString());
//      dealer.setClerkCount(map.get("结算员").toString());
//      dealer.setParkingArea(map.get("停车场面积").toString());
//      dealer.setReceptionArea(map.get("接待室").toString());
//      dealer.setGeneralArea(map.get("综合维修区").toString());
//      dealer.setAssemblyArea(map.get("总成维修区").toString());
//      dealer.setStorageArea(map.get("配件库总面积").toString());
//      dealer.setStorageWulingArea(map.get("五菱配件库面积").toString());
//      dealer.setStorageUserdPartArea(map.get("旧件库面积").toString());
//      dealer.setStorageWulingUserdPartArea(map.get("五菱旧件库面积").toString());
                dealer.setSgmwSystem(map.get("SGMW体系").toString() == "SGMW" ? true : false);
                dealer.setQualification(map.get("维修资质").toString());
//      dealer.setOtherMaintainCondition(map.get("其它车辆维修条件").toString());
//      dealer.setProductsOfMaintain(map.get("拟维修我公司产品系列").toString());
//      dealer.setOtherBrand(map.get("还兼做哪些品牌的服务").toString());

                for (ProvinceEntity provice : provinceEntities) {
                    if (provice.getName().contains(map.get("所在省").toString())) {
                        dealer.setProvince(provice);
                        break;
                    }
                }

                for (UserEntity user : userEntities) {
                    if (user.getName().equals(map.get("服务经理").toString())) {
                        dealer.setServiceManager(user);
                    }
                }
//            for (UserEntity user : userEntities) {
//                if (user.getName().contains(map.get("区域服务经理").toString())) {
//                    dealer.setServiceManager(user);
//                    break;
//                }
//            }
                dealerRepository.save(dealer);
            }
        }
    }

    private String getStar(Object star) {
        if (Integer.parseInt(star.toString()) == 3) {
            return "三星级";
        } else if (Integer.parseInt(star.toString()) == 4) {
            return "四星级";
        } else if (Integer.parseInt(star.toString()) == 5) {
            return "五星级";
        }
        return "";
    }


    public void initDocumentNo() {
        DocumentNoRepository documentNoRepository = context.getBean(DocumentNoRepository.class);
        documentNoRepository.save(new DocumentNoEntity("售后质量速报单", "ZLSB", LocalDate.now().toString().replace("-", "")));
        documentNoRepository.save(new DocumentNoEntity("售后费用速报单", "FYSB", LocalDate.now().toString().replace("-", "")));
    }

    @Test
    public void initDictionary() {
        DictionaryRepository dictionaryRepository = context.getBean(DictionaryRepository.class);
        DictionaryEntity parent;
        // 其他合作内容
        parent = dictionaryRepository.save(new DictionaryEntity("其他合作内容", "10060"));
        dictionaryRepository.save(new DictionaryEntity("客车经销商", parent));
        dictionaryRepository.save(new DictionaryEntity("改装车经销商", parent));
        dictionaryRepository.save(new DictionaryEntity("非道路车经销商", parent));
        dictionaryRepository.save(new DictionaryEntity("配件经销商", parent));
        // 拟维修我公司产品系列
        parent = dictionaryRepository.save(new DictionaryEntity("拟维修我公司产品系列", "10050"));
        dictionaryRepository.save(new DictionaryEntity("柴油车", parent));
        dictionaryRepository.save(new DictionaryEntity("天然气车", parent));
        dictionaryRepository.save(new DictionaryEntity("电动车", parent));
        dictionaryRepository.save(new DictionaryEntity("特种车辆", parent));

//        // 流程状态字典
//        parent = dictionaryRepository.save(new DictionaryEntity("流程状态", "10000"));
//        dictionaryRepository.save(new DictionaryEntity("草稿", parent));
//        dictionaryRepository.save(new DictionaryEntity("审核中", parent));
//        dictionaryRepository.save(new DictionaryEntity("已审核", parent));
//        dictionaryRepository.save(new DictionaryEntity("退回", parent));
//
//        // 服务站星级字典
//        parent = dictionaryRepository.save(new DictionaryEntity("服务站星级", "10010"));
//        dictionaryRepository.save(new DictionaryEntity("三星级", parent));
//        dictionaryRepository.save(new DictionaryEntity("四星级", parent));
//        dictionaryRepository.save(new DictionaryEntity("五星级", parent));
//
//        // 服务站维修资质 字典
//        parent = dictionaryRepository.save(new DictionaryEntity("服务站维修资质", "10020"));
//        dictionaryRepository.save(new DictionaryEntity("一类", parent));
//        dictionaryRepository.save(new DictionaryEntity("二类(汽车综合小修)", parent));
//        dictionaryRepository.save(new DictionaryEntity("三类(发动机维修)", parent));
//        dictionaryRepository.save(new DictionaryEntity("三类(发动机维修)"));
//
////        // 车辆排量 字典
//        parent = dictionaryRepository.save(new DictionaryEntity("车辆排量", "10030"));
//        dictionaryRepository.save(new DictionaryEntity("排量小于1.0L", parent));
//        dictionaryRepository.save(new DictionaryEntity("排量大于等于1.0L", parent));
//
//        // 售后费用速报 费用类型 字典
//        parent = dictionaryRepository.save(new DictionaryEntity("售后费用速报-费用类型", "10030"));
//        dictionaryRepository.save(new DictionaryEntity("非质保", parent));
//        dictionaryRepository.save(new DictionaryEntity("特殊维修", parent));
//        dictionaryRepository.save(new DictionaryEntity("其他", parent));
//
//        parent = dictionaryRepository.save(new DictionaryEntity("售后质量速报-速报级别", "10040"));
//        dictionaryRepository.save(new DictionaryEntity("普通", parent));
//        dictionaryRepository.save(new DictionaryEntity("严重", parent));
//
//        parent = dictionaryRepository.save(new DictionaryEntity("流程节点类型", "11000"));
//        dictionaryRepository.save(new DictionaryEntity("人工节点", parent));
//        dictionaryRepository.save(new DictionaryEntity("系统节点", parent));
//
//        parent = dictionaryRepository.save(new DictionaryEntity("用户类型", "12000"));
////        dictionaryRepository.save(new DictionaryEntity("五菱工业公司用户",parent));
//        dictionaryRepository.save(new DictionaryEntity("服务站用户", parent));
//        dictionaryRepository.save(new DictionaryEntity("合作商用户", parent));

//        parent = dictionaryRepository.save(new DictionaryEntity("首保费用标准", "13000"));
//        dictionaryRepository.save(new DictionaryEntity("1.0L排量车型", null, 10, "185", parent));
//        dictionaryRepository.save(new DictionaryEntity("1.6L客车", null, 20, "265", parent));
//        dictionaryRepository.save(new DictionaryEntity("1.9L柴油车型", null, 30, "550", parent));
//        dictionaryRepository.save(new DictionaryEntity("电动观光车", null, 40, "150", parent));
//
//        parent = dictionaryRepository.save(new DictionaryEntity("三包维修单据类型", "14000"));
//        dictionaryRepository.save(new DictionaryEntity("三包维修", parent));
//        dictionaryRepository.save(new DictionaryEntity("配件销售质保", parent));
//        dictionaryRepository.save(new DictionaryEntity("其他", parent));
    }

    public void initProvince() {
//        ChinaRepository chinaRepository = context.getBean(ChinaRepository.class);
//        RegionRepository regionRepository = context.getBean(RegionRepository.class);
//        List<ChinaEntity> chinaEntities = chinaRepository.findAll();
//
//        ProvinceEntity province = null;
//        CityEntity city = null;
//
//        for (ChinaEntity entity : chinaEntities) {
//            if (province == null) {
//                province = regionRepository.save(new ProvinceEntity("", entity.getProvince()));
//            } else {
//                if (province.getName().equalsIgnoreCase(entity.getProvince())) {
//                    if (city == null) {
//                        city = regionRepository.save(new CityEntity(1, "", entity.getCity(), province));
//                    } else {
//                        if (city.getName().equalsIgnoreCase(entity.getCity())) {
//                            regionRepository.save(new CountyEntity("", entity.getCounty(), city));
//                        } else {
//                            city = regionRepository.save(new CityEntity(2, "", entity.getCity(), province));
//                        }
//                    }
//                } else {
//                    province = regionRepository.save(new ProvinceEntity("", entity.getProvince()));
//                }
//            }
//        }
    }

    /**
     * 初始化系统参数内容
     */
    @Test
    public void initConfig() {
        ConfigRepository configRepository = context.getBean(ConfigRepository.class);
//        configRepository.save(new ConfigEntity("page_size", "15", "10", "分页时每页记录数量"));
//        configRepository.save(new ConfigEntity("dialog_position", "center,center", "center,center", "模态窗体显示未知"));
//        configRepository.save(new ConfigEntity("init_password", "111111", "111111", "用户默认密码"));
//        configRepository.save(new ConfigEntity("app_name", "整车售后服务系统", "企业运营管理系统", "系统名称"));
//        configRepository.save(new ConfigEntity("app_logo", "resource/images/logo-wuling.png", "resource/images/logo.png", "系统LOGO(53x38)"));
//        configRepository.save(new ConfigEntity("treenode_icon", "z-icon-credit-card", "z-icon-credit-card", "树节点"));
//        configRepository.save(new ConfigEntity("frigid_subsidy", "3", "3", "严寒地区工时补贴(元)"));
//        configRepository.save(new ConfigEntity("doc_sn_length", "4", "4", "单据流水号长度"));
        configRepository.save(new ConfigEntity("first_city_price", "13", "13", "一级城市工时单价"));
        configRepository.save(new ConfigEntity("second_city_price", "16", "16", "二级级城市工时单价"));
//        configRepository.save(new ConfigEntity("frigid_months", "10,11,12,1,2,3", "", "严寒月份,月份中间用逗隔开(比如1,2,3"));
    }


    @Test
    public void initMenu() {
        MenuRepository menuRepository = context.getBean(MenuRepository.class);
        MenuEntity parent = null;

        parent = menuRepository.save(new MenuEntity("流程管理", 950, true));
        menuRepository.save(new MenuEntity("流程定义", 95010, "/views/flow/process_definition_list.zul", parent));
        menuRepository.save(new MenuEntity("任务管理", 95020, "/views/flow/task_list.zul", parent));
//
//        parent = menuRepository.save(new MenuEntity("系统管理", 1000, true));
//        menuRepository.save(new MenuEntity("系统图标", 100010, "/views/admin/icon_list.zul", parent));
////        menuRepository.save(new MenuEntity("系统参数", 100020, "/views/admin/config_list.zul", parent));
//        menuRepository.save(new MenuEntity("资源管理", 100030, "/views/admin/resource_list.zul", parent));
//        menuRepository.save(new MenuEntity("用户管理", 100040, "/views/admin/user_list.zul", parent));
//        menuRepository.save(new MenuEntity("角色管理", 100050, "/views/admin/role_list.zul", parent));
//        menuRepository.save(new MenuEntity("组织管理", 100060, "/views/admin/org.zul", parent));
//        menuRepository.save(new MenuEntity("菜单管理", 100070, "/views/admin/menu_list.zul", parent));
//        menuRepository.save(new MenuEntity("操作列表", 100080, "/views/admin/operation_list.zul", parent));
//        menuRepository.save(new MenuEntity("定时任务", 100090, "/views/admin/schedulejob_list.zul", parent));
//        menuRepository.save(new MenuEntity("Web服务日志", 100100, "/views/admin/ws_access_log_list.zul", parent));
//
//        parent = menuRepository.save(new MenuEntity("基础管理", 990, true));
//        menuRepository.save(new MenuEntity("服务站", 99010, "/views/basic/dealer_list.zul", parent));
//        menuRepository.save(new MenuEntity("合作商", 99020, "/views/basic/agency_list.zul", parent));
//        menuRepository.save(new MenuEntity("车辆", 99030, "/views/basic/vehicle_list.zul", parent));
//        menuRepository.save(new MenuEntity("车主", 99040, "/views/basic/owner_list.zul", parent));
//        menuRepository.save(new MenuEntity("维修项目", 99050, "/views/basic/maintain_list.zul", parent));
//        menuRepository.save(new MenuEntity("严寒省份设置", 99060, "/views/basic/province_list.zul", parent));
//        menuRepository.save(new MenuEntity("数据字典", 99070, "/views/basic/dictionary_list.zul", parent));
//        menuRepository.save(new MenuEntity("参数设置", 99080, "/views/basic/config_list.zul", parent));
//        menuRepository.save(new MenuEntity("配件目录", 99090, "/views/basic/part_list.zul", parent));
//
//        parent = menuRepository.save(new MenuEntity("服务站管理", 10, true));
//        menuRepository.save(new MenuEntity("服务站准入申请", 1010, "/views/dealer/dealer_admit_list.zul", parent));
//        menuRepository.save(new MenuEntity("服务站变更申请", 1020, "/views/dealer/dealer_alter_list.zul", parent));
//        menuRepository.save(new MenuEntity("服务站退出申请", 1030, "/views/dealer/dealer_quit_list.zul", parent));
//        menuRepository.save(new MenuEntity("服务站等级变更申请", 1040, "/views/dealer/dealer_level_change_list.zul", parent));
//
//        parent = menuRepository.save(new MenuEntity("合作商管理", 20, true));
//        menuRepository.save(new MenuEntity("合作商准入申请", 2010, "/views/agency/agency_admit_list.zul", parent));
//        menuRepository.save(new MenuEntity("合作商变更申请", 2020, "/views/agency/agency_alter_list.zul", parent));
//        menuRepository.save(new MenuEntity("合作商退出申请", 2030, "/views/agency/agency_quit_list.zul", parent));
//
//        parent = menuRepository.save(new MenuEntity("售后服务管理", 30, true));
//        menuRepository.save(new MenuEntity("服务委托单", 3010, "/views/asm/service_proxy_list.zul", parent));
//        menuRepository.save(new MenuEntity("首保服务单", 3020, "/views/asm/first_maintenance_list.zul", parent));
//        menuRepository.save(new MenuEntity("三包服务单", 3030, "/views/asm/warrant_maintenance_list.zul", parent));
//        menuRepository.save(new MenuEntity("活动服务单", 3040, "/views/asm/activity_maintenance_list.zul", parent));
//        menuRepository.save(new MenuEntity("配件调拨单", 3050, "/views/asm/supply_notice_list.zul", parent));
//        menuRepository.save(new MenuEntity("配件供货单", 3060, "/views/asm/supply_list.zul", parent));
//        menuRepository.save(new MenuEntity("故障件返回单", 3070, "/views/asm/recycle_list.zul", parent));
//        menuRepository.save(new MenuEntity("待返回清单", 3080, "/views/asm/recycle_wait_list.zul", parent));
//        menuRepository.save(new MenuEntity("售后质量速报", 3090, "/views/asm/quality_report_list.zul", parent));
//        menuRepository.save(new MenuEntity("售后费用速报", 3100, "/views/asm/expense_report_list.zul", parent));
//        menuRepository.save(new MenuEntity("活动通知", 3120, "/views/asm/activity_notice_list.zul", parent));
//        menuRepository.save(new MenuEntity("活动分配单", 3130, "/views/asm/activity_distribution_list.zul", parent));
//
//        parent = menuRepository.save(new MenuEntity("费用结算", 40, true));
//        menuRepository.save(new MenuEntity("三包费用结算", 4010, "/views/settlement/warrant_maintenance_cost.zul", parent));
//        menuRepository.save(new MenuEntity("运费结算", 4020, "/views/settlement/freight_cost.zul", parent));
//        menuRepository.save(new MenuEntity("配件费用结算", 4030, "/views/settlement/part_cost.zul", parent));
//
//        parent = menuRepository.save(new MenuEntity("统计报表", 50, true));
//        menuRepository.save(new MenuEntity("维修记录查询", 5010, "/views/rpt/maintenance_list.zul", parent));
//        menuRepository.save(new MenuEntity("配件信息统计", 5020, "/views/rpt/part_info_list.zul", parent));
//        menuRepository.save(new MenuEntity("三包费用清单", 5030, "/views/rpt/warrant_maintenance_cost_list.zul", parent));
//        menuRepository.save(new MenuEntity("首保费用清单", 5030, "/views/rpt/first_maintenance_cost_list.zul", parent));
//        menuRepository.save(new MenuEntity("活动费用清单", 5030, "/views/rpt/activity_cost_list.zul", parent));
//
//        parent = menuRepository.save(new MenuEntity("公告", 100, true));
//        menuRepository.save(new MenuEntity("公告", 10010, "/views/notice/notice_all_list.zul", parent));
//        menuRepository.save(new MenuEntity("本站通知", 10020, "/views/notice/notice_me_list.zul", parent));


    }

    @Test
    public void initOperation() {
        OperationRepository operationRepository = context.getBean(OperationRepository.class);
        operationRepository.save(new OperationEntity("search", "查询", 10));
        operationRepository.save(new OperationEntity("create", "创建", 20));
        operationRepository.save(new OperationEntity("modify", "修改", 30));
        operationRepository.save(new OperationEntity("delete", "删除", 40));
        operationRepository.save(new OperationEntity("audit", "审核", 50));
        operationRepository.save(new OperationEntity("disaudit", "弃审", 60));
        operationRepository.save(new OperationEntity("import", "导入", 70));
        operationRepository.save(new OperationEntity("export", "导出", 80));
        operationRepository.save(new OperationEntity("print", "打印", 90));
    }

    public void initIcon() {
//        IconOriginRepository iconOriginRepository = context.getBean(IconOriginRepository.class);
//        IconRepository iconRepository = context.getBean(IconRepository.class);
//        List<IconOriginEntity> iconOriginEntities = iconOriginRepository.findAll();
//
//        for (IconOriginEntity iconOriginEntity : iconOriginEntities) {
//            iconRepository.save(new IconEntity(iconOriginEntity.getIconName()));
//        }
    }


}
