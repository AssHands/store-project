package com.ak.store.catalogue.model.pojo;

import com.ak.store.catalogue.model.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessedProductImages {
    List<Image> newImages = new ArrayList<>();
    LinkedHashMap<String, MultipartFile> imagesForAdd = new LinkedHashMap<>();
    List<String> imageKeysForDelete = new ArrayList<>();
}
