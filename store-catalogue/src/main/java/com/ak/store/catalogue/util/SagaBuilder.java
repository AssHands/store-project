package com.ak.store.catalogue.util;

import java.util.ArrayList;
import java.util.List;

public class SagaBuilder {

    private final List<Runnable> steps = new ArrayList<>();
    private final List<Runnable> compensations = new ArrayList<>();

    public SagaBuilder step(Runnable step) {
        steps.add(step);
        return this;
    }

    public SagaBuilder compensate(Runnable compensation) {
        compensations.add(compensation);
        return this;
    }

    public void execute() {
        for (Runnable step : steps) {
            try {
                step.run();
            } catch (Exception e) {
                compensations.forEach(Runnable::run);
                throw new RuntimeException("Saga execution failed", e);
            }
        }
    }
}