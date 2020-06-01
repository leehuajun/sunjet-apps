package com.sunjet.utils.dto.custom;

import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;

/**
 * 简单条件表达式
 *
 * @author lhj
 * @create 2015-11-15 下午11:27
 */
public class SimpleFilter implements CustomCriterion {
    private String property;       //属性名
    private CustomRestrictions.SimpleOperator operator;      //逻辑符
    private Object value;           //对应值

    public SimpleFilter(String property, CustomRestrictions.SimpleOperator operator, Object value) {
        this.property = property;
        this.value = value;
        this.operator = operator;
    }

    public String getProperty() {
        return property;
    }

    public Object getValue() {
        return value;
    }

    public CustomRestrictions.SimpleOperator getOperator() {
        return operator;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path expression = null;
        if (property.contains(".")) {
            String[] names = StringUtils.split(property, ".");
            expression = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                expression = expression.get(names[i]);
            }
        } else {
            expression = root.get(property);
        }

        switch (operator) {
            case EQ:
                return builder.equal(expression, value);//=
            case NE:
                return builder.notEqual(expression, value);//<>
            case LIKE:
                return builder.like((Expression<String>) expression, "%" + value.toString() + "%");//like
            case LT:
                return builder.lessThan(expression, (Comparable) value);//<
            case GT:
                return builder.greaterThan(expression, (Comparable) value);//>
            case LTE:
                return builder.lessThanOrEqualTo(expression, (Comparable) value);//<=
            case GTE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value);//>=
//      case CONTAIN:
//        return builder.isMember(expression,(Comparable) value);
            default:
                return null;
        }
    }
}
