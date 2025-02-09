package com.ak.store.common.dto.catalogue.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageWriteDTO {
    private Long productId;
    private Map<String, String> allImageIndexes = new HashMap<>();
    private List<MultipartFile> addImages = new ArrayList<>();
    private List<String> deleteImageIndexes = new ArrayList<>();
}
