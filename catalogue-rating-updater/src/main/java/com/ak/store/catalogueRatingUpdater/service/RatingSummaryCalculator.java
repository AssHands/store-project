package com.ak.store.catalogueRatingUpdater.service;

import com.ak.store.catalogueRatingUpdater.model.entity.RatingSummary;

public abstract class RatingSummaryCalculator {
    public static void calculateCreating(RatingSummary ratingSummary, Integer grade) {
        ratingSummary.setAmount(ratingSummary.getAmount() + 1);
        ratingSummary.setSum(ratingSummary.getSum() + grade);
        ratingSummary.setAverage(defineAvgGrade(ratingSummary.getSum(), ratingSummary.getAmount()));
    }

    public static void calculateUpdating(RatingSummary ratingSummary, Integer newGrade, Integer oldGrade) {
        ratingSummary.setSum(ratingSummary.getSum() + (newGrade - oldGrade));
        ratingSummary.setAverage(defineAvgGrade(ratingSummary.getSum(), ratingSummary.getAmount()));
    }

    public static void calculateDeleting(RatingSummary ratingSummary, Integer grade) {
        ratingSummary.setSum(ratingSummary.getSum() - grade);
        ratingSummary.setAmount(ratingSummary.getAmount() - 1);
        ratingSummary.setAverage(defineAvgGrade(ratingSummary.getSum(), ratingSummary.getAmount()));
    }

    private static Float defineAvgGrade(Integer gradeSum, Integer gradeAmount) {
        Float avg = gradeSum.floatValue() / gradeAmount.floatValue();
        return avg.isNaN() ? null : avg;
    }
}