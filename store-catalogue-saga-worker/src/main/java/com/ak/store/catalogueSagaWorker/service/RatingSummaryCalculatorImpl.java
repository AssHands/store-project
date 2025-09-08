package com.ak.store.catalogueSagaWorker.service;


import com.ak.store.catalogueSagaWorker.model.product.RatingSummary;
import org.springframework.stereotype.Component;

@Component
public class RatingSummaryCalculatorImpl implements RatingSummaryCalculator {
    public void calculateCreating(RatingSummary ratingSummary, Integer grade) {
        ratingSummary.setAmount(ratingSummary.getAmount() + 1);
        ratingSummary.setSum(ratingSummary.getSum() + grade);
        ratingSummary.setAverage(defineAvgGrade(ratingSummary.getSum(), ratingSummary.getAmount()));
    }

    public void calculateUpdating(RatingSummary ratingSummary, Integer newGrade, Integer oldGrade) {
        ratingSummary.setSum(ratingSummary.getSum() + (newGrade - oldGrade));
        ratingSummary.setAverage(defineAvgGrade(ratingSummary.getSum(), ratingSummary.getAmount()));
    }

    public void calculateDeleting(RatingSummary ratingSummary, Integer grade) {
        ratingSummary.setSum(ratingSummary.getSum() - grade);
        ratingSummary.setAmount(ratingSummary.getAmount() - 1);
        ratingSummary.setAverage(defineAvgGrade(ratingSummary.getSum(), ratingSummary.getAmount()));
    }

    private Float defineAvgGrade(Integer gradeSum, Integer gradeAmount) {
        Float avg = gradeSum.floatValue() / gradeAmount.floatValue();
        return avg.isNaN() ? 0f : avg;
    }
}