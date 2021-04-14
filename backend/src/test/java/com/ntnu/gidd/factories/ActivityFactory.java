package com.ntnu.gidd.factories;

import com.ntnu.gidd.model.Activity;
import org.springframework.beans.factory.FactoryBean;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static com.ntnu.gidd.utils.StringRandomizer.getRandomString;

public class ActivityFactory implements FactoryBean<Activity> {

    Random random = new Random();

    @Override
    public Activity getObject() throws Exception {
        return new Activity(UUID.randomUUID(), getRandomString(5), getRandomString(10), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, random.nextInt(1000));
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
