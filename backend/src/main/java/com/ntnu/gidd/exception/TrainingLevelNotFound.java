package com.ntnu.gidd.exception;

public class TrainingLevelNotFound extends RuntimeException {
    public TrainingLevelNotFound() {
        super("Training level not found");
    }
}
