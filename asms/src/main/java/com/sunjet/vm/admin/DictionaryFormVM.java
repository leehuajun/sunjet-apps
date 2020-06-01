package com.sunjet.vm.admin;

import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.service.admin.DictionaryService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字典 表单
 * Created by Administrator on 2016/8/4.
 */
public class DictionaryFormVM extends FormBaseVM {
    @Wire
    private Window dictionaryeditdialog;
    @WireVariable
    private DictionaryService dictionaryService;
    private DictionaryEntity currentDictionary;
    private List<DictionaryEntity> types = new ArrayList<>();

    public List<DictionaryEntity> getTypes() {
        return types;
    }

    public void setTypes(List<DictionaryEntity> types) {
        this.types = types;
    }

    public DictionaryEntity getCurrentDictionary() {
        return currentDictionary;
    }

    public void setCurrentDictionary(DictionaryEntity currentDictionary) {
        this.currentDictionary = currentDictionary;
    }

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            currentDictionary = dictionaryService.findOne(objId);
//      System.out.println(currentDictionary.getParent().getName());
        } else {
            currentDictionary = new DictionaryEntity();
        }

        types = dictionaryService.findTypes();
//        currentDictionary = (DictionaryEntity) (Executions.getCurrent().getArg().get("model"));
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    public void saveEntity() {
        currentDictionary = dictionaryService.save(currentDictionary);
        cacheManager.initDictionary();
        if (StringUtils.isBlank(objId)) {   // 新增加
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DICTIONARY_LIST, null);
        } else {   // 更新
            Map<String, Object> map = new HashMap<>();
            map.put("entity", currentDictionary);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DICTIONARY_ENTITY, map);
        }
    }
}
