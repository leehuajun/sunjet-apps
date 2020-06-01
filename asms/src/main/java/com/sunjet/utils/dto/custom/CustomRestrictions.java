package com.sunjet.utils.dto.custom;

import org.hibernate.criterion.MatchMode;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 条件构造器
 * 用于创建条件表达式
 *
 * @author lhj
 * @create 2015-11-15 下午11:31
 */
public class CustomRestrictions {
    public enum SimpleOperator {
        EQ, NE, LIKE, GT, LT, GTE, LTE, BETWEEN, CONTAIN
    }

    public enum LogicalOperator {
        AND, OR, NOT
    }

    /**
     * 等于
     *
     * @param property
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleFilter eq(String property, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleFilter(property, SimpleOperator.EQ, value);
    }

    /**
     * 不等于
     *
     * @param property
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleFilter ne(String property, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleFilter(property, SimpleOperator.NE, value);
    }

    /**
     * 模糊匹配
     *
     * @param property
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleFilter like(String property, String value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleFilter(property, SimpleOperator.LIKE, value);
    }

    /**
     * @param property
     * @param value
     * @param matchMode
     * @param ignoreNull
     * @return
     */
    public static SimpleFilter like(String property, String value,
                                    MatchMode matchMode, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return null;
    }

    /**
     * 大于
     *
     * @param property
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleFilter gt(String property, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleFilter(property, SimpleOperator.GT, value);
    }

    /**
     * 小于
     *
     * @param property
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleFilter lt(String property, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleFilter(property, SimpleOperator.LT, value);
    }

    /**
     * 大于等于
     *
     * @param property
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleFilter lte(String property, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleFilter(property, SimpleOperator.LTE, value);
    }

    /**
     * 小于等于
     *
     * @param property
     * @param value
     * @param ignoreNull
     * @return
     */
    public static SimpleFilter gte(String property, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleFilter(property, SimpleOperator.GTE, value);
    }

    /**
     * 并且
     *
     * @param criterionList
     * @return
     */
    public static LogicalFilter and(List<CustomCriterion> criterionList) {
        return new LogicalFilter(criterionList, LogicalOperator.AND);
    }

    /**
     * 或者
     *
     * @param criterionList
     * @return
     */
    public static LogicalFilter or(List<CustomCriterion> criterionList) {
        return new LogicalFilter(criterionList, LogicalOperator.OR);
    }

    /**
     * 包含于
     *
     * @param property
     * @param value
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static LogicalFilter in(String property, Collection value, boolean ignoreNull) {
        if (ignoreNull && (value == null || value.isEmpty())) {
            return null;
        }
        List<CustomCriterion> criterionList = new ArrayList<>();
        //SimpleFilter[] ses = new SimpleFilter[value.size()];
        int i = 0;
        for (Object obj : value) {
            criterionList.add(new SimpleFilter(property, SimpleOperator.EQ, obj));
        }
        return new LogicalFilter(criterionList, LogicalOperator.OR);
    }
}
