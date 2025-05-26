package com.ak.store.emailSender.facade;

import com.ak.store.emailSender.service.EmailEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailEventFacade {
    private final EmailEventService emailEventService;


}