package com.ntnu.gidd.factories;

import com.ntnu.gidd.model.Activity;
import org.springframework.beans.factory.FactoryBean;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static com.ntnu.gidd.utils.StringRandomizer.getRandomString;

public class ActivityFactory implements FactoryBean<Activity> {

    Random random = new Random();
    TrainingLevelFactory trainingLevelFactory = new TrainingLevelFactory();

    @Override
    public Activity getObject() throws Exception {
        return Activity.builder()
                .id(UUID.randomUUID())
                .title(getRandomString(5))
                .description(getRandomString(10))
                .start_date(LocalDateTime.now())
                .end_date(LocalDateTime.now())
                .signup_start(LocalDateTime.now())
                .signup_end(LocalDateTime.now())
                .closed(false)
                .capacity(random.nextInt(1000))
                .trainingLevel(trainingLevelFactory.getObject())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
