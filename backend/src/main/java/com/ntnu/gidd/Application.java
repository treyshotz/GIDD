package com.ntnu.gidd;

import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.util.TrainingLevelEnum;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


