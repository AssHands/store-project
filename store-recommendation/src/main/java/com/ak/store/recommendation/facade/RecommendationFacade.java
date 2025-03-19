package com.ak.store.recommendation.facade;

import com.ak.store.common.event.search.SearchAllEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecommendationFacade {
    public void putInHistory(List<SearchAllEvent> searchAllEvents) {

    }
}
