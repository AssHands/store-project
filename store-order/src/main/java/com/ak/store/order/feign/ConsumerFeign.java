package com.ak.store.order.feign;

import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "catalogue", url = "http://localhost:8080/api/v1/consumer")
public interface ConsumerFeign {
    @GetMapping("consumers/{id}")
    ConsumerDTO findOne(@PathVariable Long id);

    @GetMapping("consumers/exist/{id}")
    Boolean existOne(@PathVariable Long id);
}
