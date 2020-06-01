package com.sunjet.cache;

import com.sunjet.model.admin.ConfigEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.model.helper.EntityWrapper;
import com.sunjet.repository.basic.AgencyRepository;
import com.sunjet.repository.basic.DealerRepository;
import com.sunjet.repository.basic.PartRepository;
import com.sunjet.repository.basic.VehicleRepository;
import com.sunjet.service.admin.ConfigService;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.admin.DictionaryService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.CustomContextLoaderListener;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lhj on 16/9/16.
 */
@Component("customCacheManager")
public class CacheManager {
    private static Logger logger = LoggerFactory.getLogger(CustomContextLoaderListener.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private DictionaryService dictionaryService;
    //    @Autowired
//    private UserService userService;
//    @Autowired
//    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
//    @Autowired
//    private PartRepository partRepository;
//    @Autowired
//    private VehicleRepository vehicleRepository;
//    @Autowired
//    private DealerRepository dealerRepository;
//    @Autowired
//    private AgencyRepository agencyRepository;

    /**
     * 缓存数据类（内部类）
     */
    static class Caches {
        // 系统参数 缓存
        public final static Map<String, String> configMap = new HashMap<>();
        // 数据字典 缓存
        public final static Map<String, List<DictionaryEntity>> dictionaryMap = new HashMap<>();
//        // 流程定义 缓存
//        public final static Map<String, ProcessDefinition> mapProcessDefinition = new HashMap<>();
//        // 用户列表 缓存
//        public final static Map<String, UserEntity> mapUser = new HashMap<>();
//        // 配件列表
//        public final static List<PartEntity> lstPart = new ArrayList<>();
//        // 车辆列表
//        public final static List<VehicleEntity> lstVehicle = new ArrayList<>();
//        // 服务站列表
//        public final static List<DealerEntity> lstDealer = new ArrayList<>();
//        // 合作商列表
//        public final static List<AgencyEntity> lstAgency = new ArrayList<>();
    }

//    public void initPartList(){
//        System.out.println("初始化配件列表……");
//        Caches.lstPart.clear();
//        List<PartEntity> list = partRepository.findAll();
//        Caches.lstPart.addAll(list);
//        System.out.println("配件列表初始化完成！");
//    }

//    public void initVehicleList(){
//        System.out.println("初始化车辆列表……");
//        Caches.lstVehicle.clear();
//        List<VehicleEntity> list = vehicleRepository.findAll();
//        Caches.lstVehicle.addAll(list);
//        System.out.println("车辆列表初始化完成！");
//    }

//    public void initDealerList(){
//        System.out.println("初始化服务站列表……");
//        Caches.lstDealer.clear();
//        List<DealerEntity> list = dealerRepository.findAll();
//        Caches.lstDealer.addAll(list);
//        System.out.println("服务站列表初始化完成！");
//    }
//    public void initAgencyList(){
//        System.out.println("初始化合作商列表……");
//        Caches.lstAgency.clear();
//        List<AgencyEntity> list = agencyRepository.findAll();
//        Caches.lstAgency.addAll(list);
//        System.out.println("合作商列表初始化完成！");
//    }

    //    public void initUserMap() {
//        Caches.mapUser.clear();
//        List<UserEntity> userList = userService.findAll();
//        for (UserEntity userEntity : userList) {
//            Caches.mapUser.put(userEntity.getLogId(), userEntity);
//        }
//    }
//    public void initProcessDefinition(){
//        Caches.mapProcessDefinition.clear();
//        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
//                .list();
//        for(ProcessDefinition pd : list){
//            Caches.mapProcessDefinition.put(pd.getId(),pd);
//        }
//    }
    private List<Task> getAllTasks() {
        List<Task> personalTasks = taskService.createTaskQuery()
                .taskAssignee(CommonHelper.getActiveUser().getLogId())
//                .orderByTaskCreateTime().desc()
                .list();

        List<Task> groupTasks = taskService.createTaskQuery()
                .taskCandidateUser(CommonHelper.getActiveUser().getLogId())
                .list();

        List<Task> lstTask = new ArrayList<>();
        lstTask.addAll(personalTasks);
        lstTask.addAll(groupTasks);

        //排序
        Collections.sort(lstTask, new Comparator<Task>() {
            public int compare(Task task01, Task task02) {
                //比较每个ArrayList的第二个元素
                if (task01.getCreateTime().getTime() == task02.getCreateTime().getTime()) {
                    return 0;
                } else if (task01.getCreateTime().getTime() > task02.getCreateTime().getTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        Collections.reverse(lstTask);

        return lstTask;
    }

//    public static ProcessDefinition getProcessDefinitionById(String processDefinitionId){
//        return Caches.mapProcessDefinition.get(processDefinitionId);
//    }
//    public static UserEntity getUserbyLogId(String logId){
//        return Caches.mapUser.get(logId);
//    }


    public void initCache() {
        logger.info("初始化系统数据缓存，请等待……");
        initConfig();
        initDictionary();
//        initVehicleList();
//        initPartList();
//        initDealerList();
//        initAgencyList();
//        initUserMap();
//        initProcessDefinition();
        logger.info("初始化系统数据缓存完成！");
    }

    // --- Start ---- 系统参数

    /**
     * 初始化 系统参数 缓存
     */
    public void initConfig() {
        logger.info("正在初始化【系统参数】缓存");
        Caches.configMap.clear();
        for (ConfigEntity model : configService.findAll()) {
            Caches.configMap.put(model.getConfigKey().toLowerCase(), model.getConfigValue());
        }
    }

    /**
     * 根据参数 key ，获取参数 value
     *
     * @param configKey
     * @return
     */
    public static String getConfigValue(String configKey) {
        return Caches.configMap.get(configKey.toLowerCase());
    }

    /**
     * 获取参数 所有key
     *
     * @return
     */
    public static Set<String> getConfigKeySet() {
        return Caches.configMap.keySet();
    }

    // --- End ---- 系统参数

    // --- Start ---- 数据字典

    /**
     * 初始化数据字典内容
     */
    public void initDictionary() {
        logger.info("正在初始化【数据字典】缓存");
        Caches.dictionaryMap.clear();
        List<DictionaryEntity> dictionaryEntities = dictionaryService.findAll();
        for (DictionaryEntity de : dictionaryEntities) {
            if (de.getParent() == null) {
                List<DictionaryEntity> entities = new ArrayList<>();
                for (DictionaryEntity entity : dictionaryEntities) {
                    if (entity.getParent() != null) {
                        if (entity.getParent().getObjId().equalsIgnoreCase(de.getObjId())) {
                            entities.add(entity);
                        }
                    }
                }
                Caches.dictionaryMap.put(de.getCode(), entities);
            }
        }
    }

    /**
     * 根据字典中 父节点的 code，获取所有子节点
     *
     * @param dictionaryCode
     * @return
     */
    public static List<DictionaryEntity> getDictionariesByParentCode(String dictionaryCode) {
        return Caches.dictionaryMap.get(dictionaryCode);
    }

    /**
     * 获取数据字典所有 key 集合
     *
     * @return
     */
    public static Set<String> getDictionaryKeySet() {
        return Caches.dictionaryMap.keySet();
    }

    /**
     * 2. 根据父节点的 code 属性，获取所有子节点，并用 EntityWrapper 对象包装每个子节点。
     *
     * @param parentCode
     * @return
     */
    public static List<EntityWrapper<DictionaryEntity>> getWrappersByParentCode(String parentCode) {
        List<EntityWrapper<DictionaryEntity>> entityWrappers = new ArrayList<>();
        List<DictionaryEntity> entities = getDictionariesByParentCode(parentCode);
        return getEntityWrappers(entityWrappers, entities);
    }

    /**
     * 1. 增加一个 "全部" 子节点
     * 2. 根据父节点的 code 属性，获取所有子节点，并用 EntityWrapper 对象包装每个子节点。
     *
     * @param parentCode
     * @return
     */
    public static List<EntityWrapper<DictionaryEntity>> getWrappersByParentCodeAddAllItem(String parentCode) {
        List<EntityWrapper<DictionaryEntity>> entityWrappers = new ArrayList<>();
        List<DictionaryEntity> entities = getDictionariesByParentCode(parentCode);
        entityWrappers.add(new EntityWrapper<>(new DictionaryEntity("全部", "0"), true));
        return getEntityWrappers(entityWrappers, entities);
    }

    /**
     * 辅助方法
     *
     * @param entityWrappers
     * @param entities
     * @return
     */
    private static List<EntityWrapper<DictionaryEntity>> getEntityWrappers(List<EntityWrapper<DictionaryEntity>> entityWrappers, List<DictionaryEntity> entities) {
        for (DictionaryEntity entity : entities) {
            entityWrappers.add(new EntityWrapper<>(entity, false));
        }
        return entityWrappers;
    }
    // --- End ---- 数据字典

//    public static List<PartEntity> getPartList(){
//        return Caches.lstPart;
//    }
//    public static List<VehicleEntity> getVehicleList(){
//        return Caches.lstVehicle;
//    }
//    public static List<DealerEntity> getDealerList(){
//        return Caches.lstDealer;
//    }
//    public static List<AgencyEntity> getAgencyList(){
//        return Caches.lstAgency;
//    }

}
