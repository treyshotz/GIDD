package com.ntnu.gidd.exception;

public class TrainingLevelNotFound extends EntityNotFoundException {

    public static final String DEFAULT_MESSAGE = "Training level not found";

    public TrainingLevelNotFound(String error) {
        super(error);
    }

    public TrainingLevelNotFound() {
        super(DEFAULT_MESSAGE);
    }
}
