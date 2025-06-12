package com.ak.store.synchronization.repository.postgres;

import com.ak.store.synchronization.model.entity.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepo extends JpaRepository<Characteristic, Long> {
}