package com.ntnu.gidd.exception;

public class TrainingLevelNotFound extends EntityNotFoundException {
    public TrainingLevelNotFound() {
        super("Training level not found");
    }
}
