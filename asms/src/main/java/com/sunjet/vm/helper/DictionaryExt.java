package com.sunjet.vm.helper;


import com.sunjet.model.admin.DictionaryEntity;

import com.sunjet.service.base.DictionaryTypeEnum;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/6/14.
 */
public class DictionaryExt {
    private DictionaryEntity selectedItem_LJGNFL = null;   // 零件功能分类
    private DictionaryEntity selectedItem_PJSX = null;     // 配件属性
    private List<DictionaryEntity> selectedList_PJSX = new ArrayList<>();     // 配件属性  List
    private DictionaryEntity selectedItem_WLLX = null;     // 物料类型
    private DictionaryEntity selectedItem_GYS = null;      // 供应商
    private DictionaryEntity selectedItem_PJK = null;      // 配件库
    private DictionaryEntity selectedItem_FHD = null;      //发货地
    private DictionaryEntity selectedItem_YSFS = null;      // 运输方式
    private DictionaryEntity selectedItem_SAP_DDLX = null;      // SAP订单类型
    private DictionaryEntity selectedItem_CPZ = null;      // 产品组
    private DictionaryEntity selectedItem_XSZZ = null;      // 销售组织
    private DictionaryEntity selectedItem_FXQD = null;      // 分销渠道
    private DictionaryEntity selectedItem_GC = null;      // 工厂
    private DictionaryEntity selectedItem_CYS = null;    //承运商

    private List<DictionaryEntity> lstDictionary_FHD = new ArrayList<>(); //列表：发货地
    private List<DictionaryEntity> lstDictionaryAll = new ArrayList<>();     // 所有数据字典内容
    private List<DictionaryEntity> lstDictionary_LJGNFL = new ArrayList<>(); // 列表:零件功能分类
    private List<DictionaryEntity> lstDictionary_PJSX = new ArrayList<>();   // 列表:配件(物料)属性
    private List<DictionaryEntity> lstDictionary_WLLX = new ArrayList<>();   // 列表:物料类型
    private List<DictionaryEntity> lstDictionary_GYS = new ArrayList<>();    // 列表:供应商
    private List<DictionaryEntity> lstDictionary_PJK = new ArrayList<>();    // 列表:配件库
    private List<DictionaryEntity> lstDictionary_YSFS = new ArrayList<>();   // 列表:运输方式
    private List<DictionaryEntity> lstDictionary_SAP_DDLX = new ArrayList<>();   // 列表:SAP订单类型
    private List<DictionaryEntity> lstDictionary_CPZ = new ArrayList<>();   // 列表:产品组
    private List<DictionaryEntity> lstDictionary_XSZZ = new ArrayList<>();   // 列表:销售组织
    private List<DictionaryEntity> lstDictionary_FXQD = new ArrayList<>();   // 列表:分销渠道
    private List<DictionaryEntity> lstDictionary_GC = new ArrayList<>();   // 列表:工厂
    private List<DictionaryEntity> lstDictionary_CYS = new ArrayList<>();   // 列表:承运商


    public List<DictionaryEntity> getLstDictionary_CYS() {
        return lstDictionary_CYS;
    }

    public DictionaryEntity getSelectedItem_CYS() {
        return selectedItem_CYS;
    }

    public void setSelectedItem_CYS(DictionaryEntity selectedItem_CYS) {
        this.selectedItem_CYS = selectedItem_CYS;
    }

    public DictionaryEntity getSelectedItem_YSFS() {
        return selectedItem_YSFS;
    }

    public void setSelectedItem_YSFS(DictionaryEntity selectedItem_YSFS) {
        this.selectedItem_YSFS = selectedItem_YSFS;
    }

    public DictionaryEntity getSelectedItem_SAP_DDLX() {
        return selectedItem_SAP_DDLX;
    }

    public void setSelectedItem_SAP_DDLX(DictionaryEntity selectedItem_SAP_DDLX) {
        this.selectedItem_SAP_DDLX = selectedItem_SAP_DDLX;
    }

    public DictionaryEntity getSelectedItem_CPZ() {
        return selectedItem_CPZ;
    }

    public void setSelectedItem_CPZ(DictionaryEntity selectedItem_CPZ) {
        this.selectedItem_CPZ = selectedItem_CPZ;
    }

    public DictionaryEntity getSelectedItem_XSZZ() {
        return selectedItem_XSZZ;
    }

    public void setSelectedItem_XSZZ(DictionaryEntity selectedItem_XSZZ) {
        this.selectedItem_XSZZ = selectedItem_XSZZ;
    }

    public DictionaryEntity getSelectedItem_FXQD() {
        return selectedItem_FXQD;
    }

    public void setSelectedItem_FXQD(DictionaryEntity selectedItem_FXQD) {
        this.selectedItem_FXQD = selectedItem_FXQD;
    }

    public DictionaryEntity getSelectedItem_GC() {
        return selectedItem_GC;
    }

