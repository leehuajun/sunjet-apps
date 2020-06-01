package com.sunjet.vm;

import com.sunjet.model.admin.UserEntity;
import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.ListModelList;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class TabWindowVM {
    private Logger logger = Logger.getLogger(this.getClass());

    private ListModelList<UserEntity> userEntityListEntity = new ListModelList<>();

    public ListModelList<UserEntity> getUserEntityListEntity() {
        return userEntityListEntity;
    }

    public void setUserEntityListEntity(ListModelList<UserEntity> userEntityListEntity) {
        this.userEntityListEntity = userEntityListEntity;
    }

    @Init
    public void init() {

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
//        logger.info("DIV的长宽值:" + this.mainDiv.getWidth() + " ---  " + this.mainDiv.getAttribute("height"));
        //this.mainGrid.setHeight(this.mainDiv.getHeight());
    }

    @Command
    public void changeSize() {
//        logger.info("DIV的长宽值:" + this.mainDiv.getAttribute("width") + " ---  " + this.mainDiv.getAttribute("height"));
    }
}
