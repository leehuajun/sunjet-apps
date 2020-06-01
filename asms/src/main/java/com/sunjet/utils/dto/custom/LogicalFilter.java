package com.sunjet.utils.dto.custom;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 逻辑条件表达式 用于复杂条件时使用，如但属性多对应值的 AND / OR 查询等
 *
 * @author lhj
 * @create 2015-11-15 下午11:29
 */
public class LogicalFilter implements CustomCriterion {
    private List<CustomCriterion> customCriterionList;  // 逻辑表达式中包含的表达式
    private CustomRestrictions.LogicalOperator operator;      //计算符 AND 或 OR

    public List<CustomCriterion> getCustomCriterionList() {
        return customCriterionList;
    }

    public LogicalFilter(List<CustomCriterion> criterionList, CustomRestrictions.LogicalOperator operator) {
        this.customCriterionList = criterionList;
        this.operator = operator;
    }

    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (customCriterionList.size() <= 0) {
            return null;
        } else {
            for (CustomCriterion customCriterion : customCriterionList) {
                switch (operator) {
                    case OR:
                        if (CustomSpecification.predicate == null) {
                            CustomSpecification.predicate = customCriterion.toPredicate(root, query, builder);
                        } else {
                            CustomSpecification.predicate = builder.or(CustomSpecification.predicate, customCriterion.toPredicate(root, query, builder));
                        }
                        break;
                    default:
                        if (CustomSpecification.predicate == null) {
                            CustomSpecification.predicate = customCriterion.toPredicate(root, query, builder);
                        } else {
                            CustomSpecification.predicate = builder.and(CustomSpecification.predicate, customCriterion.toPredicate(root, query, builder));
                        }
                        break;
                }
            }
        }
        return CustomSpecification.predicate;
    }
}
