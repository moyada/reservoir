package com.moyada.reservoir.repository;

import com.moyada.reservoir.domain.ReservoirTrendDO;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface ReservoirTrendRepository extends CrudRepository<ReservoirTrendDO, Integer>, JpaSpecificationExecutor<ReservoirTrendDO> {
}
