package com.ak.store.emailSender.repository;

import com.ak.store.emailSender.model.entity.EmailEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailEventRepo extends JpaRepository<EmailEvent, UUID> {
}
