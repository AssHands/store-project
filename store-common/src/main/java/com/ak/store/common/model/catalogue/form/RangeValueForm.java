package com.ak.store.common.model.catalogue.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RangeValueForm {
    private Integer from;
    private Integer to;
}
