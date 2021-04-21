package com.ntnu.gidd.service.traininglevel;

import com.ntnu.gidd.exception.TrainingLevelNotFound;
import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.util.TrainingLevelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TrainingLevelServiceImpl implements TrainingLevelService {

    @Autowired
    private TrainingLevelRepository trainingLevelRepository;

    @Override
    public TrainingLevel getTrainingLevelLow() {
        return trainingLevelRepository.findTrainingLevelByLevel(TrainingLevelEnum.Low)
                .orElseThrow(TrainingLevelNotFound::new);
    }

    @Override
    public TrainingLevel getTrainingLevelMedium() {
        return trainingLevelRepository.findTrainingLevelByLevel(TrainingLevelEnum.Medium)
                .orElseThrow(TrainingLevelNotFound::new);    }

    @Override
    public TrainingLevel getTrainingLevelHigh() {
        return trainingLevelRepository.findTrainingLevelByLevel(TrainingLevelEnum.High)
                .orElseThrow(TrainingLevelNotFound::new);    }
}
