package com.ak.store.catalogue.utils;

import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ak.store.catalogue.TestCatalogueFactory.createCategoryDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class CatalogueUtilsTest {

    private CatalogueUtils catalogueUtils = new CatalogueUtils();

    @Test
    public void buildCategoryTree_withoutCategories() {
        List<CategoryDTO> categoryDTOList = catalogueUtils.buildCategoryTree(Collections.emptyList());

        assertThat(categoryDTOList).isEmpty();
    }

    @Test
    public void buildCategoryTree_withChildCategories() {
        List<CategoryDTO> categoryDTOList = List.of(
                createCategoryDTO(1L, "root 1"),
                createCategoryDTO(2L, "child 2", 1L),
                createCategoryDTO(3L, "child 3", 2L),
                createCategoryDTO(4L, "child 4", 1L),
                createCategoryDTO(5L, "root 5")
        );

        List<CategoryDTO> expectedTree = List.of(
                createCategoryDTO(1L, "root 1", List.of(
                        createCategoryDTO(2L, "child 2", 1L, List.of(
                                createCategoryDTO(3L, "child 3", 2L))),
                        createCategoryDTO(4L, "child 4", 1L))),
                createCategoryDTO(5L, "root 5")
        );

        List<CategoryDTO> actualTree = catalogueUtils.buildCategoryTree(categoryDTOList);

        assertThat(actualTree).isEqualTo(expectedTree);
    }
    @Test
    public void buildCategoryTree_withoutChildCategories() {
        List<CategoryDTO> categoryDTOList = List.of(
                createCategoryDTO(1L, "root 1"),
                createCategoryDTO(2L, "root 2"),
                createCategoryDTO(3L, "root 3")
        );

        List<CategoryDTO> expectedTree = List.of(
                createCategoryDTO(1L, "root 1"),
                createCategoryDTO(2L, "root 2"),
                createCategoryDTO(3L, "root 3")
        );

        List<CategoryDTO> actualTree = catalogueUtils.buildCategoryTree(categoryDTOList);

        assertThat(actualTree).isEqualTo(expectedTree);
    }

    @Test
    public void buildCategoryTree_withoutRootCategory() {
        List<CategoryDTO> categoryDTOList = List.of(
                createCategoryDTO(1L, "root 1"),
                createCategoryDTO(2L, "child 2", 1L),
                createCategoryDTO(3L, "child 3", 2L),
                createCategoryDTO(4L, "child 4", 5L)
        );

        Exception thrown = assertThrows(
                RuntimeException.class,
                () -> catalogueUtils.buildCategoryTree(categoryDTOList));

        assertThat(thrown.getMessage()).isEqualTo("no parent with id 5");
    }
}