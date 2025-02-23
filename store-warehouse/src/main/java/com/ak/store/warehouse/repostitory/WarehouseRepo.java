package com.ak.store.warehouse.repostitory;

import com.ak.store.warehouse.model.Warehouse;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findAllByProductIdIn(List<Long> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = AvailableHints.HINT_SPEC_LOCK_TIMEOUT, value = "5000")})
    List<Warehouse> findAllLockedByProductIdIn(List<Long> productIds);

    Optional<Warehouse> findOneByProductId(Long productId);
}
