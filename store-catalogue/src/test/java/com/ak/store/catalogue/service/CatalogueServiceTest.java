package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.catalogue.utils.CatalogueUtils;
import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import com.ak.store.common.dto.search.Filters;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.ak.store.catalogue.TestCatalogueFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

//@SpringBootTest(classes = TestConfig.class) need for test containers
@ExtendWith(MockitoExtension.class)
public class CatalogueServiceTest {
    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private CharacteristicRepo characteristicRepo;

    @Mock
    private CatalogueUtils catalogueUtils;

    @Mock
    private CatalogueMapper catalogueMapper;

    @InjectMocks
    private CatalogueService catalogueService;

    @Nested
    class CharacteristicTests {

        @Test
        public void findAllAvailableCharacteristicByCategory_VerifyMethodCallsAndReturnValue() {
            long CharacteristicId = 1L;
            List<Characteristic> characteristicList = List.of(
                    mock(Characteristic.class),
                    mock(Characteristic.class),
                    mock(Characteristic.class)
            );
            Filters expectedFilters = mock(Filters.class);

            when(characteristicRepo.findAllWithTextValuesByCategoryId(CharacteristicId)).thenReturn(characteristicList);
            when(catalogueMapper.mapToFilters(characteristicList)).thenReturn(expectedFilters);

            Filters actualFilters = catalogueService.findAllAvailableCharacteristicByCategory(CharacteristicId);

            assertThat(actualFilters).isEqualTo(expectedFilters);
            verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(CharacteristicId);
            verify(catalogueMapper, times(1)).mapToFilters(characteristicList);
        }

        @Test
        public void findAllAvailableCharacteristicByCategory_VerifyMethodCallsAndReturnValue_emptyCollection() {
            long CharacteristicId = 1L;
            List<Characteristic> characteristicList = Collections.emptyList();
            Filters expectedFilters = mock(Filters.class);

            when(characteristicRepo.findAllWithTextValuesByCategoryId(CharacteristicId)).thenReturn(characteristicList);
            when(catalogueMapper.mapToFilters(characteristicList)).thenReturn(expectedFilters);

            Filters actualFilters = catalogueService.findAllAvailableCharacteristicByCategory(CharacteristicId);

            assertThat(actualFilters).isEqualTo(expectedFilters);
            verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(CharacteristicId);
            verify(catalogueMapper, times(1)).mapToFilters(characteristicList);
        }
    }

    @Nested
    class CategoryTests {

        @Test
        public void findAllCategory_VerifyMethodCallsAndReturnValue() {
            List<Category> categoryList = List.of(
                    mock(Category.class),
                    mock(Category.class),
                    mock(Category.class)
            );
            List<CategoryDTO> expectedCategoryList = List.of(
                    mock(CategoryDTO.class),
                    mock(CategoryDTO.class),
                    mock(CategoryDTO.class)
            );


            when(categoryRepo.findAll()).thenReturn(categoryList);
            when(catalogueUtils.buildCategoryTree(any())).thenReturn(expectedCategoryList);

            List<CategoryDTO> actualCategoryList = catalogueService.findAllCategory();

            assertThat(actualCategoryList).isEqualTo(expectedCategoryList);
            verify(categoryRepo, times(1)).findAll();
            verify(catalogueMapper, times(categoryList.size())).mapToCategoryDTO(any());
            verify(catalogueUtils, times(1)).buildCategoryTree(any());
        }

        @Test
        public void findAllCategory_VerifyMethodCallsAndReturnValue_emptyCollection() {
            List<Category> categoryList = Collections.emptyList();
            List<CategoryDTO> expectedCategoryList = Collections.emptyList();

            when(categoryRepo.findAll()).thenReturn(categoryList);
            when(catalogueUtils.buildCategoryTree(any())).thenReturn(expectedCategoryList);

            List<CategoryDTO> actualCategoryList = catalogueService.findAllCategory();

            assertThat(actualCategoryList).isEqualTo(expectedCategoryList);
            verify(categoryRepo, times(1)).findAll();
            verify(catalogueMapper, never()).mapToCategoryDTO(any());
            verify(catalogueUtils, times(1)).buildCategoryTree(any());
        }
    }
}