package com.ak.store.catalogue.product.service.image;

import com.ak.store.catalogue.model.pojo.ProcessedImages;
import com.ak.store.catalogue.repository.ImageFileRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ImageStorageServiceTest {

    @Mock
    private ImageFileRepo imageFileRepo;

    @InjectMocks
    private ImageStorageService imageStorageService;

    @Test
    void syncShouldCompensateWhenDeleteFails() {
        var imagesForAdd = new HashMap<String, org.springframework.web.multipart.MultipartFile>();
        imagesForAdd.put("k1", null);

        var processed = ProcessedImages.builder()
                .imagesForAdd(imagesForAdd)
                .imageKeysForDelete(List.of("k2"))
                .build();

        doThrow(new RuntimeException("boom"))
                .when(imageFileRepo)
                .deleteAllImage(processed.getImageKeysForDelete());

        assertThatThrownBy(() -> imageStorageService.sync(processed))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("boom");

        verify(imageFileRepo).compensateAddAllImage(processed.getImagesForAdd().keySet());
        verify(imageFileRepo).compensateDeleteAllImage(processed.getImageKeysForDelete());
    }
}
