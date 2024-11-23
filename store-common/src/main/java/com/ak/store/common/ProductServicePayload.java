package com.ak.store.common;

import co.elastic.clients.elasticsearch._types.FieldValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductServicePayload {
    List<Long> ids;
    List<Object> searchAfter;
}
