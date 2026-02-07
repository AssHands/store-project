package com.ak.store.recommendation.service;

import com.ak.store.recommendation.model.command.WriteHistoryCommand;
import com.ak.store.recommendation.repo.SearchHistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepo searchHistoryRepo;

    List<Long> getOneByUserId(UUID userId) {
        return searchHistoryRepo.findOne(userId);
    }

    public void putAll(List<WriteHistoryCommand> commands) {
        Map<UUID, List<Long>> map = new HashMap<>(commands.size());

        for (var command : commands) {
            UUID userId = command.getUserId();
            Long categoryId = command.getCategoryId();
            map.computeIfAbsent(userId, k -> new ArrayList<>()).add(categoryId);
        }

        for (var entry : map.entrySet()) {
            searchHistoryRepo.putOne(entry.getKey(), entry.getValue());
        }
    }
}
