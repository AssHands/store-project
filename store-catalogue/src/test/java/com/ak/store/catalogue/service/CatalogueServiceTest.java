package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.util.CatalogueUtils;
import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import com.ak.store.common.dto.catalogue.product.CharacteristicDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//@SpringBootTest(classes = TestConfig.class) need for test containers
@ExtendWith(MockitoExtension.class)
public class CatalogueServiceTest {
    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private CharacteristicRepo characteristicRepo;

    @Mock
    private CatalogueMapper catalogueMapper;

    @InjectMocks
    private CatalogueService catalogueService;

    @Nested
    class CharacteristicTests {

        @Test
        public void findAllAvailableCharacteristicByCategory_VerifyMethodCallsAndReturnValue() {
            long CharacteristicId = 1L;
            List<CharacteristicDTO> expectedCharacteristicDTOList = List.of(
                    mock(CharacteristicDTO.class),
                    mock(CharacteristicDTO.class),
                    mock(CharacteristicDTO.class)
            );
            List<Characteristic> characteristicList = List.of(
                    mock(Characteristic.class),
                    mock(Characteristic.class),
                    mock(Characteristic.class)
            );

            when(characteristicRepo.findAllWithTextValuesByCategoryId(CharacteristicId)).thenReturn(characteristicList);
            when(catalogueMapper.mapToCharacteristicDTO(any(Characteristic.class)))
                    .thenAnswer(AdditionalAnswers.returnsElementsOf(expectedCharacteristicDTOList));

            List<CharacteristicDTO> actualCharacteristicDTOList = catalogueService.findAllAvailableCharacteristicByCategory(CharacteristicId);

            assertThat(actualCharacteristicDTOList).isEqualTo(expectedCharacteristicDTOList);
            verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(CharacteristicId);
            verify(catalogueMapper, times(expectedCharacteristicDTOList.size())).mapToCharacteristicDTO(any());
        }

        @Test
        public void findAllAvailableCharacteristicByCategory_VerifyMethodCallsAndReturnValue_emptyCollection() {
            long CharacteristicId = 1L;
            List<CharacteristicDTO> expectedCharacteristicDTOList = Collections.emptyList();
            List<Characteristic> characteristicList = Collections.emptyList();

            when(characteristicRepo.findAllWithTextValuesByCategoryId(CharacteristicId)).thenReturn(characteristicList);

            List<CharacteristicDTO> actualCharacteristicDTOList = catalogueService.findAllAvailableCharacteristicByCategory(CharacteristicId);

            assertThat(actualCharacteristicDTOList).isEqualTo(expectedCharacteristicDTOList);
            verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(CharacteristicId);
            verify(catalogueMapper, never()).mapToCharacteristicDTO(any());
        }
    }

    @Nested
    class CategoryTests {

        private MockedStatic<CatalogueUtils> mockedStaticCatalogueUtils;

        @BeforeEach
        public void setUp() {
            mockedStaticCatalogueUtils = mockStatic(CatalogueUtils.class);
        }

        @AfterEach
        public void tearDown() {
            mockedStaticCatalogueUtils.close();
        }

        @Test
        public void findAllCategory_VerifyMethodCallsAndReturnValue() {
            List<Category> categoryList = List.of(
                    mock(Category.class),
                    mock(Category.class),
                    mock(Category.class)
            );
            List<CategoryDTO> expectedCategoryDTOList = List.of(
                    mock(CategoryDTO.class),
                    mock(CategoryDTO.class),
                    mock(CategoryDTO.class)
            );

            when(categoryRepo.findAll()).thenReturn(categoryList);
            mockedStaticCatalogueUtils.when(() -> CatalogueUtils.buildCategoryTree(any())).thenReturn(expectedCategoryDTOList);

            List<CategoryDTO> actualCategoryList = catalogueService.findAllCategory();

            assertThat(actualCategoryList).isEqualTo(expectedCategoryDTOList);
            verify(categoryRepo, times(1)).findAll();
            verify(catalogueMapper, times(categoryList.size())).mapToCategoryDTO(any());
            mockedStaticCatalogueUtils.verify(() -> CatalogueUtils.buildCategoryTree(any()), times(1));
        }

        @Test
        public void findAllCategory_VerifyMethodCallsAndReturnValue_emptyCollection() {
            List<Category> categoryList = Collections.emptyList();
            List<CategoryDTO> expectedCategoryList = Collections.emptyList();

            when(categoryRepo.findAll()).thenReturn(categoryList);
            mockedStaticCatalogueUtils.when(() -> CatalogueUtils.buildCategoryTree(any())).thenReturn(expectedCategoryList);

            List<CategoryDTO> actualCategoryList = catalogueService.findAllCategory();

            assertThat(actualCategoryList).isEqualTo(expectedCategoryList);
            verify(categoryRepo, times(1)).findAll();
            mockedStaticCatalogueUtils.verify(() -> CatalogueUtils.buildCategoryTree(any()), times(1));
            verify(catalogueMapper, never()).mapToCategoryDTO(any());
        }
    }
}