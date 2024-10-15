package com.dung.UniStore.specification;

import com.dung.UniStore.entity.Order;
import com.dung.UniStore.form.OrderFilterForm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;


public class OrderSpecification {
    private static final String SEARCH_FULL_NAME = "fullName";
    private static final String TOTAL_MONEY = "totalMoney";
    private static final String ACTIVE = "active";

    public static Specification<Order> buildWhere(OrderFilterForm form) {
        if (form == null) {
            return null;
        }

        Specification<Order> whereFullName = form.getFullName() != null ? new SpecificationImpl(SEARCH_FULL_NAME, form.getFullName()) : null;
        Specification<Order> whereTotalMoney = form.getTotalMoney() != null ? new SpecificationImpl(TOTAL_MONEY, form.getTotalMoney()) : null;
        Specification<Order> whereActive = form.getActive() != null ? new SpecificationImpl(ACTIVE, form.getActive()) : null;

        Specification<Order> where = Specification.where(whereFullName);

        if (whereTotalMoney != null) {
            where = where.and(whereTotalMoney);
        }

        if (whereActive != null) {
            where = where.and(whereActive);
        }

        return where;
    }

    @AllArgsConstructor
    public static class SpecificationImpl implements Specification<Order> {
        private String key;
        private Object value;

        @Override
        public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            if (key == null || value == null) {
                return null;
            }
            switch (key) {
                case SEARCH_FULL_NAME:
                    return criteriaBuilder.like(root.get(SEARCH_FULL_NAME), "%" + value + "%");
                case TOTAL_MONEY:
                    if (value instanceof Number) {
                        return criteriaBuilder.greaterThanOrEqualTo(root.get(TOTAL_MONEY),value.toString());
                    }
                    break;
                case ACTIVE:
                    if (value instanceof Boolean) {
                        return criteriaBuilder.equal(root.get(ACTIVE), value);
                    }
                    break;
            }
            return null;

        }
    }
}
