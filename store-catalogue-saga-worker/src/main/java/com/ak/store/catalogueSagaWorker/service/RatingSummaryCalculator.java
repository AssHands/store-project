package com.ak.store.catalogueSagaWorker.service;

import com.ak.store.catalogueSagaWorker.model.entity.RatingSummary;

public interface RatingSummaryCalculator {
    void calculateCreating(RatingSummary ratingSummary, Integer grade);

    void calculateUpdating(RatingSummary ratingSummary, Integer newGrade, Integer oldGrade);

    void calculateDeleting(RatingSummary ratingSummary, Integer grade);
}
