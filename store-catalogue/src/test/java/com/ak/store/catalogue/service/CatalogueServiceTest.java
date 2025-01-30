package com.ak.store.catalogue.service;

import com.ak.store.catalogue.TestCatalogueFactory;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import com.ak.store.common.dto.search.Filters;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ak.store.catalogue.TestCatalogueFactory.createCategory;
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

    @Spy
    private CatalogueMapper catalogueMapper = new CatalogueMapper(new ModelMapper());

    @InjectMocks
    private CatalogueService catalogueService;

    @Nested
    class CharacteristicTests {

        @Test
        public void findAllAvailableCharacteristicByCategory_categoryDoesNotExist() {
            Long categoryId = 1L;
            when(characteristicRepo.findAllWithTextValuesByCategoryId(categoryId)).thenReturn(Collections.emptyList());

            Filters filters = catalogueService.findAllAvailableCharacteristicByCategory(categoryId);

            assertThat(filters.getNumericFilters()).isEmpty();
            assertThat(filters.getTextFilters()).isEmpty();
            verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(categoryId);
        }

        @Test
        public void findAllAvailableCharacteristicByCategory_textCharacteristicsSupported() {
            Long categoryId = 1L;
            List<Characteristic> characteristics = TestCatalogueFactory.createTextCharacteristicList(categoryId, 1);
            when(characteristicRepo.findAllWithTextValuesByCategoryId(categoryId)).thenReturn(characteristics);

            Filters filters = catalogueService.findAllAvailableCharacteristicByCategory(categoryId);

            assertThat(filters.getNumericFilters()).isEmpty();
            assertThat(filters.getTextFilters()).isNotEmpty();
            verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(categoryId);
        }

        @Test
        public void findAllAvailableCharacteristicByCategory_numericCharacteristicsSupported() {
            Long categoryId = 1L;
            List<Characteristic> characteristics = TestCatalogueFactory.createNumericCharacteristicList(categoryId, 1);
            when(characteristicRepo.findAllWithTextValuesByCategoryId(categoryId)).thenReturn(characteristics);

            Filters filters = catalogueService.findAllAvailableCharacteristicByCategory(categoryId);

            assertThat(filters.getNumericFilters()).isNotEmpty();
            assertThat(filters.getTextFilters()).isEmpty();
            verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(categoryId);
        }

        @Test
        public void findAllAvailableCharacteristicByCategory_allCharacteristicsSupported() {
            Long categoryId = 1L;
            List<Characteristic> characteristics = TestCatalogueFactory.createAllCharacteristicList(categoryId, 2);
            when(characteristicRepo.findAllWithTextValuesByCategoryId(categoryId)).thenReturn(characteristics);

            Filters filters = catalogueService.findAllAvailableCharacteristicByCategory(categoryId);

            assertThat(filters.getNumericFilters()).isNotEmpty();
            assertThat(filters.getTextFilters()).isNotEmpty();
            verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(categoryId);
        }
    }

    @Nested
    class CategoryTests {

        @Test
        public void findAllCategory_withoutCategories() {
            when(categoryRepo.findAll()).thenReturn(Collections.emptyList());

            List<CategoryDTO> categoryDTOList = catalogueService.findAllCategory();

            assertThat(categoryDTOList).isEmpty();
            verify(categoryRepo, times(1)).findAll();
        }

        @Test
        public void findAllCategory_withChildCategories() {
            List<Category> categories = new ArrayList<>();
            categories.add(createCategory(1L, "root 1"));
            categories.add(createCategory(2L, "child 2", 1L));
            categories.add(createCategory(3L, "child 3", 2L));
            categories.add(createCategory(4L, "child 4", 1L));
            categories.add(createCategory(5L, "root 5"));
            when(categoryRepo.findAll()).thenReturn(categories);

            List<CategoryDTO> categoryDTOList = catalogueService.findAllCategory();

            assertCategory(categoryDTOList.get(0), 1L, "root 1", List.of(
                    CategoryDTO.builder().id(2L).name("child 2").parentId(1L).childCategories(
                            List.of(CategoryDTO.builder().id(3L).name("child 3").parentId(2L).build())).build(),
                    CategoryDTO.builder().id(4L).name("child 4").parentId(1L).build()
            ));
            assertCategory(categoryDTOList.get(1), 5L, "root 5", List.of());
            verify(categoryRepo, times(1)).findAll();
        }

        @Test
        public void findAllCategory_withoutChildCategories() {
            List<Category> categories = new ArrayList<>();
            categories.add(createCategory(1L, "root 1"));
            categories.add(createCategory(2L, "root 2"));
            categories.add(createCategory(3L, "root 3"));
            when(categoryRepo.findAll()).thenReturn(categories);

            List<CategoryDTO> categoryDTOList = catalogueService.findAllCategory();

            assertCategory(categoryDTOList.get(0), 1L, "root 1", List.of());
            assertCategory(categoryDTOList.get(1), 2L, "root 2", List.of());
            assertCategory(categoryDTOList.get(2), 3L, "root 3", List.of());
            verify(categoryRepo, times(1)).findAll();
        }

        @Test
        public void findAllCategory_withoutRootCategory() {
            List<Category> categories = new ArrayList<>();
            categories.add(createCategory(1L, "root 1"));
            categories.add(createCategory(2L, "child 2", 1L));
            categories.add(createCategory(3L, "child 3", 2L));
            categories.add(createCategory(4L, "child 4", 5L));

            when(categoryRepo.findAll()).thenReturn(categories);

            Exception thrown = assertThrows(
                    RuntimeException.class,
                    () -> catalogueService.findAllCategory());

            assertThat(thrown.getMessage()).isEqualTo("no parent with id 5");
            verify(categoryRepo, times(1)).findAll();
        }

        private void assertCategory(CategoryDTO category, Long expectedId, String expectedName, List<CategoryDTO> expectedChild) {
            assertThat(category.getId()).isEqualTo(expectedId);
            assertThat(category.getName()).isEqualTo(expectedName);
            assertThat(category.getChildCategories()).containsExactlyInAnyOrderElementsOf(expectedChild);
        }
    }
}