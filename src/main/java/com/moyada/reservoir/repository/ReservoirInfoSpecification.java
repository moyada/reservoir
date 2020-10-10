package com.moyada.reservoir.repository;

import com.moyada.reservoir.domain.ReservoirInfoDO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ReservoirInfoSpecification implements Specification<ReservoirInfoDO> {

    private final ReservoirInfoDO condition;

    public ReservoirInfoSpecification(ReservoirInfoDO condition) {
        this.condition = condition;
    }

    @Override
    public Predicate toPredicate(Root<ReservoirInfoDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        /**
         * 参数一：查询的属性
         * 参数二：条件的值
         */
        List<Predicate> predicates = new ArrayList<>();
        if (condition.getOriginName() != null) {
            predicates.add(criteriaBuilder.equal(root.get("originName"), condition.getOriginName()));
        }
        if (condition.getProvinceCode() != null) {
            predicates.add(criteriaBuilder.equal(root.get("provinceCode"), condition.getProvinceCode()));
        }
        if (condition.getCityCode() != null) {
            predicates.add(criteriaBuilder.equal(root.get("cityCode"), condition.getCityCode()));
        }
        if (condition.getDeleted() != null) {
            predicates.add(criteriaBuilder.equal(root.get("deleted"), condition.getDeleted()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
