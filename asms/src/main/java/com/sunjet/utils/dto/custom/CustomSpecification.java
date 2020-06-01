package com.sunjet.utils.dto.custom;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 自定义通用Specification
 *
 * @author lhj
 * @create 2015-11-15 下午11:18
 */
public class CustomSpecification {
    public static Predicate predicate = null;

    public static <T> Specification<T> bySearchFilter(final CustomCriterion customCriterion) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (customCriterion instanceof LogicalFilter) {
                    // 当前 criterion 是 LogicalFilter 类型
                    //predicate = genPredicate(root,query,builder,customCriterion,predicate);
                    predicate = customCriterion.toPredicate(root, query, builder);

                } else {              // 当前 criterion 是 SimpleFilter 类型
                    predicate = ((SimpleFilter) customCriterion).toPredicate(root, query, builder);
                }

//                Predicate predicate01 = CustomRestrictions.like("name","5",true).toPredicate(root,query,builder);
//                Predicate predicate02 = CustomRestrictions.like("logId","8",true).toPredicate(root,query,builder);
//                Predicate predicate03 = CustomRestrictions.like("name","2",true).toPredicate(root,query,builder);
//                Predicate p = builder.and(predicate03,builder.or(predicate01,predicate02));

                return predicate;
            }
        };
    }
}
