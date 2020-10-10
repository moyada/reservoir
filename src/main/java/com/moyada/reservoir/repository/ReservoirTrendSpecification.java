package com.moyada.reservoir.repository;

import com.moyada.reservoir.domain.ReservoirInfoDO;
import com.moyada.reservoir.domain.ReservoirTrendDO;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ReservoirTrendSpecification implements Specification<ReservoirTrendDO> {

    private final ReservoirTrendDO condition;

    public ReservoirTrendSpecification(ReservoirTrendDO condition) {
        this.condition = condition;
    }

    @Override
    public Predicate toPredicate(Root<ReservoirTrendDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("reservoir_id"), condition.getReservoirId());
//        if (condition.getCreateTime() == null) {
//            criteriaQuery.where(criteriaBuilder.equal(root.get("reservoir_id"), condition.getReservoirId()));
//        } else {
//            criteriaQuery.where(criteriaBuilder.and(
//                    criteriaBuilder.equal(root.get("reservoir_id"), condition.getReservoirId()),
//                    criteriaBuilder.greaterThan(root.get("create_time"), condition.getCreateTime())));
//        }
//        criteriaQuery.orderBy(QueryUtils.toOrders(Sort.by("id DESC"), root, criteriaBuilder));
//        return criteriaQuery.getRestriction();
    }
}
