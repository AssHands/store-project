package com.ak.store.consumer.repository;

import com.ak.store.consumer.model.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepo extends JpaRepository<Consumer, Long> {
}