    public void setSelectedItem_GC(DictionaryEntity selectedItem_GC) {
        this.selectedItem_GC = selectedItem_GC;
    }

    public DictionaryEntity getSelectedItem_LJGNFL() {
        return selectedItem_LJGNFL;
    }

    public void setSelectedItem_LJGNFL(DictionaryEntity selectedItem_LJGNFL) {
        this.selectedItem_LJGNFL = selectedItem_LJGNFL;
    }

    public List<DictionaryEntity> getSelectedList_PJSX() {
        return selectedList_PJSX;
    }

    public void setSelectedList_PJSX(List<DictionaryEntity> selectedList_PJSX) {
        this.selectedList_PJSX = selectedList_PJSX;
    }

    public DictionaryEntity getSelectedItem_PJSX() {
        return selectedItem_PJSX;
    }

    public void setSelectedItem_PJSX(DictionaryEntity selectedItem_PJSX) {
        this.selectedItem_PJSX = selectedItem_PJSX;
    }

    public DictionaryEntity getSelectedItem_WLLX() {
        return selectedItem_WLLX;
    }

    public void setSelectedItem_WLLX(DictionaryEntity selectedItem_WLLX) {
        this.selectedItem_WLLX = selectedItem_WLLX;
    }

    public DictionaryEntity getSelectedItem_GYS() {
        return selectedItem_GYS;
    }

    public void setSelectedItem_GYS(DictionaryEntity selectedItem_GYS) {
        this.selectedItem_GYS = selectedItem_GYS;
    }

    public DictionaryEntity getSelectedItem_PJK() {
        return selectedItem_PJK;
    }

    public void setSelectedItem_PJK(DictionaryEntity selectedItem_PJK) {
        this.selectedItem_PJK = selectedItem_PJK;
    }

    public List<DictionaryEntity> getLstDictionaryAll() {
        return lstDictionaryAll;
    }

    public List<DictionaryEntity> getLstDictionary_LJGNFL() {
        return lstDictionary_LJGNFL;
    }

    public List<DictionaryEntity> getLstDictionary_PJSX() {
        return lstDictionary_PJSX;
    }

    public List<DictionaryEntity> getLstDictionary_WLLX() {
        return lstDictionary_WLLX;
    }

    public List<DictionaryEntity> getLstDictionary_GYS() {
        return lstDictionary_GYS;
    }

    public List<DictionaryEntity> getLstDictionary_PJK() {
        return lstDictionary_PJK;
    }

    public List<DictionaryEntity> getLstDictionary_YSFS() {
        return lstDictionary_YSFS;
    }

    public List<DictionaryEntity> getLstDictionary_SAP_DDLX() {
        return lstDictionary_SAP_DDLX;
    }

    public List<DictionaryEntity> getLstDictionary_CPZ() {
        return lstDictionary_CPZ;
    }

    public List<DictionaryEntity> getLstDictionary_XSZZ() {
        return lstDictionary_XSZZ;
    }

    public List<DictionaryEntity> getLstDictionary_FXQD() {
        return lstDictionary_FXQD;
    }

    public List<DictionaryEntity> getLstDictionary_GC() {
        return lstDictionary_GC;
    }

    public DictionaryEntity getSelectedItem_FHD() {
        return selectedItem_FHD;
    }

    public void setSelectedItem_FHD(DictionaryEntity selectedItem_FHD) {
        this.selectedItem_FHD = selectedItem_FHD;
    }

    public List<DictionaryEntity> getLstDictionary_FHD() {
        return lstDictionary_FHD;
    }

