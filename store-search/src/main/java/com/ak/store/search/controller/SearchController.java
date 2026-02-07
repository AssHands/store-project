package com.ak.store.search.controller;

import com.ak.store.search.facade.SearchFacade;
import com.ak.store.search.mapper.SearchMapper;
import com.ak.store.search.model.form.SearchFilterForm;
import com.ak.store.search.model.form.SearchProductForm;
import com.ak.store.search.model.view.response.SearchFilterView;
import com.ak.store.search.model.view.response.SearchProductView;
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
    public SearchProductView searchAllProduct(@RequestBody @Valid SearchProductForm form) {
        var response = searchFacade.searchAllProduct(searchMapper.toSearchProductCommand(form));
        return searchMapper.toSearchProductView(response);
    }

    @PostMapping("filters")
    //todo заменить на view
    public SearchFilterView searchAllFilter(@AuthenticationPrincipal Jwt accessToken,
                                            @RequestBody @Valid SearchFilterForm form) {
        UUID userId = accessToken == null ? null : UUID.fromString(accessToken.getSubject());
        var command = searchMapper.toSearchFilterCommand(userId, form);

        var response = searchFacade.searchAllFilter(command);
        return searchMapper.toSearchFilterView(response);
    }
}
