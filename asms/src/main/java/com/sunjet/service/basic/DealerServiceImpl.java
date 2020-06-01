package com.sunjet.service.basic;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.repository.basic.DealerRepository;
import com.sunjet.service.admin.UserService;
import com.sunjet.utils.common.CommonHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lhj on 16/9/17.
 */
@Service("dealerService")
public class DealerServiceImpl implements DealerService {
    @Autowired
    private UserService userService;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private CacheManager customCacheManager;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerRepository;
    }

    @Override
    public DealerEntity save(DealerEntity dealerEntity) {
        DealerEntity dealer = dealerRepository.save(dealerEntity);
//        customCacheManager.initDealerList();
        return dealer;
    }

    @Override
    public List<DealerEntity> findAllByKeyword(String keyword) {
        return dealerRepository.findAllByKeyword("%" + keyword + "%");
//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }
//        String lowerFilter = keyword.toLowerCase();
//        Stream<DealerEntity> dealerStream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getEnabled() == false)
//                    return false;
//                if (dealer.getCode() == null || dealer.getName() == null)
//                    return false;
//                if (dealer.getCode().toLowerCase().contains(lowerFilter)
//                        || dealer.getName().toLowerCase().contains(lowerFilter)) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        return dealerStream.collect(Collectors.toList());
    }

    @Override
    public List<DealerEntity> findParentDealersByCity(CityEntity selectedCity) {
        return dealerRepository.findParentDealersByCity(selectedCity);
//
//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }
//
//        Stream<DealerEntity> dealerStream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getEnabled() == false)
//                    return false;
//                if (dealer.getCode() == null || dealer.getName() == null)
//                    return false;
//                if (dealer.getCity().getObjId() == selectedCity.getObjId()
//                        && dealer.getParent() == null) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        return dealerStream.collect(Collectors.toList());
    }

    @Override
    public List<DealerEntity> findParentDealersByProvince(ProvinceEntity selectedProvince) {
        return dealerRepository.findParentDealersByProvince(selectedProvince);
//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }
//        Stream<DealerEntity> dealerStream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getEnabled() == false)
//                    return false;
//                if (dealer.getProvince().getObjId() == selectedProvince.getObjId()
//                        && dealer.getParent() == null) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        return dealerStream.collect(Collectors.toList());
    }

    @Override
    public List<DealerEntity> findParentDealers() {
        return dealerRepository.findParentDealers();
//
//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }
//
//        Stream<DealerEntity> dealerStream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getEnabled() == false)
//                    return false;
//                if (dealer.getParent() == null) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        return dealerStream.collect(Collectors.toList());
    }

    @Override
    public List<DealerEntity> findWithoutChild(String keyword) {
        return dealerRepository.findWithoutChild("%" + keyword + "%");

//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }
//
//        String lowerFilter = keyword.toLowerCase();
//        Stream<DealerEntity> dealerStream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getEnabled() == false)   // 服务站未启用
//                    return false;
//                if (dealer.getCode() == null || dealer.getName() == null)    // 服务站信息不全，没有编号和名字
//                    return false;
//                if (dealer.getParent() != null)   // 不是一级服务站
//                    return false;
//                if (dealer.getCode().toLowerCase().contains(lowerFilter) || dealer.getName().toLowerCase().contains(lowerFilter)) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        return dealerStream.collect(Collectors.toList());
    }

    @Override
    public List<DealerEntity> findChildrenByParentId(String objId) {

        return dealerRepository.findChildrenByParentId(objId);
//
//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }
//
//        Stream<DealerEntity> dealerStream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getEnabled() == false)
//                    return false;
//                if (dealer.getParent().getObjId().equals(objId)) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        return dealerStream.collect(Collectors.toList());
    }

    @Override
    public List<DealerEntity> findChildrenByParentIdAndFilter(String objId, String keyword) {
        return dealerRepository.findChildrenByParentIdAndFilter(objId, "%" + keyword + "%");

//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }
//
//        String lowerKeyword = keyword.toLowerCase();
//        Stream<DealerEntity> dealerStream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getEnabled() == false)
//                    return false;
////                if(dealer.getCode().equals(CommonHelper.getActiveUser().getDealer().getCode())
////
////                if(dealer.getParent()==null){
////                    if(dealer.getCode().toLowerCase().contains(lowerKeyword) || dealer.getName().toLowerCase().contains(lowerKeyword)){
////
////                    }
//                if (dealer.getCode() == null || dealer.getName() == null)
//                    return false;
//                if (dealer.getParent() == null)
//                    return false;
//                if (dealer.getParent().getCode() != null
//                        && dealer.getParent().getCode().equals(CommonHelper.getActiveUser().getDealer().getCode())
//                        && dealer.getCode().toLowerCase().contains(lowerKeyword))
//                    return true;
//                if (dealer.getParent().getName() != null
//                        && dealer.getParent().getCode().equals(CommonHelper.getActiveUser().getDealer().getCode())
//                        && dealer.getName().toLowerCase().contains(lowerKeyword))
//                    return true;
//
//                return false;
//            }
//        });
//        return dealerStream.collect(Collectors.toList());
    }

    @Override
    public List<DealerEntity> getDealersByUserIdAndKeyword(String userId, String keyword) {
        //return dealerRepository.findByUserIdAndKeyword(userId,"%"+keyword+"%");

        List<DealerEntity> dealers = new ArrayList<>();
        if (CommonHelper.getActiveUser().getDealer() != null) {   // 服务站用户
            if (CommonHelper.getActiveUser().getDealer().getParent() != null) {  // 是二级服务站
                dealers.clear();
                dealers.add(CommonHelper.getActiveUser().getDealer());
                dealers.add(CommonHelper.getActiveUser().getDealer().getParent());
            } else {   // 一级服务站
//               dealers = dealerRepository.findAllByStatusAndKeyword("%"+keyword+"%");
                dealers.add(CommonHelper.getActiveUser().getDealer());
                dealers.addAll(this.findChildrenByParentIdAndFilter(CommonHelper.getActiveUser().getDealer().getObjId(), keyword));
            }
        } else if (CommonHelper.getActiveUser().getAgency() != null) {   // 合作商
            dealers = dealerRepository.findAllByKeyword("%" + keyword + "%");
//            Stream<DealerEntity> stream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//                @Override
//                public boolean test(DealerEntity dealer) {
//                    if(StringUtils.isBlank(dealer.getCode()) || StringUtils.isBlank(dealer.getName()))
//                        return false;
//                    if(dealer.getCode().contains(keyword) || dealer.getName().contains(keyword))
//                        return true;
//
//                    return false;
//                }
//            });
//            dealers = stream.collect(Collectors.toList());
//            dealers = dealerRepository.findAllByStatusAndKeyword("%"+keyword+"%");
        } else {   // 五菱用户
//            dealers.clear();
//            dealers = dealerRepository.findAllByStatusAndKeyword("%"+keyword+"%");
//            dealers = this.findAllByKeyword(keyword);
            dealers = dealerRepository.findAllByKeywordAndServiceManager(userId, "%" + keyword + "%");

//            Stream<DealerEntity> stream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//                @Override
//                public boolean test(DealerEntity dealer) {
//                    if (dealer.getServiceManager() == null)
//                        return false;
//                    if (dealer.getCode() == null)
//                        return false;
//                    if (dealer.getName() == null)
//                        return false;
//
//                    if(dealer.getServiceManager().getObjId().equals(userId) &&(dealer.getCode().contains(keyword) || dealer.getName().contains(keyword)))
//                        return true;
//
//                    return false;
//                }
//            });
//            dealers = stream.collect(Collectors.toList());
            if (dealers.size() <= 0)
                dealers = this.findAllByKeyword(keyword);
            //return stream.collect(Collectors.toList());
        }

        return dealers;
    }

    /**
     * 检查code是否存在
     *
     * @param code
     * @return
     */
    @Override
    public Boolean checkCodeExists(String code) {
        DealerEntity dealerEntity = dealerRepository.findOneByCode(code);
        if (dealerEntity != null)
            return true;
        else
            return false;
//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }
//
//        Stream<DealerEntity> stream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getCode() == null)
//                    return false;
//                if (dealer.getCode().equals(code))
//                    return true;
//
//                return false;
//            }
//        });
//        if (stream.collect(Collectors.toList()).size() > 0)
//            return true;
//        else
//            return false;
    }

    @Override
    public List<DealerEntity> findAllByServiceManager(String userId, String keyword) {
        return dealerRepository.findAllByKeywordAndServiceManager(userId, "%" + keyword + "%");
//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }

//        Stream<DealerEntity> stream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getServiceManager() == null || StringUtils.isBlank(dealer.getCode()) || StringUtils.isBlank(dealer.getName()))
//                    return false;
//                if (dealer.getEnabled() == true && dealer.getServiceManager().getObjId().equals(userId) && (dealer.getCode().contains(keyword) || dealer.getName().contains(keyword)))
//                    return true;
//
//                return false;
//            }
//        });
//        return stream.collect(Collectors.toList());
    }

    @Override
    public DealerEntity findDealerByCode(String dealerCode) {
        return dealerRepository.findOneByCode(dealerCode);
//        if (CacheManager.getDealerList().size() <= 0) {
//            customCacheManager.initDealerList();
//        }
//
//        Stream<DealerEntity> stream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//            @Override
//            public boolean test(DealerEntity dealer) {
//                if (dealer.getCode() == null || dealer.getEnabled() == false)
//                    return false;
//                if (dealer.getCode().equals(dealerCode))
//                    return true;
//
//                return false;
//            }
//        });
//        return stream.collect(Collectors.toList()).get(0);
    }

    @Override
    public List<DealerEntity> findPrimaryByKeyword(String keyword) {
        return dealerRepository.findWithoutChild("%" + keyword + "%");
//      if (CacheManager.getDealerList().size() <= 0) {
//          customCacheManager.initDealerList();
//      }
//      String lowerFilter = keyword.toLowerCase();
//      Stream<DealerEntity> dealerStream = CacheManager.getDealerList().parallelStream().filter(new Predicate<DealerEntity>() {
//          @Override
//          public boolean test(DealerEntity dealer) {
//              if (dealer.getEnabled() == false)
//                  return false;
//              if(dealer.getParent()!=null)
//                  return false;
//              if (dealer.getCode() == null || dealer.getName() == null)
//                  return false;
//              if (dealer.getCode().toLowerCase().contains(lowerFilter)
//                  || dealer.getName().toLowerCase().contains(lowerFilter)) {
//                  return true;
//              }
//              return false;
//          }
//      });
//      return dealerStream.collect(Collectors.toList());
    }
}