    public DictionaryExt(List<DictionaryEntity> lstDictionaryAll) {
        this.lstDictionaryAll = lstDictionaryAll;
        try {
            initData(true);
            initStorage(true);
            initOriginName(true);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public DictionaryExt(List<DictionaryEntity> lstDictionaryAll, boolean isQuery) {
        this.lstDictionaryAll = lstDictionaryAll;
        try {
            initData(isQuery);
            initStorage(isQuery);
            initOriginName(isQuery);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

//  private DictionaryModel selectedItem_SAP_DDLX = null;      // SAP订单类型
//  private DictionaryModel selectedItem_CPZ = null;      // 产品组
//  private DictionaryModel selectedItem_XSZZ = null;      // 销售组织
//  private DictionaryModel selectedItem_FXQD = null;      // 分销渠道
//  private DictionaryModel selectedItem_GC = null;      // 工厂

    /**
     * 获取 数据字典 的内容（除库点外）
     */
    private void initData(boolean isQuery) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (DictionaryEntity model : this.lstDictionaryAll) {
            if (model.getParent() != null) {
                if (model.getParent().getCode().equals(DictionaryTypeEnum.LJGNFL)) {
                    lstDictionary_LJGNFL.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.GYS)) {
                    lstDictionary_GYS.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.WLSX)) {
                    lstDictionary_PJSX.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.WLLX)) {
                    lstDictionary_WLLX.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.YSFX)) {
                    lstDictionary_YSFS.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.SAP_DDLX)) {
                    lstDictionary_SAP_DDLX.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.CPZ)) {
                    lstDictionary_CPZ.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.XSZZ)) {
                    lstDictionary_XSZZ.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.FXQD)) {
                    lstDictionary_FXQD.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.GC)) {
                    lstDictionary_GC.add((DictionaryEntity) BeanUtils.cloneBean(model));
                } else if (model.getParent().getCode().equals(DictionaryTypeEnum.CYS)) {
                    lstDictionary_CYS.add((DictionaryEntity) BeanUtils.cloneBean(model));
                }
            }
        }
        this.selectedItem_LJGNFL = this.lstDictionary_LJGNFL.get(0);
        this.selectedItem_GYS = this.lstDictionary_GYS.get(0);
        this.selectedItem_PJSX = this.lstDictionary_PJSX.get(0);
        this.selectedItem_WLLX = this.lstDictionary_WLLX.get(0);
        this.selectedItem_YSFS = this.lstDictionary_YSFS.get(0);
        this.selectedItem_SAP_DDLX = this.lstDictionary_SAP_DDLX.get(0);
        this.selectedItem_CPZ = this.lstDictionary_CPZ.get(0);
        this.selectedItem_XSZZ = this.lstDictionary_XSZZ.get(0);
        this.selectedItem_FXQD = this.lstDictionary_FXQD.get(0);
        this.selectedItem_GC = this.lstDictionary_GC.get(0);
        this.selectedItem_CYS = this.lstDictionary_CYS.get(0);
        if (isQuery) {

            if (this.lstDictionary_LJGNFL.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_LJGNFL = defaultModel;
                this.lstDictionary_LJGNFL.add(0, defaultModel);
            }

            if (this.lstDictionary_GYS.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_GYS = defaultModel;
                lstDictionary_GYS.add(0, defaultModel);
            }

            if (this.lstDictionary_PJSX.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_PJSX = defaultModel;
                lstDictionary_PJSX.add(defaultModel);
            }
            if (this.lstDictionary_WLLX.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_WLLX = defaultModel;
                lstDictionary_WLLX.add(defaultModel);
            }

            if (this.lstDictionary_YSFS.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_YSFS = defaultModel;
                lstDictionary_YSFS.add(defaultModel);
            }

            if (this.lstDictionary_SAP_DDLX.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_SAP_DDLX = defaultModel;
                lstDictionary_SAP_DDLX.add(defaultModel);
            }

            if (this.lstDictionary_CPZ.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_CPZ = defaultModel;
                lstDictionary_CPZ.add(defaultModel);
            }

            if (this.lstDictionary_XSZZ.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_XSZZ = defaultModel;
                lstDictionary_XSZZ.add(defaultModel);
            }

            if (this.lstDictionary_FXQD.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_FXQD = defaultModel;
                lstDictionary_FXQD.add(defaultModel);
            }

            if (this.lstDictionary_GC.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_GC = defaultModel;
                lstDictionary_GC.add(defaultModel);
            }
            if (this.lstDictionary_CYS.size() > 1) {
                DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
                this.selectedItem_CYS = defaultModel;
                lstDictionary_CYS.add(defaultModel);
            }
        }
    }

    /**
     * 获取 数据字典（库点）内容
     */
    private void initStorage(boolean isQuery) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (DictionaryEntity model : this.lstDictionaryAll) {
            if (model.getParent() != null) {
                if (model.getParent().getCode().equals(DictionaryTypeEnum.PJK)) {
                    for (DictionaryEntity model1 : this.lstDictionaryAll) {
                        if (model1.getParent() != null) {
                            if (model1.getParent().getObjId().equals(model.getObjId())) {
                                lstDictionary_PJK.add((DictionaryEntity) BeanUtils.cloneBean(model1));
                            }
                        }
                    }
                }
            }
        }
        selectedItem_PJK = lstDictionary_PJK.get(0);
        if (isQuery) {
            DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
            this.selectedItem_PJK = defaultModel;
            lstDictionary_PJK.add(0, defaultModel);
        }

    }

    private void initOriginName(boolean isQuery) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (DictionaryEntity model : this.lstDictionaryAll) {
            if (model.getParent() != null) {
                if (model.getParent().getCode().equals(DictionaryTypeEnum.PJK)) {
                    lstDictionary_FHD.add((DictionaryEntity) BeanUtils.cloneBean(model));
                }
            }
        }
        selectedItem_FHD = lstDictionary_FHD.get(0);
        if (isQuery) {
            DictionaryEntity defaultModel = new DictionaryEntity("全部", "all", 1);
            this.selectedItem_FHD = defaultModel;
            lstDictionary_FHD.add(0, defaultModel);
        }

    }
}
