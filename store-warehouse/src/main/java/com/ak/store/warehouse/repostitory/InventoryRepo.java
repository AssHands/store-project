package com.ak.store.warehouse.repostitory;

import com.ak.store.warehouse.model.entity.Inventory;
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
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    List<Inventory> findAllByProductIdIn(List<Long> productIds);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = AvailableHints.HINT_SPEC_LOCK_TIMEOUT, value = "5000")})
    List<Inventory> findAllLockedPessimisticReadByProductIdIn(List<Long> id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = AvailableHints.HINT_SPEC_LOCK_TIMEOUT, value = "5000")})
    Optional<Inventory> findOneLockedPessimisticReadByProductId(Long id);

    Optional<Inventory> findOneByProductId(Long productId);
}
