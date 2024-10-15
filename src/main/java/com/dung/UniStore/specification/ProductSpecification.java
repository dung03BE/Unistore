package com.dung.UniStore.specification;

import com.dung.UniStore.entity.Product;
import com.dung.UniStore.form.ProductFilterForm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    private static final String MIN_PRICE ="minPrice";
    private static final String MAX_PRICE ="maxPrice";
    private static final String SEARCH ="search";

    public static Specification<Product> builtWhere(ProductFilterForm form)
    {
        if(form ==null)
        {
            return null;
        }
        Specification<Product> whereProductName =new Specificationimpl(SEARCH,form.getSearch());
        Specification<Product> whereMinPrice =new Specificationimpl(MIN_PRICE,form.getMinPrice());
        Specification<Product> whereMaxPrice =new Specificationimpl(MAX_PRICE,form.getMaxPrice());
        return Specification.where(whereProductName).and(whereMaxPrice.and(whereMinPrice));
    }
    @AllArgsConstructor
    public static class Specificationimpl implements Specification<Product>
    {
        private String key;
        private Object value;

        @Override
        public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            if(value==null)
            {
                return null;
            }
            switch (key)
            {
                case SEARCH:
                   return criteriaBuilder.like(root.get("name"),"%"+value+"%");
                case MIN_PRICE:
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value.toString());
                case MAX_PRICE:
                    return criteriaBuilder.lessThanOrEqualTo(root.get("price"), value.toString());
            }
            return null;
        }
    }
}
