package com.ak.store.search.controller;

import com.ak.store.search.facade.SearchFacade;
import com.ak.store.search.mapper.SearchMapper;
import com.ak.store.search.model.form.request.FilterSearchRequestForm;
import com.ak.store.search.model.form.request.ProductSearchRequestForm;
import com.ak.store.search.model.view.response.FilterSearchResponseView;
import com.ak.store.search.model.view.response.ProductSearchResponseView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/search")
public class SearchController {
    private final SearchFacade searchFacade;
    private final SearchMapper searchMapper;

    @PostMapping("products")
    public ProductSearchResponseView searchAllProduct(@RequestBody @Valid ProductSearchRequestForm request) {
        var response = searchFacade.searchAllProduct(searchMapper.toProductSearchRequestDTO(request));
        return searchMapper.toProductSearchResponseView(response);
    }

    @PostMapping("filters")
    //todo заменить на view
    public FilterSearchResponseView searchAllAvailableFilters(@AuthenticationPrincipal Jwt accessToken,
                                                              @RequestBody @Valid FilterSearchRequestForm request) {
        var userId = UUID.fromString(accessToken.getSubject());
        var response = searchFacade.searchAllFilter(userId, searchMapper.FilterSearchRequestDTO(request));
        return searchMapper.toFilterSearchResponseView(response);
    }
}
