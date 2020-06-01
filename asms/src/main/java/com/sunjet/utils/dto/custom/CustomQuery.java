package com.sunjet.utils.dto.custom;

import com.sunjet.utils.dto.SearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 通用查询服务
 *
 * @author lhj
 * @create 2015-11-17 下午3:58
 */

public class CustomQuery<T> {
    private JpaSpecificationExecutor<T> jpaSpecificationExecutor;

    public CustomQuery() {
    }

    public CustomQuery(JpaSpecificationExecutor<T> jpaSpecificationExecutor) {
        this.jpaSpecificationExecutor = jpaSpecificationExecutor;
    }

    public void setJpaSpecificationExecutor(JpaSpecificationExecutor<T> jpaSpecificationExecutor) {
        this.jpaSpecificationExecutor = jpaSpecificationExecutor;
    }

//    private Page<T> getPageContent(SearchDTO searchDTO) {
//        Integer pageNo = 0;
//        Integer pageSize = 0;
//        if (searchDTO.getPageNumber() == null || searchDTO.getPageNumber() < 1) {
//            pageNo = 0;
//        } else {
//            pageNo = searchDTO.getPageNumber() ;
//        }
//        if (searchDTO.getPageSize() == null || searchDTO.getPageSize() < 1) {
//            pageSize = Integer.MAX_VALUE;
//        } else {
//            pageSize = searchDTO.getPageSize();
//        }
//
//        PageRequest pageRequest = buildPageRequest(pageNo,pageSize, searchDTO.getSearchOrderList());
//        Specification<T> spec = null;
//        if(searchDTO.getCustomCriterion()!=null){
//            spec = buildSpecification(searchDTO.getCustomCriterion());
//        }
//        Page<T> page = jpaSpecificationExecutor.findAll(spec, pageRequest);
//        return page;
//    }

    public Page<T> getPageContent(SearchDTO searchDTO) {
        Integer pageNo = 0;
        Integer pageSize = 0;
        if (searchDTO.getPageNumber() == null || searchDTO.getPageNumber() < 1) {
            pageNo = 0;
        } else {
            pageNo = searchDTO.getPageNumber();
        }
        if (searchDTO.getPageSize() == null || searchDTO.getPageSize() < 1) {
            pageSize = Integer.MAX_VALUE;
        } else {
            pageSize = searchDTO.getPageSize();
        }

        PageRequest pageRequest = buildPageRequest(pageNo, pageSize, searchDTO.getSearchOrderList());

        Specification<T> spec = null;
        if (searchDTO.getSpecification() == null) {
            if (searchDTO.getCustomCriterion() != null) {
                spec = buildSpecification(searchDTO.getCustomCriterion());
            }
        } else {
            spec = searchDTO.getSpecification();
        }

        Page<T> page = jpaSpecificationExecutor.findAll(spec, pageRequest);
        return page;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, List<SearchOrder> searchOrderList) {
        if (searchOrderList.size() == 0) {
            return new PageRequest(pageNumber, pagzSize);
        } else {
            Sort sort = getSort(searchOrderList);
            return new PageRequest(pageNumber, pagzSize, sort);
        }
    }

    /**
     * 创建排序条件
     *
     * @param searchOrderList
     * @return
     */
    private Sort getSort(List<SearchOrder> searchOrderList) {
        Collections.sort(searchOrderList, new Comparator<SearchOrder>() {
            @Override
            public int compare(SearchOrder o1, SearchOrder o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
        for (SearchOrder searchOrder : searchOrderList) {
            orderList.add(new Sort.Order(
                    searchOrder.getOrderType() == SearchOrder.OrderType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                    searchOrder.getProperty()));
        }
        return new Sort(orderList);
    }


    /**
     * 创建动态查询条件组合.
     */
    private Specification<T> buildSpecification(CustomCriterion customCriterion) {
        Specification<T> spec = CustomSpecification.bySearchFilter(customCriterion);
        CustomSpecification.predicate = null;
        return spec;
    }
}
