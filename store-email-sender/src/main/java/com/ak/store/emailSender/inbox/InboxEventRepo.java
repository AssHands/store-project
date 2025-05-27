package com.ak.store.emailSender.inbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InboxEventRepo extends JpaRepository<InboxEvent, UUID> {
}
